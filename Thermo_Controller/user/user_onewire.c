#include "esp_common.h"
#include "gpio.h"


////#include "ets_sys.h"
////#include "esp_libc.h"
//#include "osapi.h"
//#include "eagle_soc.h"
//#include "gpio.h"



/////#include "os_type.h"
//#include "user_config.h"

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

LOCAL xTaskHandle onewire_client_handle;

#define ONEWIRE_CLIENT_THREAD_NAME         "onewire_client_thread"
#define ONEWIRE_CLIENT_THREAD_STACK_WORDS  2048
#define ONEWIRE_CLIENT_THREAD_PRIO         14


/* Макросы для "дергания ноги" и изменения режима ввода/вывода */
#define THERM_INPUT_MODE()  gpio_output_set(0, 0, 0, BIT12)
#define THERM_OUTPUT_MODE() gpio_output_set(0, BIT12, BIT12, 0)
#define THERM_LOW()         gpio_output_set(0, BIT12, BIT12, 0)
#define THERM_HIGH()        gpio_output_set(BIT12, 0, BIT12, 0)

#define THERM_GET_VAL() GPIO_INPUT_GET(12)

#define _delay_us os_delay_us

#define DEBUG

uint8_t therm_reset();
void therm_write_bit(uint8_t bit);
uint8_t therm_read_bit(void);
uint8_t therm_read_byte(void);
void therm_write_byte(uint8_t byte);
uint8_t therm_read_temperature(uint8_t *buffer);
uint16_t GetTemperature(void);
void GetID_DS18B20(uint8_t *buffer);

// команды управления датчиком
#define THERM_CMD_CONVERTTEMP 0x44
#define THERM_CMD_RSCRATCHPAD 0xbe
#define THERM_CMD_WSCRATCHPAD 0x4e
#define THERM_CMD_CPYSCRATCHPAD 0x48
#define THERM_CMD_RECEEPROM 0xb8
#define THERM_CMD_RPWRSUPPLY 0xb4
#define THERM_CMD_SEARCHROM 0xf0
#define THERM_CMD_READROM 0x33
#define THERM_CMD_MATCHROM 0x55
#define THERM_CMD_SKIPROM 0xcc
#define THERM_CMD_ALARMSEARCH 0xec
 
#define THERM_DECIMAL_STEPS_12BIT 625 //.0625

#define ETS_GPIO_INTR_ENABLE()  _xt_isr_unmask(1 << ETS_GPIO_INUM)  //ENABLE INTERRUPTS
#define ETS_GPIO_INTR_DISABLE() _xt_isr_mask(1 << ETS_GPIO_INUM)    //DISABLE INTERRUPTS


LOCAL os_timer_t wr_onewire_timer;
LOCAL os_timer_t time1;

LOCAL uint8 LED_counter;
LOCAL uint8_t temperature_buffer[3];
LOCAL uint8_t rom_buffer[6];

extern uint16_t min_temp;
extern uint16_t max_temp;

int whatyouwant;

LOCAL uint8 start_nasos; //переменная для счетчика, что бы включить насос без ошибочно
LOCAL uint16_t prev_temp; //

// gpio interrupt handler. See below
LOCAL void  gpio_intr_handler(int * dummy);

void ICACHE_FLASH_ATTR
GetID_DS18B20(uint8_t *buffer)
{
	memcpy(buffer, rom_buffer, 6);
}

uint16_t ICACHE_FLASH_ATTR
GetTemperature(void)
{
	uint16_t decimal = temperature_buffer[1];
	//printf("decimal = %X\n", decimal);
	decimal = decimal<<8;
	//printf("decimal = %X\n", decimal);
	decimal |= temperature_buffer[0];
	//printf("decimal = %X\n", decimal);
	decimal = decimal>>2;
	//printf("decimal = %X\n", decimal);
	//printf("GetTemperature: %d.%d C\n", (decimal>>2), (decimal&0x03)*25);
	return decimal;
}

