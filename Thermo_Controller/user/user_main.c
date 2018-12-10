/*
 * ESPRESSIF MIT License
 *
 * Copyright (c) 2015 <ESPRESSIF SYSTEMS (SHANGHAI) PTE LTD>
 *
 * Permission is hereby granted for use on ESPRESSIF SYSTEMS ESP8266 only, in which case,
 * it is free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
 
 /*
export SDK_PATH=/media/sf_ESP8266/ESP8266_RTOS_SDK-2.0.0
export BIN_PATH=/media/sf_ESP8266/bin

make BOOT=new APP=1 SPI_SPEED=40 SPI_MODE=QIO SPI_SIZE_MAP=2


sudo /usr/local/bin/esptool.py  -p /dev/ttyS3 --before default_reset -a hard_reset --no-stub chip_id


*/


#include "esp_common.h"
#include "user_config.h"
#include "uart.h"
//#include "gpio.h"


LOCAL uint8 LED_counter;
LOCAL uint32 chip_id;
uint16_t min_temp;
uint16_t max_temp;

uint8 wifi_start;
//uint8 xTask_MQTT;
uint8 xTask_webclient;
uint8 xTask_onewire;

//int whatyouwant;

#define ETS_GPIO_INTR_ENABLE()  _xt_isr_unmask(1 << ETS_GPIO_INUM)  //ENABLE INTERRUPTS
#define ETS_GPIO_INTR_DISABLE() _xt_isr_mask(1 << ETS_GPIO_INUM)    //DISABLE INTERRUPTS
#define ETS_GPIO_INTR_ATTACH(func, arg) gpio_intr_handler_register((func), (void *)(arg)) 


/******************************************************************************
 * FunctionName : user_rf_cal_sector_set
 * Description  : SDK just reversed 4 sectors, used for rf init data and paramters.
 *                We add this function to force users to set rf cal sector, since
 *                we don't know which sector is free in user's application.
 *                sector map for last several sectors : ABCCC
 *                A : rf cal
 *                B : rf init data
 *                C : sdk parameters
 * Parameters   : none
 * Returns      : rf cal sector
*******************************************************************************/
uint32 user_rf_cal_sector_set(void)
{
    flash_size_map size_map = system_get_flash_size_map();
    uint32 rf_cal_sec = 0;

    switch (size_map) {
        case FLASH_SIZE_4M_MAP_256_256:
            rf_cal_sec = 128 - 5;
            break;

        case FLASH_SIZE_8M_MAP_512_512:
            rf_cal_sec = 256 - 5;
            break;

        case FLASH_SIZE_16M_MAP_512_512:
        case FLASH_SIZE_16M_MAP_1024_1024:
            rf_cal_sec = 512 - 5;
            break;

        case FLASH_SIZE_32M_MAP_512_512:
        case FLASH_SIZE_32M_MAP_1024_1024:
            rf_cal_sec = 1024 - 5;
            break;
        case FLASH_SIZE_64M_MAP_1024_1024:
            rf_cal_sec = 2048 - 5;
            break;
        case FLASH_SIZE_128M_MAP_1024_1024:
            rf_cal_sec = 4096 - 5;
            break;
        default:
            rf_cal_sec = 0;
            break;
    }

    return rf_cal_sec;
}

void wifi_event_handler_cb(System_Event_t *event)
{
	//printf("I am in wifi_event_handler_cb, wifi_start = %d, xTask_webclient = %d\n", wifi_start, xTask_webclient);
	
	if (wifi_start == 1 && xTask_webclient == 0)
	{
		printf("RESTART user_conn_init\n");
		//xTask_webclient = 1;
		user_webclient_init();
	}
	
	
    if (event == NULL) {
        return;
    }

    switch (event->event_id) {
        case EVENT_STAMODE_GOT_IP:
            printf("sta got ip ,create task and free heap size is %d\n", system_get_free_heap_size());
			wifi_start = 1;
            //user_conn_init();
			user_webclient_init();
            break;

        case EVENT_STAMODE_CONNECTED:
            printf("sta connected\n");
            break;

        case EVENT_STAMODE_DISCONNECTED:
            //wifi_station_connect();
			printf("wifi_station_connect\n");
            break;

        case EVENT_SOFTAPMODE_PROBEREQRECVED:
            //printf("EVENT_SOFTAPMODE_PROBEREQRECVED\n");
			//wifi_station_disconnect();
            break;
			
        default:
            break;
    }
}

void ICACHE_FLASH_ATTR user_set_station_config(void)
{
	struct station_config stationConf;
	stationConf.bssid_set = 0; //need not check MAC address	of AP
	bzero(&stationConf, sizeof(struct station_config));
    sprintf(stationConf.ssid, SSID);
    sprintf(stationConf.password, PASSWORD);
	wifi_station_set_config(&stationConf);
}

