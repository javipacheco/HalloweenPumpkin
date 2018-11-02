package com.mqtthalloween.models

import akme.toDeferred
import arrow.effects.DeferredK

sealed class Command {

    data class MqttConnectCommand(val server: String) : Command()

    object MqttDisconnectCommand : Command()

    data class MqttPublishCommand(val topic: String, val payload: String) : Command()

    object MqttServerIsConnectCommand : Command()

}

fun Command.toDeferred(): DeferredK<Unit> = "${this.javaClass.simpleName} command doesn't exist".toDeferred()