// сброс датчика
uint8_t therm_reset(){
    uint8_t i;
    // опускаем ногу вниз на 480uS
    THERM_LOW();
    THERM_OUTPUT_MODE();
    _delay_us(480);             // замените функцию задержки на свою
    // подымаем линию на 60uS
    THERM_INPUT_MODE();
    _delay_us(60);
    // получаем значение на линии в период 480uS
    i=THERM_GET_VAL();
    _delay_us(480);
    // возвращаем значение (0=OK, 1=датчик не найден)
    return i;
}
 
 
// функция отправки бита
void therm_write_bit(uint8_t bit){
    // опускаем на 1uS
    THERM_LOW();
    THERM_OUTPUT_MODE();
    _delay_us(1);
    // если хотим отправить 1, поднимаем линию (если нет, оставляем как есть)
    if(bit) THERM_INPUT_MODE();
    // ждем 60uS и поднимаем линию
    _delay_us(60);
    THERM_INPUT_MODE();
}
 
// чтение бита
uint8_t therm_read_bit(void){
    uint8_t bit=0;
    // опускаем на 1uS
    THERM_LOW();
    THERM_OUTPUT_MODE();
    _delay_us(1);
    // поднимаем на 14uS
    THERM_INPUT_MODE();
    _delay_us(14);
    // читаем состояние
    if(THERM_GET_VAL()) bit=1;
    // ждем 45 мкс и возвращаем значение
    _delay_us(45);
    return bit;
}
 
// читаем байт
uint8_t therm_read_byte(void){
    uint8_t i=8, n=0;
    while(i--){
        // сдвигаем в право на 1 и сохраняем следующее значение
        n>>=1;
        n|=(therm_read_bit()<<7);
    }
    return n;
}
 
// отправляем байт
void therm_write_byte(uint8_t byte){
    uint8_t i=8;
    while(i--){
        // отправляем бит и сдвигаем вправо на 1
        therm_write_bit(byte&1);
        byte>>=1;
    }
}

// читаем ROM датчика
void therm_read_ROM(void){
	uint8_t buffer_byte[9];
	uint8_t buffer[20];
	
	printf("I in therm_read_ROM\n");
 
    if (therm_reset()) {
        printf("Not found DS18B20.\n");
    }
	else {
	
		therm_write_byte(THERM_CMD_READROM);
	 
		buffer_byte[0]=therm_read_byte();
		buffer_byte[1]=therm_read_byte();
		buffer_byte[2]=therm_read_byte();
		buffer_byte[3]=therm_read_byte();
		buffer_byte[4]=therm_read_byte();
		buffer_byte[5]=therm_read_byte();
		buffer_byte[6]=therm_read_byte();
		buffer_byte[7]=therm_read_byte();
		//buffer_byte[8]=therm_read_byte();

		rom_buffer[0]=buffer_byte[1];
		rom_buffer[1]=buffer_byte[2];
		rom_buffer[2]=buffer_byte[3];
		rom_buffer[3]=buffer_byte[4];
		rom_buffer[4]=buffer_byte[5];
		rom_buffer[5]=buffer_byte[6];
	}

    if (therm_reset()) {
        printf("Not found DS18B20.\n");
    }/*
	uint8_t i=0;
	do {
		if ((0xff & buffer_byte[i]) > 15) os_sprintf(&buffer[i*2], "%X", (0xff & buffer_byte[i])); else os_sprintf(&buffer[i*2], "0%X", (0xff & buffer_byte[i]));
		i++;
	} while (i < 8);
	
	os_sprintf(&buffer[16], "\n");
	printf("%s", buffer);
*/
}
	
