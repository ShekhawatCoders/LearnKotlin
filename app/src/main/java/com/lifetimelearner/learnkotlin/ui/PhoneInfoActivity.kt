package com.lifetimelearner.learnkotlin.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lifetimelearner.learnkotlin.R
import java.util.*

class PhoneInfoActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 107
    }

    lateinit var tts : TextToSpeech

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_info)

        if(!checkPermission())
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE),REQUEST_CODE)

        tts  = TextToSpeech(applicationContext) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(1.0f)
            }
        }

        // tts.voice = Voice("Man",Locale.US,Voice.QUALITY_VERY_HIGH,Voice.LATENCY_NORMAL,)
        // tts.speak("Hello, Vishal Singh",TextToSpeech.QUEUE_FLUSH,null,"1001")

        val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val nm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        showPhoneState(tm)
        showNetworkState(nm)
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }

    private fun showNetworkState(nm: ConnectivityManager) {
        nm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                findViewById<TextView>(R.id.TV_Phone_State).text = "Network Connected"
                tts.speak("Network Connected",TextToSpeech.QUEUE_FLUSH,null,"1001")
            }
            override fun onLost(network: Network) {
                super.onLost(network)
                findViewById<TextView>(R.id.TV_Phone_State).text = "NetWork Disconnected"
                tts.speak("Network Disconnected",TextToSpeech.QUEUE_FLUSH,null,"1001")
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showPhoneState(tm: TelephonyManager) {
        if(!checkPermission()) return
        val data = tm.dataNetworkType
        val list = tm.emergencyNumberList
        // findViewById<TextView>(R.id.TV_Phone_State).text = "$data \n $list"
        tm.listen(object : PhoneStateListener() {
            override fun onDataConnectionStateChanged(state: Int) {
                super.onDataConnectionStateChanged(state)
                var data = ""
                when(state) {
                    TelephonyManager.DATA_CONNECTED -> data = "Connected"
                    TelephonyManager.DATA_DISCONNECTED -> data = "Disconnected"
                }
            }
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                var data = ""
                when(state) {
                    TelephonyManager.CALL_STATE_RINGING -> data = "Call is in ringing mode"
                    TelephonyManager.CALL_STATE_IDLE -> data = "Call is in ideal state mode"
                }
                tts.speak(data,TextToSpeech.QUEUE_FLUSH,null,"1001")
            }
        }, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE or PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun checkPermission() : Boolean {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }
}