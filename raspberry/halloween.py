import blinkt
from blinkt import set_brightness, set_pixel, show, clear
import json
import random
import time
import paho.mqtt.client as mqtt

blinkt.set_clear_on_exit()

colors = [
    [255, 0,   0], # red
    [255, 194, 0], # amber
    [255, 165, 0]  # orange
]

interval = 0.05

def fire(total_time):
    elapsed_time = 0
    while elapsed_time <= total_time:
        brightness = random.uniform(0, 0.2)
        color      = random.choice(colors)
        pixel      = random.randint(0, 7)

        r = color[0]
        g = color[1]
        b = color[2]

        set_pixel(pixel, r, g, b, brightness)
        show()

        time.sleep(interval)
        elapsed_time += interval

    clear()
    show()

def on_connect(client, userdata, flags, rc):
    print("Connected")
    client.subscribe("halloween/#")

def on_message(client, userdata, msg):
    print("Topic: " + msg.topic + " -> " + str(msg.payload))
    if (msg.topic == "halloween"):
       print("Pumpkin on fire")
       payload = json.loads(str(msg.payload))
       fire(payload["time"])

# Connecting to MQTT Client

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("localhost", 1883, 60)

client.loop_forever()