// читаем температуру с датчика
uint8_t therm_read_temperature(uint8_t *buffer){
    //uint8_t temperature[2];
    int8_t digit;
    uint16_t decimal;
	uint8_t buffer_byte[9];
	
	//printf("I in therm_read_temperature\n");
 
    if (therm_reset())
    {
        printf("Not found DS18B20.\n");
		return -1;
    }
	else {
		therm_write_byte(THERM_CMD_SKIPROM);
		therm_write_byte(THERM_CMD_CONVERTTEMP);
	 
		while(!therm_read_bit());
	 
		therm_reset();
		therm_write_byte(THERM_CMD_SKIPROM);
		therm_write_byte(THERM_CMD_RSCRATCHPAD);
	 
		buffer_byte[0]=therm_read_byte();
		buffer_byte[1]=therm_read_byte();
		buffer_byte[2]=therm_read_byte();
		buffer_byte[3]=therm_read_byte();
		buffer_byte[4]=therm_read_byte();
		buffer_byte[5]=therm_read_byte();
		buffer_byte[6]=therm_read_byte();
		buffer_byte[7]=therm_read_byte();
		buffer_byte[8]=therm_read_byte();
	}
    if (therm_reset())
    {
        printf("Not found DS18B20.\n");
    }
/*
    temperature[0]=buffer_byte[0];
    temperature[1]=buffer_byte[1];
	
    digit=temperature[0]>>4;
    digit|=(temperature[1]&0x7)<<4;
 
    decimal=temperature[0]&0xf;
    decimal*=THERM_DECIMAL_STEPS_12BIT;
*/
	buffer[0] = buffer_byte[0];
	buffer[1] = buffer_byte[1];
	
	//printf("%X - %X\n", buffer[1], buffer[0]);
	
	//decimal = buffer_byte[1];
	//decimal = decimal<<8;
	//decimal |= buffer_byte[0];
	//decimal = decimal>>2;
    //printf("%d.%d C\n", (decimal>>2), (decimal&0x03)*25);

/*
	uint8_t i=0;
	do {
		if ((0xff & buffer_byte[i]) > 15) os_sprintf(&buffer[i*2], "%X", (0xff & buffer_byte[i])); else os_sprintf(&buffer[i*2], "0%X", (0xff & buffer_byte[i]));
		i++;
	} while (i < 9);
	
	os_sprintf(&buffer[18], "\n");
	printf("%s", buffer);
*/
	/* Проверим Configuration Register */
	digit = (buffer_byte[4]>>5)&0x03;
	if (digit != 1){
		printf("Configuration Register = %X\n", digit);
		
		/*Если разрешение не равно 10 бит - переключим на 10 бит
		9, 10, 11, 12 bits - 0.5°C, 0.25°C, 0.125°C, 0.0625°C */
		
		printf("Configuration Register not 10 bits\n");
		
		therm_reset();
		therm_write_byte(THERM_CMD_SKIPROM);		
		therm_write_byte(THERM_CMD_WSCRATCHPAD);
		therm_write_byte(buffer_byte[2]);
		therm_write_byte(buffer_byte[3]);
		therm_write_byte(0x3F);
		
		therm_reset();
		
		therm_write_byte(THERM_CMD_SKIPROM);
		therm_write_byte(THERM_CMD_RSCRATCHPAD);
	 
		buffer_byte[0]=therm_read_byte();
		buffer_byte[1]=therm_read_byte();
		buffer_byte[2]=therm_read_byte();
		buffer_byte[3]=therm_read_byte();
		buffer_byte[4]=therm_read_byte();
		buffer_byte[5]=therm_read_byte();
		buffer_byte[6]=therm_read_byte();
		buffer_byte[7]=therm_read_byte();
		buffer_byte[8]=therm_read_byte();

		therm_reset();

		therm_write_byte(THERM_CMD_SKIPROM);
		therm_write_byte(THERM_CMD_CPYSCRATCHPAD);
		
		while(!therm_read_bit());
	}
	return 1;
}

void stoppump(void* arg)
{
#ifdef DEBUG
    printf("t1 stoppump\n");
#endif
	gpio_output_set(0, BIT13, BIT13, 0); // выключить насос
	//gpio_output_set(0, BIT4, BIT4, 0);
	//gpio_output_set(0, BIT5, BIT5, 0);
	os_timer_disarm(&time1);
//	ETS_GPIO_INTR_ENABLE();
}


