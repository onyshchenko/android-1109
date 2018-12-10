/*******************************************************************************
 * Copyright (c) 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *    Ian Craggs - initial API and implementation and/or initial documentation
 *******************************************************************************/

#include <stddef.h>

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

#include "mqtt/MQTTClient.h"

#include "user_config.h"

#define MQTT_CLIENT_THREAD_NAME         "mqtt_client_thread"
#define MQTT_CLIENT_THREAD_STACK_WORDS  2048
#define MQTT_CLIENT_THREAD_PRIO         1

LOCAL xTaskHandle mqttc_client_handle;

extern void GetChipID(uint8_t *buffer);
extern uint8 xTask_MQTT;


static void messageArrived_min_temp(MessageData* data)
{
	char *content;
	
	content = (char *) malloc(data->message->payloadlen+1);
	content[data->message->payloadlen] = 0;
	
	memcpy(content, data->message->payload, data->message->payloadlen);

    printf("messageArrived_min_temp: %s\n", content);
	free(content);
}

static void messageArrived_max_temp(MessageData* data)
{
	char *content;
	
	content = (char *) malloc(data->message->payloadlen+1);
	content[data->message->payloadlen] = 0;
	
	memcpy(content, data->message->payload, data->message->payloadlen);

    printf("messageArrived_max_temp: %s\n", content);
	free(content);
}

static void mqtt_client_thread(void* pvParameters)
{
    printf("mqtt client thread starts\n");
    MQTTClient client;
    Network network;
    unsigned char sendbuf[80], readbuf[80] = {0};
    int rc = 0, count = 0;
	uint8_t i=0;
    MQTTPacket_connectData connectData = MQTTPacket_connectData_initializer;

    pvParameters = 0;
    NetworkInit(&network);
	uint8_t buffer_byte[6];
	char chip_id[9];
	char char_topic[50] = {0};
	
	//unsigned char length_buf[80] = {0};
	char *content;
	
    MQTTClientInit(&client, &network, 30000, sendbuf, sizeof(sendbuf), readbuf, sizeof(readbuf));

    char* address = MQTT_BROKER;

    if ((rc = NetworkConnect(&network, address, MQTT_PORT)) != 0) {
        printf("Return code from network connect is %d\n", rc);
    }

#if defined(MQTT_TASK)

    if ((rc = MQTTStartTask(&client)) != pdPASS) {
        printf("Return code from start tasks is %d\n", rc);
    } else {
        printf("Use MQTTStartTask\n");
    }

#endif

	
	GetChipID(buffer_byte);
	
	i=0;
	do {
		if ((0xff & buffer_byte[i]) > 15) sprintf(&chip_id[i*2], "%X", (0xff & buffer_byte[i])); else sprintf(&chip_id[i*2], "0%X", (0xff & buffer_byte[i]));
		i++;
	} while (i < 4);
	chip_id[9] = 0;

    connectData.MQTTVersion = 3;
    //connectData.clientID.cstring = "ESP8266_sample";
	//connectData.username.cstring = MQTT_USER;
	//connectData.password.cstring = MQTT_PASS;

    if ((rc = MQTTConnect(&client, &connectData)) != 0) {
        printf("Return code from MQTT connect is %d\n", rc);
    } else {
        printf("MQTT Connected\n");
    }
	

	sprintf(char_topic, "livefood.in.ua/%s/min", chip_id);
	content = (char *) malloc(strlen(char_topic)+1);
	content[strlen(char_topic)] = 0;
	memcpy(content, char_topic, strlen(char_topic));
	//printf("First topic = %s\n", char_topic);
	
    if ((rc = MQTTSubscribe(&client, "livefood.in.ua/0007BBFD/min" /*content*/, 1, messageArrived_min_temp)) != 0) {
        printf("Return code from MQTT subscribe is %d\n", rc);
    } else {
        printf("MQTT subscribe to topic %s\n", char_topic);
    }
	free(content);

	sprintf(char_topic, "livefood.in.ua/%s/max", chip_id);
	content = (char *) malloc(strlen(char_topic)+1);
	content[strlen(char_topic)] = 0;
	memcpy(content, char_topic, strlen(char_topic));
	//printf("Second topic = %s\n", char_topic);
    if ((rc = MQTTSubscribe(&client, content, 2, messageArrived_max_temp)) != 0) {
        printf("Return code from MQTT subscribe is %d\n", rc);
    } else {
        printf("MQTT subscribe to topic %s\n", char_topic);
    }
	free(content);

	
	MQTTMessage message;
	uint16_t decimal;
	char payload[30];
	
	message.qos = QOS2;
	message.retained = 0;
	
	xTask_MQTT = 1;
	
	
	uint32	value;
	uint8	*addr	=	(uint8	*)&value;
	spi_flash_read(0x7B	* SPI_FLASH_SEC_SIZE,	(uint32	*)addr,	4);
	os_printf("0x7B	sec: %02x%02x%02x%02x\n", addr[0], addr[1], addr[2], addr[3]);
		
    while (++count) {

		decimal = GetTemperature();
		printf("Temperature = %d.%d\n", (decimal>>2), (decimal&0x03)*25);
		printf("decimal = %d\n", decimal);
		
		/*
		value = 0;
		
		addr[0] = decimal;
		addr[2] = decimal;
		
		os_printf("addr: %02x%02x%02x%02x\n", addr[0], addr[1], addr[2], addr[3]);
		spi_flash_write(0x7B * SPI_FLASH_SEC_SIZE, (uint32 *)addr,	4);
*/
		sprintf(payload, "%d.%d", (decimal>>2), (decimal&0x03)*25);
		message.payload = payload;
        message.payloadlen = strlen(payload);

		sprintf(char_topic, "livefood.in.ua/%s/temperature", chip_id);
		//printf("Third topic = %s\n", char_topic);
        if ((rc = MQTTPublish(&client, char_topic, &message)) != 0) {
            printf("Return code from MQTT publish is %d\n", rc);
			
			if (rc == -1)
			{
				break;
			}
        } else {
            //printf("MQTT publish topic \"%s\", message number is %d\n", char_topic, count);
        }

        vTaskDelay(5000 / portTICK_RATE_MS);  //send every 1 seconds
    }

    printf("mqtt_client_thread going to be deleted\n");
	xTask_MQTT = 0;
    vTaskDelete(NULL);
    return;
}

void user_conn_init(void)
{
    int ret;
	//gpio_output_set(0, 0x00002000, 0x00002000, 0); //set ON
	
    ret = xTaskCreate(mqtt_client_thread,
                      MQTT_CLIENT_THREAD_NAME,
                      MQTT_CLIENT_THREAD_STACK_WORDS,
                      NULL,
                      MQTT_CLIENT_THREAD_PRIO,
                      &mqttc_client_handle);

    if (ret != pdPASS)  {
        printf("mqtt create client thread %s failed\n", MQTT_CLIENT_THREAD_NAME);
    }
}
