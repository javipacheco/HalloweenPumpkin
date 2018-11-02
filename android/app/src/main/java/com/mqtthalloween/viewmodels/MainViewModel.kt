package com.mqtthalloween.viewmodels

import akme.*
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import arrow.effects.DeferredK
import arrow.effects.fix
import arrow.effects.monad
import arrow.typeclasses.binding
import com.mqtthalloween.models.Command
import com.mqtthalloween.models.toDeferred
import com.mqtthalloween.service.MqttService

class MainViewModel(
        private val mqttService: MqttService
): ViewModel() {

    val isConnected = State<Boolean>()

    fun send(command: Command): DeferredK<Unit> = when (command) {
        is Command.MqttConnectCommand ->
            DeferredK.monad().binding {
                mqttService.connect(command.server).bind()
                Unit
            }.fix()
        is Command.MqttDisconnectCommand ->
            DeferredK.monad().binding {
                mqttService.disconnect().bind()
                Unit
            }.fix()
        is Command.MqttServerIsConnectCommand ->
            DeferredK.monad().binding {
                val connected = mqttService.isConnected().bind()
                isConnected.postValue(connected)
                Unit
            }.fix()
        is Command.MqttPublishCommand ->
            DeferredK.monad().binding {
                mqttService.publish(command.topic, command.payload).bind()
                Unit
            }.fix()
        else -> command.toDeferred()
    }

}

class MainViewModelFactory(
    private val mqttService: MqttService) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mqttService) as T
    }
}