void ICACHE_FLASH_ATTR
user_onewire_work(void)
{	
	printf("I in user_onewire_work\n");

	uint16_t decimal;
	uint32	value;
	uint8	*save_therm	=	(uint8	*)&value;
	
	//wifi_station_disconnect();
	//wifi_set_opmode_current(NULL_MODE);
	// Disable interrupts by GPIO
    //ETS_GPIO_INTR_DISABLE();
	
	//_delay_us(3000);
	
	if (therm_read_temperature(temperature_buffer))
	{
	
	//ETS_GPIO_INTR_ENABLE();
	//wifi_set_opmode_current(STATIONAP_MODE);
	//wifi_station_connect();

	


/*	
	spi_flash_read(0x7B	* SPI_FLASH_SEC_SIZE,	(uint32	*)save_therm,	4);
	os_printf("0x7B	sec: %02x%02x%02x%02x\n", save_therm[0], save_therm[1], save_therm[2], save_therm[3]);
	min_temp = save_therm[0];
	max_temp = save_therm[1];
	
	printf("min_temp = %d.%d\n", (min_temp>>2), (min_temp&0x03)*25);
	printf("max_temp = %d.%d\n", (max_temp>>2), (max_temp&0x03)*25);
	*/
		decimal = GetTemperature();
#ifdef DEBUG
		printf("Temperature = %d.%d\n", (decimal>>2), (decimal&0x03)*25);
		printf("decimal = %d\n", decimal);
#endif

	
		if ( decimal >= 4000 )
		{
			therm_reset();
			printf("therm_reset\n");
		}
		else
		{
			if ( decimal >= max_temp )
			{
				//turn off relle
				printf("turn off relle\n");
				gpio_output_set(0, BIT5, BIT5, 0);
			}
			if ( decimal < min_temp )
			{
				//turn on relle
				printf("turn on relle\n");
				gpio_output_set(BIT5, 0, BIT5, 0);
			}
		}
	}


		
   //disarm timer first
    os_timer_disarm(&wr_onewire_timer);
	/*
	if (LED_counter == 1){
		//printf("LED ON\n");
		gpio_output_set(0, BIT13, BIT13, 0);
		//gpio_output_set(0, 1<<I2C_MASTER_SCL_GPIO, 1<<I2C_MASTER_SCL_GPIO, 0);
		LED_counter++;
	}
	else{
		//printf("LED OFF\n");
		gpio_output_set(BIT13, 0, BIT13, 0);
		//gpio_output_set(1<<I2C_MASTER_SCL_GPIO, 0, 1<<I2C_MASTER_SCL_GPIO, 0);
		LED_counter--;
		
		//therm_read_ROM();
	}
	*/


   //re-arm timer to check ip
	os_timer_setfn(&wr_onewire_timer, (os_timer_func_t *)user_onewire_work, NULL);
	os_timer_arm(&wr_onewire_timer, 30000, 0);
	
}

static void ICACHE_FLASH_ATTR onewire_client_thread(void* pvParameters)
{
	int rc = 0, count = 0;
	char *content = (char *)os_malloc(512);
	uint16_t decimal;
	uint32	value;
	uint8	*save_therm	=	(uint8	*)&value;
	
    //printf("onewire client thread starts\n");

	while (++count) {
#ifdef DEBUG
		printf("ONEWIRE thread, count number is %d, rc = %d\n", count, rc);
		//system_print_meminfo();
		//printf("system_get_free_heap_size = %d\n", system_get_free_heap_size());
#endif
/*
    uint32 gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);
	printf("I am in gpio_intr_handler (onewire_client_thread). gpio_status = %d\n", gpio_status);
*/
		if (therm_reset())
		{
			printf("Not found DS18B20.\n");
		} else {
			if(therm_read_temperature(temperature_buffer))
			{
				decimal = GetTemperature();

/*		
		spi_flash_read(0x7B	* SPI_FLASH_SEC_SIZE,	(uint32	*)save_therm,	4);
		//os_printf("0x7B	sec: %02x%02x%02x%02x\n", save_therm[0], save_therm[1], save_therm[2], save_therm[3]);
		min_temp = save_therm[0];
		max_temp = save_therm[1];
*/

#ifdef DEBUG

				printf("min_temp = %d.%d;   ", (min_temp>>2), (min_temp&0x03)*25);
				printf("max_temp = %d.%d;   ", (max_temp>>2), (max_temp&0x03)*25);
				printf("Temperature = %d.%d, %d\n", (decimal>>2), (decimal&0x03)*25, decimal);
#endif
				uint8_t i = GPIO_INPUT_GET(14);
				printf("GPIO_INPUT_GET bit %d\n", i);
				
				if (i == 1 )
				{
					start_nasos++;
					
					if (start_nasos >= 5) {
						gpio_output_set(BIT13, 0, BIT13, 0); //включить насос

						os_timer_disarm(&time1);
						os_timer_setfn(&time1, stoppump, NULL);
						os_timer_arm(&time1, 150000, 1);
						start_nasos = 0;
					}
				}
				else {
					start_nasos = 0;
				}
				
				if (rc > 0) {
					rc++;
					if (rc > 16) {
						//rc = 0;
						if(decimal <= prev_temp) {
							// прошла минута нагрева, но температура не поменялась.
							gpio_output_set(0, BIT5, BIT5, 0);
#ifdef DEBUG
							printf("turn off relle\n");
#endif
						}
					}
					if (rc > 40) {
						rc = 0;
					}
				}

	
				if ( decimal >= 2000 )
				{
					therm_reset();
					printf("therm_reset\n");
				}
				else
				{
					if ( decimal >= max_temp )
					{
						//turn off relle
		#ifdef DEBUG
						printf("turn off relle\n");
		#endif
						gpio_output_set(0, BIT5, BIT5, 0);
					}
					if ( decimal < min_temp && rc == 0)
					{
						//turn on relle
		#ifdef DEBUG
						printf("turn on relle\n");
		#endif
						gpio_output_set(BIT5, 0, BIT5, 0);
						prev_temp = decimal;
						rc = 10;
					}
				}
			}
		}
        vTaskDelay(10000 / portTICK_RATE_MS);  //send every 1 seconds
    }

#ifdef DEBUG
    printf("onewire_client_thread going to be deleted\n");
#endif
    vTaskDelete(NULL);
	free(content);
    return;
}

