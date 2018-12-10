#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

#include "ets_sys.h"
#include "osapi.h"

//#include "uart.h"
#include "user_interface.h"
#include "espconn.h"
#include "mem.h"

//#include "user_webclient.h"


LOCAL xTaskHandle web_client_handle;

extern uint8 xTask_webclient;

#define WEB_CLIENT_THREAD_NAME         "web_client_thread"
#define WEB_CLIENT_THREAD_STACK_WORDS  2048
#define WEB_CLIENT_THREAD_PRIO         5

#define DEBUG

#define DNS_ENABLE

#define NET_DOMAIN "livefood.in.ua"
#define pheadbuffer "POST /api/index.php HTTP/1.1\r\nUser-Agent: curl/7.77.0\r\nHost: %s\r\nAccept: */*\r\nContent-Length: %d\r\n\r\n%s"

#define packet_size   (2 * 1024)


//struct espconn *pespconn_main;

LOCAL char *content;
LOCAL os_timer_t test_timer, send_data_timer;
LOCAL struct espconn user_tcp_conn;
LOCAL struct _esp_tcp user_tcp;
ip_addr_t tcp_server_ip;
//LOCAL uint32 chip_id;

extern uint16_t min_temp;
extern uint16_t max_temp;
extern void GetChipID(uint8_t *buffer);

static uint32 dat_sumlength = 0;

/*
void user_check_ip(void);
LOCAL void user_dns_check_cb(void *arg);
LOCAL void user_dns_found(const char *name, ip_addr_t *ipaddr, void *arg);
LOCAL void user_tcp_recon_cb(void *arg, sint8 err);
LOCAL void user_tcp_connect_cb(void *arg);
LOCAL void user_send_data();
LOCAL void user_tcp_discon_cb(void *arg);
LOCAL void user_tcp_sent_cb(void *arg);
LOCAL void user_tcp_recv_cb(void *arg, char *pusrdata, unsigned short length);
LOCAL void GetChipID(uint8_t *buffer);
LOCAL bool save_data(char *precv, uint16 length);
LOCAL bool check_data(char *precv, uint16 length);
*/

/*
LOCAL void ICACHE_FLASH_ATTR
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
*/

LOCAL bool ICACHE_FLASH_ATTR
check_data(char *precv, uint16 length)
{
    char length_buf[10] = {0};
    char *ptemp = NULL;
    char *pdata = NULL;
    char *tmp_precvbuffer;
    uint16 tmp_length = length;
    uint32 tmp_totallength = 0;
    
    ptemp = (char *)strstr(precv, "\r\n\r\n");
	
	printf("I am in check_data\n");
    
    if (ptemp != NULL) {
        tmp_length -= ptemp - precv;
        tmp_length -= 4;
        tmp_totallength += tmp_length;
        
        pdata = (char *)strstr(precv, "Content-Length: ");
        
        if (pdata != NULL) {
            pdata += 16;
            tmp_precvbuffer = (char *)strstr(pdata, "\r\n");
            
            if (tmp_precvbuffer != NULL)
			{
                memcpy(length_buf, pdata, tmp_precvbuffer - pdata);
                dat_sumlength = atoi(length_buf);
                printf("A_dat:%u,tot:%u,lenght:%u\n",dat_sumlength,tmp_totallength,tmp_length);
                if(dat_sumlength != tmp_totallength){
                    return false;
                }
            }
        }
    }
    return true;
}


