# Halloween Pumpkin

[Post in Spanish](https://medium.com/@javipacheco/mi-calabaza-de-halloween-que-usa-raspberry-pi-y-android-d1195366bf76)

Inspired on [this project](https://www.raspberrypi.org/blog/halloween-pumpkin-light-effect-tutorial/)

<img src="https://github.com/javipacheco/HalloweenPumpkin/blob/master/halloween.jpg" width="200"/>

This small project uses MQTT to communicate an Android application with a Raspberry PI, which has 8 LEDs that will light up to illuminate a pumpkin. I printed this pumpkin using [this Thingiverse objet](https://www.thingiverse.com/thing:3135934)

## Prerequisites

* Raspberry must to have installed **mosquitto**, a broker widely used in IoT and Raspberry project
* I used [Blinkt](https://shop.pimoroni.com/products/blinkt), a Pimoroni HAT with 8 LEDs very easy to use thanks to its Python library.

## Android App

<img src="https://github.com/javipacheco/HalloweenPumpkin/blob/master/halloween-android.png" width="200"/>

This application connects to the MQTT server installed on the Raspberry PI and sends a *JSON* with the time it wants the pumpkin to be turned on to the topic **halloween**

You should know the IP of the Raspberry PI *(for example: "tpc://192.168.2.48:1883")*. *1883* is the default port used by MQTT

You can use the source code of the application or [install it directly from the APK](https://github.com/javipacheco/HalloweenPumpkin/releases/tag/v1)

## Python App on Raspberry PI

The Python application subscribes to the topic **halloween** waiting for a message from the Android application. When the message arrives it will extract the JSON time and turn on the pumpkin

To launch the app simply copy the Python file into your Raspberry and launch it

    python halloween.py

# License

    Copyright (C) 2018 Javi Pacheco

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