void ICACHE_FLASH_ATTR
GetChipID(uint8_t *buffer)
{
	uint8 mybyte = (chip_id>>24)&0xff;
	buffer[0] = mybyte;
	//printf("chip_id = %X\n", mybyte);
	mybyte = (chip_id>>16)&0xff;
	buffer[1] = mybyte;
	//printf("chip_id = %X\n", mybyte);
	mybyte = (chip_id>>8)&0xff;
	buffer[2] = mybyte;
	//printf("chip_id = %X\n", mybyte);
	mybyte = (chip_id)&0xff;
	buffer[3] = mybyte;
	//printf("chip_id = %X\n", mybyte);
}

/*
void io_intr_handler(void)
{
	// clear gpio status. Say ESP8266EX SDK Programming Guide in  5.1.6. GPIO interrupt handler
	uint8_t i;
	
	printf("I am in gpio_intr_handler\n");

    uint32 gpio_status = GPIO_REG_READ(GPIO_STATUS_ADDRESS);

// if the interrupt was by GPIO14
    if (gpio_status & BIT(14))
    {
// disable interrupt for GPIO14
        gpio_pin_intr_state_set(GPIO_ID_PIN(14), GPIO_PIN_INTR_DISABLE);

// Do something, for example, increment whatyouwant indirectly
        //(*dummy)++;
		
		i = GPIO_INPUT_GET(14);
		printf("GPIO_INPUT_GET bit %d\n", i);

//clear interrupt status for GPIO14
        GPIO_REG_WRITE(GPIO_STATUS_W1TC_ADDRESS, gpio_status & BIT(14));

// Reactivate interrupts for GPIO14
        gpio_pin_intr_state_set(GPIO_ID_PIN(14), GPIO_PIN_INTR_ANYEDGE);
    }
}
*/

/******************************************************************************
 * FunctionName : user_init
 * Description  : entry of user application, init user function here
 * Parameters   : none
 * Returns      : none
*******************************************************************************/
void user_init(void)
{
	chip_id = system_get_chip_id();
	uart_init_new();    //UART0 Initialize
	UART_SetBaudrate(UART1, BIT_RATE_115200);

	uint32	value;
	uint8	*save_therm	=	(uint8	*)&value;
	
	spi_flash_read(0x7B	* SPI_FLASH_SEC_SIZE, (uint32 *)save_therm, 4);

	if (save_therm[0] == 0xff || save_therm[1] == 0xff)
	{
		save_therm[0] = 31*4;
		save_therm[1] = 35*4;

		spi_flash_erase_sector(0x7B);
		spi_flash_write(0x7B * SPI_FLASH_SEC_SIZE, (uint32 *)save_therm, 4);
	}
	else
	{
		min_temp = save_therm[0];
		max_temp = save_therm[1];
	}

#ifdef DEBUG
	printf("0x7B sec: %02x%02x%02x%02x\n", save_therm[0], save_therm[1], save_therm[2], save_therm[3]);
	printf("min_temp = %d.%d\n", (min_temp>>2), (min_temp&0x03)*25);
	printf("max_temp = %d.%d\n", (max_temp>>2), (max_temp&0x03)*25);
	printf("Temperature = %d.%d, %d\n", (decimal>>2), (decimal&0x03)*25, decimal);
#endif


	xTask_webclient = 0;
	xTask_onewire = 0;
	wifi_start = 0;

	wifi_set_opmode(STATIONAP_MODE);
    wifi_station_set_hostname("NET_Vit");
	user_set_station_config();

	PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTCK_U, FUNC_GPIO13);
	//LED_counter = 1;
	
	//gpio_output_set(0, BIT13, BIT13, 0); //set ON
	gpio_output_set(BIT13, 0, BIT13, 0); //set OFF
	
    //os_printf("SDK version:%s %d\n", system_get_sdk_version(), system_get_free_heap_size());

	wifi_set_event_handler_cb(wifi_event_handler_cb);
	
	user_onewire_init();
/*
	PIN_FUNC_SELECT(PERIPHS_IO_MUX_MTMS_U, FUNC_GPIO14);
	PIN_PULLUP_DIS(PERIPHS_IO_MUX_MTMS_U);
	
	//gpio_output_set(0, BIT14, BIT14, 0); //LOW
	//gpio_output_set(BIT14, 0, BIT14 0); //HIGH
	
	gpio_output_set(0,	0,	0,	BIT14);// Set GPIO14 as input
	
		// Disable interrupts by GPIO
    ETS_GPIO_INTR_DISABLE();
	
    GPIO_ConfigTypeDef io_in_conf;
    io_in_conf.GPIO_IntrType = GPIO_PIN_INTR_NEGEDGE;
    io_in_conf.GPIO_Mode = GPIO_Mode_Input;
    io_in_conf.GPIO_Pin = GPIO_Pin_14;
    io_in_conf.GPIO_Pullup = GPIO_PullUp_DIS;
    gpio_config(&io_in_conf);

    gpio_intr_handler_register(io_intr_handler, NULL);
	
	ETS_GPIO_INTR_ENABLE();
*/
}