LOCAL bool ICACHE_FLASH_ATTR
save_data(char *precv, uint16 length)
{
    //bool flag = false;
    //char length_buf[10] = {0};
    char *pstart = NULL;
    char *pfinish = NULL;
    uint16 headlength = 0;
    static uint32 totallength = 0;
	char *precvbuffer = NULL;
	char *recvbuffer = NULL;
	
	char *name_param = NULL;
	char *value_param = NULL;
	
	uint16_t min_temp_var;
	uint16_t max_temp_var;

	//printf("I am in save_data\n");

    pstart = (char *)strstr(precv, "\r\n\r\n");

    if (pstart != NULL)
	{
        length -= pstart - precv;
        length -= 4;
        totallength += length;
        headlength = pstart - precv + 4;
		//printf("length = %d, totallength = %d, headlength = %d\n", length, totallength, headlength);
		
		precvbuffer = (char *)zalloc(totallength+1);
		//printf("zalloc\n");
		
		memcpy(precvbuffer, &precv[headlength], totallength);
		//printf("precvbuffer : %s\n", precvbuffer);
		
		pstart = (char *)strstr(precvbuffer, "\r\n");
		pfinish = (char *)strstr(precvbuffer, "}");
		
		//printf("pfinish:%d\n", pfinish);
	    if (pstart != NULL)
		{
			recvbuffer = (char *)zalloc(pfinish - pstart);
			
			totallength = pstart - precvbuffer + 3;
			
			memcpy(recvbuffer, &precvbuffer[totallength], pfinish - pstart - 2);
			//printf("recvbuffer:%s\n", recvbuffer);
			
			if (precvbuffer != NULL) {
				//printf("free(precvbuffer)\n");
				free(precvbuffer);
				//printf("free(precvbuffer) finished\n");
				precvbuffer = NULL;
			}
			precvbuffer = recvbuffer + (pfinish - pstart - 2);
			
			pstart = recvbuffer;
			
			do {
				pstart = (char *)strstr(pstart, "'");
				pfinish = (char *)strstr(pstart+1, "':'");
				
				name_param = (char *)zalloc(pfinish - pstart);
				memcpy(name_param, pstart+1, pfinish - pstart - 1);
				
				pstart = pfinish + 3;
				pfinish = (char *)strstr(pstart, "'");
				
				value_param = (char *)zalloc(pfinish - pstart + 1);
				memcpy(value_param, pstart, pfinish - pstart);
				
				//printf("name_param = %s\n", name_param);
				//printf("value_param = %s\n", value_param);
				
				//printf("pfinish = %d, precvbuffer = %d\n", pfinish, precvbuffer);
				if (strcmp(name_param, "Temp_min") == 0 )
				{
					min_temp_var = (uint16_t) (atoi(value_param));
					if (min_temp_var != min_temp)
					{
						min_temp = min_temp_var;
					}
				}
				else if (strcmp(name_param, "Temp_max") == 0 )
				{					
					max_temp_var = (uint16_t) (atoi(value_param));
					if (max_temp_var != max_temp)
					{
						max_temp = max_temp_var;
					}
				}

				if (name_param != NULL)
				{
					//printf("free(name_param)\n");
					free(name_param);
					//printf("free(name_param) finished\n");
					name_param = NULL;
				}
			
				if (value_param != NULL)
				{
					//printf("free(value_param)\n");
					free(value_param);
					//printf("free(value_param) finished\n");
					value_param = NULL;
				}
				
				pstart = pfinish + 2;
				
			} while (pfinish+2 < precvbuffer);
			
			precvbuffer = NULL;
			
			
			if (name_param != NULL) {
				//printf("free(name_param)\n");
				free(name_param);
				//printf("free(name_param) finished\n");
				name_param = NULL;
			}
		
			if (value_param != NULL) {
				//printf("free(value_param)\n");
				free(value_param);
				//printf("free(value_param) finished\n");
				value_param = NULL;
			}
			
			if (recvbuffer != NULL) {
				//printf("free(recvbuffer)\n");
				free(recvbuffer);
				//printf("free(recvbuffer) finished\n");
				recvbuffer = NULL;
			}
		}

		if (precvbuffer != NULL) {
			//printf("free(precvbuffer)\n");
			free(precvbuffer);
			//printf("free(precvbuffer) finished\n");
			precvbuffer = NULL;
		}
		
		
    }
}


