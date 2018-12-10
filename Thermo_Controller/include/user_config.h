/*
 * ESPRESSIF MIT License
 *
 * Copyright (c) 2017 <ESPRESSIF SYSTEMS (SHANGHAI) PTE LTD>
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

#ifndef __USER_CONFIG_H__
#define __USER_CONFIG_H__

#define SPI_FLASH_SEC_SIZE      4096

#define SSID         "livefood"        /* Wi-Fi SSID */
#define PASSWORD     ""     /* Wi-Fi Password */


//#define SSID         "TP-LINK_32819E"        /* Wi-Fi SSID */
//#define PASSWORD     ""     /* Wi-Fi Password */

//#define SSID         "Lenovo90"        /* Wi-Fi SSID */
//#define PASSWORD     "1234567890"     /* Wi-Fi Password */


/*
#define MQTT_BROKER  "m12.cloudmqtt.com"  // MQTT Broker Address
#define MQTT_PORT    17958             // MQTT Port
#define MQTT_USER	"Zhkdkmrz"			// Логи от сервер
#define MQTT_PASS	"f8cR9iIH5_OV"				// Пароль от сервера
*/

#define MQTT_BROKER  "broker.hivemq.com"  // MQTT Broker Address
#define MQTT_PORT    1883             // MQTT Port
#define MQTT_USER	"Zhkdkmrz"			// Логи от сервер
#define MQTT_PASS	"f8cR9iIH5_OV"				// Пароль от сервера


#define GPIO_INPUT_GET(gpio_no)     ((gpio_input_get()>>gpio_no)&BIT(0))

#endif

