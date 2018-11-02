package com.mqtthalloween.ui.main

import akme.*
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import arrow.effects.unsafeRunAsync
import com.mqtthalloween.R
import com.mqtthalloween.models.Command
import com.mqtthalloween.service.MqttService
import com.mqtthalloween.viewmodels.MainViewModel
import com.mqtthalloween.viewmodels.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity(), MqttService {

    override fun getContext(): Context = applicationContext

    override var client: MqttAndroidClient? = null

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(
            this,
            MainViewModelFactory(this)
        ).get(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeModel()

        initUi()

    }

    private fun observeModel() {
        mainViewModel.isConnected.observe(this, Observer {
            it?.let {
                connected(it)
            }
        })
    }


    private fun reviewConnection() {
        Timer("Connection", false).schedule(1000L) {
            mainViewModel.send(Command.MqttServerIsConnectCommand).unsafeRunAsyncWithLog()
        }
    }

    // UI

    private fun initUi() {
        main_connect.setOnClickListener { _ ->
            val isConnected = mainViewModel.isConnected.getValue() ?: false
            if (isConnected) {
                mainViewModel.send(Command.MqttDisconnectCommand).unsafeRunAsync {
                    reviewConnection()
                }
            } else {
                val server = if (main_input_server.text.isEmpty()) testServer else
                    main_input_server.text.toString()
                mainViewModel.send(Command.MqttConnectCommand(server)).unsafeRunAsync {
                    reviewConnection()
                }
            }
        }

        main_publish.setOnClickListener {
            mainViewModel.send(
                Command.MqttPublishCommand(
                    "halloween",
                    "{ \"time\": ${main_time.progress + 5} }"
                )
            ).unsafeRunAsyncWithLog()
        }

        main_publish.isEnabled = false

        getServer()?.let {
            main_input_server.setText(it)
        }

        main_time_title.text = getString(R.string.time, "5")

        main_time.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                main_time_title.text = getString(R.string.time, (progress + 5).toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }

    private fun connected(connected: Boolean) {
        if (connected) setServer(main_input_server.text.toString())
        main_connected.setText(
            if (connected) R.string.connected else R.string.not_connected
        )
        main_connected_card.strokeColor =
                resources.getCompatColor(if (connected) R.color.connected else R.color.disconnected)
        main_connect.backgroundTintList = ColorStateList.valueOf(
            resources.getCompatColor(if (connected) R.color.disconnected else R.color.connected)
        )
        main_connect.setText(if (connected) R.string.disconnect else R.string.connect)
        main_publish.isEnabled = connected
    }

    companion object {
        private const val testServer = "tcp://broker.hivemq.com:1883"
    }

}