// interrupt handler
// this function will be executed on any edge of GPIO0
LOCAL void io_intr_handler(int * dummy)
{
	// clear gpio status. Say ESP8266EX SDK Programming Guide in  5.1.6. GPIO interrupt handler
	uint8_t i;

    uint32 gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);
	
	printf("I am in gpio_intr_handler. gpio_status = %d\n", gpio_status);
	
// if the interrupt was by GPIO14
    if (gpio_status & BIT(14))
    {
		printf("BIT-14 enable\n");
// disable interrupt for GPIO14
        gpio_pin_intr_state_set(GPIO_ID_PIN(14), GPIO_PIN_INTR_DISABLE);

		i = GPIO_INPUT_GET(14);
		printf("GPIO_INPUT_GET bit %d\n", i);
		
		if (i == 1 )
		{
			gpio_output_set(BIT13, 0, BIT13, 0); //включить насос

			os_timer_disarm(&time1);
			os_timer_setfn(&time1, stoppump, NULL);
			os_timer_arm(&time1, 150000, 1);
		}

//clear interrupt status for GPIO14
        GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status & BIT(14));

// Reactivate interrupts for GPIO14
        gpio_pin_intr_state_set(GPIO_ID_PIN(14), GPIO_PIN_INTR_ANYEDGE);
    }
	else if (gpio_status & BIT(4))
    {
		printf("BIT-04 enable\n");
// disable interrupt for GPIO14
        gpio_pin_intr_state_set(GPIO_ID_PIN(4), GPIO_PIN_INTR_DISABLE);

// Do something, for example, increment whatyouwant indirectly
        //(*dummy)++;

		i = GPIO_INPUT_GET(4);
		printf("GPIO_INPUT_GET bit %d\n", i);

		if (i == 0 )
		{
			gpio_output_set(0, BIT13, BIT13, 0); // выключить насос
		}
//		else {
//			gpio_output_set(0, BIT13, BIT13, 0); // выключить насос
//		}


//clear interrupt status for GPIO4
        GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status & BIT(4));

// Reactivate interrupts for GPIO4
        gpio_pin_intr_state_set(GPIO_ID_PIN(4), GPIO_PIN_INTR_ANYEDGE);
    }
}


void ICACHE_FLASH_ATTR
user_onewire_init()
{
	start_nasos = 0;
	// Initialize the GPIO subsystem.
    //gpio_init();
	
	//PIN_FUNC_SELECT(PERIPHS_IO_MUX_GPIO2_U, FUNC_GPIO2);

    //Set GPIO2 low
    //gpio_output_set(0, BIT2, BIT2, 0);
	
    //Set GPIO2 to output mode
    PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTCK_U, FUNC_GPIO13);
    PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTDI_U, FUNC_GPIO12);
	PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTMS_U, FUNC_GPIO14);
	
	PIN_FUNC_SELECT(PERIPHS_IO_MUX_GPIO4_U, FUNC_GPIO4);
	PIN_FUNC_SELECT(PERIPHS_IO_MUX_GPIO5_U, FUNC_GPIO5);

	
	
	

	//gpio_output_conf(0, 1<<ir_out_gpio_num, 1<<ir_out_gpio_num, 0)
	//gpio16_output_conf();
	//gpio16_output_set(0);
	//gpio16_input_conf();
	//gpio16_input_get();