LOCAL sint8 ICACHE_FLASH_ATTR
user_send_data()
{
	sint8 ret = 10;
	uint8_t rom_buffer[13];
	uint8_t buffer_byte[6];
	uint8_t chip_id[9];
	uint16_t decimal = GetTemperature();
	
	GetID_DS18B20(buffer_byte);
	
	uint8_t i=0;
	do {
		if ((0xff & buffer_byte[i]) > 15) sprintf(&rom_buffer[i*2], "%X", (0xff & buffer_byte[i])); else sprintf(&rom_buffer[i*2], "0%X", (0xff & buffer_byte[i]));
		i++;
	} while (i < 6);
	rom_buffer[13] = 0;
	
	GetChipID(buffer_byte);
	
	i=0;
	do {
		if ((0xff & buffer_byte[i]) > 15) sprintf(&chip_id[i*2], "%X", (0xff & buffer_byte[i])); else sprintf(&chip_id[i*2], "0%X", (0xff & buffer_byte[i]));
		i++;
	} while (i < 4);
	chip_id[9] = 0;
	
	//sprintf(&rom_buffer[12], "\n");
	//printf("%s\n", rom_buffer);


	//decimal = buffer[1];
	//decimal = decimal<<8;
	//decimal |= buffer[0];
	//decimal = decimal>>2;
	
	//temp_from_ds18b20 = decimal;
	//printf("decimal = %X\n", decimal);
	//printf("decimal = %d\n", decimal);
    //printf("%d.%d C\n", (decimal>>2), (decimal&0x03)*25);
	
	//printf("user_send_data: %d.%d C\n", (decimal>>2), (decimal&0x03)*25);

	if (&user_tcp_conn)
	{
		char *pbuf = (char *)zalloc(packet_size);
		content = (char *) malloc(512);
		
		sprintf(content, "{\"ChipID\":\"%s\",\"ID\":\"%s\",\"temperature\":\"%d.%d\"}", chip_id, rom_buffer, (decimal>>2), (decimal&0x03)*25);
		
		sprintf(pbuf, pheadbuffer, NET_DOMAIN, strlen(content), content);
		ret = espconn_send(&user_tcp_conn, pbuf, strlen(pbuf));
		free(pbuf);
		free(content);
		
		printf("send_data, ret code: %d\n", ret);
	}

	
	return ret;
}

/******************************************************************************
 * FunctionName : user_tcp_recv_cb
 * Description  : receive callback.
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_tcp_recv_cb(void *arg, char *pusrdata, unsigned short length)
{
	bool parse_flag = false;
   //received some data from tcp connection
/*
#ifdef DEBUG
    printf("Received data string\n");
	printf("len:%u\n",length);
#endif 
*/
	save_data(pusrdata, length);
	/*
	if(check_data(pusrdata, length) == false)
	{
		printf("goto\n");
	}
	else {
		parse_flag = save_data(pusrdata, length);
	}
	*/
}
/******************************************************************************
 * FunctionName : user_tcp_sent_cb
 * Description  : data sent callback.
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_tcp_sent_cb(void *arg)
{
	//data sent successfully
    printf("Sent callback: data sent successfully.\n");
}
/******************************************************************************
 * FunctionName : user_tcp_discon_cb
 * Description  : disconnect callback.
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_tcp_discon_cb(void *arg)
{
	//tcp disconnect successfully
    //printf("Disconnected from server.\r\n");
	//pespconn_main = NULL;
#ifdef DEBUG
    printf("Disconnected from server...\n");
#endif 
}

/******************************************************************************
 * FunctionName : user_tcp_connect_cb
 * Description  : A new incoming tcp connection has been connected.
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_tcp_connect_cb(void *arg)
{
    //pespconn_main = arg;
 
#ifdef DEBUG
    printf("Connected to server...\n");
#endif

	//struct	espconn	*pesp_conn	=	arg;
	printf("user_tcp_conn.state = %d\n", user_tcp_conn.state);
	
	user_send_data();
	
/*
    espconn_regist_recvcb(pespconn_main, user_tcp_recv_cb);
    espconn_regist_sentcb(pespconn_main, user_tcp_sent_cb);
    espconn_regist_disconcb(pespconn_main, user_tcp_discon_cb);
*/

