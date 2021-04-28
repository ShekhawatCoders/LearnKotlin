package com.lifetimelearner.learnkotlin.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.lifetimelearner.learnkotlin.R

class MainActivity : AppCompatActivity() {

    lateinit var btn1 : Button
    lateinit var btn2 : Button
    lateinit var btn3 : Button
    lateinit var btn4 : Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1 = findViewById(R.id.BTN_Main_1)
        btn2 = findViewById(R.id.BTN_Main_2)
        btn3 = findViewById(R.id.BTN_Main_3)
        btn4 = findViewById(R.id.BTN_Main_4)

        btn1.setOnClickListener { goToPhoneStateActivity() }
        btn2.setOnClickListener { goToTextToSpeechActivity() }
        btn3.setOnClickListener { startMyService() }
        btn4.setOnClickListener { goToMediaActivity() }

        /*
        val i = 2
        val intent = Intent(this,MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, RECEIVER_CODE, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + i*3000, pendingIntent)
         */
        /*
        val serviceIntent = Intent(this, MyService::class.java)
        Handler().postDelayed({
            stopService(serviceIntent)
        },8000)
        */

        /*
        val serviceIntent = Intent(this, MyService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        */

    }

    private fun goToMediaActivity() {
        val intent = Intent(this,MediaActivity::class.java)
        startActivity(intent)
    }

    private fun startMyService() {

    }

    private fun goToPhoneStateActivity() {
        val intent = Intent(this,PhoneInfoActivity::class.java)
        startActivity(intent)
    }

    private fun goToTextToSpeechActivity() {
        val intent = Intent(this,TextToSpeechActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val RECEIVER_CODE = 1003
    }
}
