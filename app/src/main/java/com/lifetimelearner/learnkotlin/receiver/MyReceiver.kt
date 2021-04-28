package com.lifetimelearner.learnkotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.lifetimelearner.learnkotlin.service.MyService

class MyReceiver : BroadcastReceiver() {

    private val channelId = "com.lifetimelearner.learnkotlin.basic"
    private val channelName = "Basic Notifications"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val serviceIntent = Intent(context, MyService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)

        /*
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(2000,10))
        // VibrationEffect.createWaveform(longArrayOf(100,200,300,400,500),0)

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val intent = Intent(context,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(context,1001,intent,0)
        val notification = NotificationCompat.Builder(context,channelId)
            .setContentTitle("Alarm Clock")
            .setContentText(Date(System.currentTimeMillis()).toString())
            .setSmallIcon(android.R.drawable.sym_contact_card)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        // manager.notify(NOTIFICATION_ID,notification)
         */

    }

    companion object {
        const val NOTIFICATION_ID = 1000
    }

}