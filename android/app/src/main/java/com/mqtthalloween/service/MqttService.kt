package com.mqtthalloween.service

import akme.AkmeException
import akme.logD
import akme.logE
import akme.orDeferred
import android.content.Context
import arrow.effects.DeferredK
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.IllegalStateException

interface MqttService {

    var client: MqttAndroidClient?

    fun getContext(): Context

    fun connect(serverUri: String): DeferredK<Unit> =
        AkmeException.MqttException("MQTT Client not connected").orDeferred {
            val clientId = MqttClient.generateClientId()

            client = MqttAndroidClient(getContext(), serverUri, clientId)

            client?.let {
                val token = it.connect()
                token.actionCallback = object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        "MQTT Client connected".logD()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        "MQTT Client not connected".logE(exception)

                    }
                }
            }
            Unit
        }

    fun disconnect(): DeferredK<Unit> =
        AkmeException.MqttException("MQTT Client not connected").orDeferred {
            client?.let {
                it.disconnect()
            }
            client = null
            Unit
        }

    fun publish(topic: String, payload: String): DeferredK<Unit> =
        AkmeException.MqttException("MQTT Client not connected").orDeferred {
            client?.let {
                val encodedPayload = payload.toByteArray(charset("UTF-8"))
                val message = MqttMessage(encodedPayload)
                it.publish(topic, message)
                Unit
            } ?: throw IllegalStateException("Client is null")
        }

    fun isConnected(): DeferredK<Boolean> =
        AkmeException.MqttException("MQTT Client not connected").orDeferred {
            client?.isConnected ?: false
        }

}