/*
	if (pespconn_main)
	{
		user_send_data();
	}
	*/
}
  
/******************************************************************************
 * FunctionName : user_tcp_recon_cb
 * Description  : reconnect callback, error occured in TCP connection.
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_tcp_recon_cb(void *arg, sint8 err)
{
   //error occured , tcp connection broke. user can try to reconnect here. 
     
    printf("Reconnect callback called, error code: %d !!!\n",err);

	/*
	// Set timer to check whether router allotted an IP 
	os_timer_disarm(&test_timer);
	os_timer_setfn(&test_timer, (os_timer_func_t *)user_check_ip, NULL);
	os_timer_arm(&test_timer, 1000, 0);
	*/
}

  
#ifdef DNS_ENABLE
/******************************************************************************
 * FunctionName : user_dns_found
 * Description  : dns found callback
 * Parameters   : name -- pointer to the name that was looked up.
 *                ipaddr -- pointer to an ip_addr_t containing the IP address of
 *                the hostname, or NULL if the name could not be found (or on any
 *                other error).
 *                callback_arg -- a user-specified callback argument passed to
 *                dns_gethostbyname
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_dns_found(const char *name, ip_addr_t *ipaddr, void *arg)
{
    struct espconn *pespconn = (struct espconn *)arg;
	
#ifdef DEBUG
	printf("user_dns_found\n");
#endif
  
    if (ipaddr == NULL) 
    {
#ifdef DEBUG
        printf("user_dns_found NULL \r\n");
#endif
        return;
    }
 
#ifdef DEBUG
   //dns got ip
    printf("user_dns_found %d.%d.%d.%d \r\n",
            *((uint8 *)&ipaddr->addr), *((uint8 *)&ipaddr->addr + 1),
            *((uint8 *)&ipaddr->addr + 2), *((uint8 *)&ipaddr->addr + 3));
#endif 
  
    if (tcp_server_ip.addr == 0 && ipaddr->addr != 0) 
    {
      // dns succeed, create tcp connection
        //os_timer_disarm(&test_timer);
        tcp_server_ip.addr = ipaddr->addr;
        memcpy(pespconn->proto.tcp->remote_ip, &ipaddr->addr, 4); // remote ip of tcp server which get by dns
  
        pespconn->proto.tcp->remote_port = 80; // remote port of tcp server
        
        pespconn->proto.tcp->local_port = espconn_port(); //local port of ESP8266
  
        espconn_regist_connectcb(pespconn, user_tcp_connect_cb); // register connect callback
        espconn_regist_reconcb(pespconn, user_tcp_recon_cb); // register reconnect callback as error handler
  
        espconn_connect(pespconn); // tcp connect
    }
}

  
/******************************************************************************
 * FunctionName : user_esp_platform_dns_check_cb
 * Description  : 1s time callback to check dns found
 * Parameters   : arg -- Additional argument to pass to the callback function
 * Returns      : none
*******************************************************************************/
LOCAL void ICACHE_FLASH_ATTR
user_dns_check_cb(void *arg)
{
    struct espconn *pespconn = arg;
  
    espconn_gethostbyname(pespconn, NET_DOMAIN, &tcp_server_ip, user_dns_found); // recall DNS function

}
#endif
/******************************************************************************
 * FunctionName : user_check_ip
 * Description  : check whether get ip addr or not
 * Parameters   : none
 * Returns      : none
*******************************************************************************/
void ICACHE_FLASH_ATTR
user_check_ip(void)
{
    struct ip_info ipconfig;
	
	printf("I in user_check_ip\n");
	
 
   //disarm timer first
    //os_timer_disarm(&test_timer);
  
   //get ip info of ESP8266 station
    wifi_get_ip_info(STATION_IF, &ipconfig);
  
    if (wifi_station_get_connect_status() == STATION_GOT_IP && ipconfig.ip.addr != 0) 
    {
      printf("Connected to router and assigned IP!\r\n");
  
      // Connect to tcp server as NET_DOMAIN
      user_tcp_conn.proto.tcp = &user_tcp;
      user_tcp_conn.type = ESPCONN_TCP;
      user_tcp_conn.state = ESPCONN_NONE;
        
#ifdef DNS_ENABLE
       tcp_server_ip.addr = 0;
       espconn_gethostbyname(&user_tcp_conn, NET_DOMAIN, &tcp_server_ip, user_dns_found); // DNS function
  
       //os_timer_setfn(&test_timer, (os_timer_func_t *)user_dns_check_cb, &user_tcp_conn);
       //os_timer_arm(&test_timer, 1000, 0);
#else
       //const char esp_tcp_server_ip[4] = {109, 251, 200, 186}; // remote IP of TCP server
	   const char esp_tcp_server_ip[4] = tcp_server_ip.addr; // remote IP of TCP server
  
       memcpy(user_tcp_conn.proto.tcp->remote_ip, esp_tcp_server_ip, 4);
  
       user_tcp_conn.proto.tcp->remote_port = 80;  // remote port
        
       user_tcp_conn.proto.tcp->local_port = espconn_port(); //local port of ESP8266
  
       espconn_regist_connectcb(&user_tcp_conn, user_tcp_connect_cb); // register connect callback
       espconn_regist_reconcb(&user_tcp_conn, user_tcp_recon_cb); // register reconnect callback as error handler
       espconn_connect(&user_tcp_conn); 
  
#endif
    } 
   else
    {
       
        if ((wifi_station_get_connect_status() == STATION_WRONG_PASSWORD ||
                wifi_station_get_connect_status() == STATION_NO_AP_FOUND ||
                wifi_station_get_connect_status() == STATION_CONNECT_FAIL)) 
        {
			printf("Connection to router failed!\r\n");
        } 
        else
        {
           //re-arm timer to check ip
            //os_timer_setfn(&test_timer, (os_timer_func_t *)user_check_ip, NULL);
            //os_timer_arm(&test_timer, 1000, 0);
        }
		
    }
}