/*	
	GPIO_ConfigTypeDef io_in_conf_16;
    io_in_conf_16.GPIO_IntrType = GPIO_PIN_INTR_NEGEDGE;
    io_in_conf_16.GPIO_Mode = GPIO_Mode_Output;
    io_in_conf_16.GPIO_Pin = GPIO_Pin_16;
    io_in_conf_16.GPIO_Pullup = GPIO_PullUp_DIS;
    gpio_config(&io_in_conf_16);
*/

	//gpio_output_set(0, BIT4, BIT4, 0);
	gpio_output_set(0, BIT5, BIT5, 0);
	
	
	gpio_output_set(0, BIT13, BIT13, 0);
	
	//PIN_PULLUP_DIS(PERIPHS_IO_MUX_MTMS_U);
	
	//gpio_output_set(0, BIT14, BIT14, 0); //LOW
	//gpio_output_set(BIT14, 0, BIT14 0); //HIGH
	
	//gpio_output_set(0,	0,	0,	BIT14);// Set GPIO14 as input
	
	
    //PIN_FUNC_SELECT(PERIPHS_IO_MUX_GPIO5_U, FUNC_GPIO5);
	//PIN_PULLUP_EN(PERIPHS_IO_MUX_GPIO5_U);
	//PIN_PULLUP_DIS(PERIPHS_IO_MUX_GPIO5_U);
	
	//gpio_output_set(0, 0, 0, BIT5); //input
	//gpio_output_set(0, BIT5, BIT5, 0); //OUTPUT
	//+gpio_output_set(0, BIT5, BIT5, 0); //low
	//gpio_output_set(BIT5, 0, BIT5, 0); //hight
	
	//PIN_PULLUP_EN(FUNC_GPIO4);

	//PIN_PULLUP_EN(BIT16);

//  PIN_PULLUP_DIS(PIN_NAME)
//  PIN_PULLUP_EN(PIN_NAME)
//  PIN_PULLDWN_DIS(PIN_NAME)
//  PIN_PULLDWN_EN(PIN_NAME)

	therm_read_ROM();

	// Disable interrupts by GPIO
    ETS_GPIO_INTR_DISABLE();
	
    GPIO_ConfigTypeDef io_in_conf;
//     io_in_conf.GPIO_IntrType = GPIO_PIN_INTR_NEGEDGE;
	io_in_conf.GPIO_IntrType = GPIO_PIN_INTR_ANYEDGE;
    io_in_conf.GPIO_Mode = GPIO_Mode_Input;
    io_in_conf.GPIO_Pin = GPIO_Pin_14 | GPIO_Pin_4;
	//io_in_conf.GPIO_Pin = GPIO_Pin_4;
    io_in_conf.GPIO_Pullup = GPIO_PullUp_DIS;
    gpio_config(&io_in_conf);

    gpio_intr_handler_register(io_intr_handler, &whatyouwant);
	
	ETS_GPIO_INTR_ENABLE();

	int ret;
    ret = xTaskCreate(onewire_client_thread,
                      ONEWIRE_CLIENT_THREAD_NAME,
                      ONEWIRE_CLIENT_THREAD_STACK_WORDS,
                      NULL,
                      ONEWIRE_CLIENT_THREAD_PRIO,
                      &onewire_client_handle);

    if (ret != pdPASS)  {
        printf("onewire create client thread %s failed\n", ONEWIRE_CLIENT_THREAD_NAME);
    }
	
	
/*
	os_timer_disarm(&wr_onewire_timer);
	os_timer_setfn(&wr_onewire_timer, (os_timer_func_t *)user_onewire_work, NULL);
	os_timer_arm(&wr_onewire_timer, 10000, 0);
	LED_counter = 1;
	therm_read_ROM();
	therm_read_temperature(temperature_buffer);
*/
}


// GPIO13 - BIT13 - Насос
// GPIO12 - BIT12 - OneWire
// GPIO05 - Нагреватель
// GPIO04 - LED LED
// GPIO14 - 1 датчик уровня воды


/*
28
FF949B831603
B7


chip_id = 7BBFD

*/
