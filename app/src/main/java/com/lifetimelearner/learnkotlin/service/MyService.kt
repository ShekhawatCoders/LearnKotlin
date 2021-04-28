package com.lifetimelearner.learnkotlin.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lifetimelearner.learnkotlin.ui.MainActivity
import java.util.*

class MyService : Service() {

    private val channelId = "com.lifetimelearner.learnkotlin.basic"
    private val channelName = "Basic Notifications"
    private val notificationId = 102

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if(intent?.action.equals("com.lifetimelearner.learnkotlin.CloseService")) {
            stopForeground(true)
            stopSelf()
        }

        val notification = createNotification()
        startForeground(notificationId, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // TODO("Not yet implemented")
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() : Notification {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this,101,intent,0)
        val prevIntent = Intent(this, MyService::class.java)
        val pendingPrevIntent = PendingIntent.getService(this,102,prevIntent,0)
        val playPauseIntent = Intent(this, MyService::class.java)
        val pendingPlayPauseIntent = PendingIntent.getService(this,103,playPauseIntent,0)
        val nextIntent = Intent(this, MyService::class.java)
        val pendingNextIntent = PendingIntent.getService(this,104,nextIntent,0)
        val closeIntent = Intent(this, MyService::class.java)
        closeIntent.setAction("com.lifetimelearner.learnkotlin.CloseService")
        val pendingCloseIntent = PendingIntent.getService(this,105,closeIntent,0)
        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle("Service Running")
            .setContentText(Date(System.currentTimeMillis()).toString())
            .setSmallIcon(android.R.drawable.sym_contact_card)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_media_previous,"Previous Song",pendingPrevIntent)
            .addAction(android.R.drawable.ic_media_play,"Play and Pause Song",pendingPlayPauseIntent)
            //.addAction(android.R.drawable.ic_media_next,"Next Song",pendingNextIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Close Service",pendingCloseIntent)
            .setAutoCancel(true)
            .build()
        // manager.notify(notificationId,notification)
        return notification
    }


}