static void ICACHE_FLASH_ATTR web_client_thread(void* pvParameters)
{
	int rc = 0, count = 0;
	char *content = (char *)malloc(512);
	uint16_t decimal;
	struct ip_info ipconfig;
	sint8 ret = 100;

#ifdef DEBUG
    printf("web client thread starts\n");
#endif
	
	xTask_webclient = 1;

	while (++count) {
#ifdef DEBUG
        printf("WEB thread, count number is %d\n", count);
		printf("user_tcp_conn.state = %d\n", user_tcp_conn.state);
#endif

		if (user_tcp_conn.state == ESPCONN_CLOSE)
		{
			printf("*** espconn_disconnect ***\n");
			ret = espconn_disconnect(&user_tcp_conn);
			printf("espconn_disconnect, ret code: %d\n", ret);
		}
				

	   //get ip info of ESP8266 station
		wifi_get_ip_info(STATION_IF, &ipconfig);
	  
		if (wifi_station_get_connect_status() == STATION_GOT_IP && ipconfig.ip.addr != 0 && (user_tcp_conn.state == ESPCONN_NONE || user_tcp_conn.state == ESPCONN_CLOSE)
		/*&& !&user_tcp_conn*/
		) 
		{
#ifdef DEBUG
		  printf("Connected to router and assigned IP!\n");
#endif
	  
		  // Connect to tcp server as NET_DOMAIN
		  user_tcp_conn.proto.tcp = &user_tcp;
		  user_tcp_conn.type = ESPCONN_TCP;
		  user_tcp_conn.state = ESPCONN_NONE;
        
#ifdef DNS_ENABLE
		   tcp_server_ip.addr = 0;
		   espconn_gethostbyname(&user_tcp_conn, NET_DOMAIN, &tcp_server_ip, user_dns_found); // DNS function
#else
		   const char esp_tcp_server_ip[4] = tcp_server_ip.addr; // remote IP of TCP server
	  
		   memcpy(user_tcp_conn.proto.tcp->remote_ip, esp_tcp_server_ip, 4);
	  
		   user_tcp_conn.proto.tcp->remote_port = 80;  // remote port
			
		   user_tcp_conn.proto.tcp->local_port = espconn_port(); //local port of ESP8266
	  
		   espconn_regist_connectcb(&user_tcp_conn, user_tcp_connect_cb); // register connect callback
		   espconn_regist_reconcb(&user_tcp_conn, user_tcp_recon_cb); // register reconnect callback as error handler
		   
		   espconn_regist_recvcb(&user_tcp_conn, user_tcp_recv_cb); // Register data receive function which will be called back when data is received.
		   espconn_regist_sentcb(&user_tcp_conn, user_tcp_sent_cb); // Register data sent function which will be called back when data are successfully sent.
		   espconn_regist_disconcb(&user_tcp_conn, user_tcp_discon_cb); // Register disconnection function which will be called back under successful TCP disconnection.

		   espconn_connect(&user_tcp_conn);

#endif
		}
		else
		{
			if (user_tcp_conn.state == ESPCONN_CONNECT)
			{
				if (user_send_data() != 0)
				{
					printf("user_tcp_conn.state = %d\n", user_tcp_conn.state);
					
					printf("*** espconn_disconnect ***\n");
					ret = espconn_disconnect(&user_tcp_conn);
					printf("espconn_disconnect, ret code: %d\n", ret);
					printf("user_tcp_conn.state = %d\n", user_tcp_conn.state);
					
					//ret = espconn_delete(&user_tcp_conn);
					//printf("espconn_delete, ret code: %d\n", ret);
					
			
					//free(pespconn_main);
					//pespconn_main = NULL;
					//pespconn_main ={};

					//memset(&pespconn_main, 0, sizeof(pespconn_main));
					count = -2;
				}
			}
			else
			{
#ifdef DEBUG
			  printf("user_tcp_conn.state != ESPCONN_CONNECT\n");
#endif
			}
		}

        vTaskDelay(20000 / portTICK_RATE_MS);  //send every 20 seconds
    }
	xTask_webclient = 0;

#ifdef DEBUG
    printf("WebClient_thread going to be deleted\n");
#endif
    vTaskDelete(NULL);
	free(content);
	xTask_webclient = 0;
    return;
}


void ICACHE_FLASH_ATTR
user_webclient_init()
{
	//chip_id = system_get_chip_id();

	int ret;
	/*
	pHTTPServer = (struct espconn *)os_zalloc(sizeof(struct espconn));
	memset( pHTTPServer, , sizeof( struct espconn ) );

*/
	
    ret = xTaskCreate(web_client_thread,
                      WEB_CLIENT_THREAD_NAME,
                      WEB_CLIENT_THREAD_STACK_WORDS,
                      NULL,
                      WEB_CLIENT_THREAD_PRIO,
                      &web_client_handle);

    if (ret != pdPASS)  {
        printf("WebClient create client thread %s failed\n", WEB_CLIENT_THREAD_NAME);
    }

/*	
	// Set timer to check whether router allotted an IP 
	os_timer_disarm(&test_timer);
	os_timer_setfn(&test_timer, (os_timer_func_t *)user_check_ip, NULL);
	os_timer_arm(&test_timer, 1000, 0);
*/
}
