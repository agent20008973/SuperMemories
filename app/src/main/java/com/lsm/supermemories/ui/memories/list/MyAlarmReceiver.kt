package com.lsm.supermemories.ui.memories.list

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lsm.supermemories.MainActivity
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R

class MyAlarmReceiver: BroadcastReceiver() {
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.lsm.supermemories.ui.memories.list"
    private val CHANNEL_ID = "com.lsm.supermemories.ui.memories.list"
    private val description = "Test notification"

    override fun onReceive(p0: Context?, p1: Intent?) {
        //val requestCode = p1!!.getIntExtra("REQUEST_CODE",-1)
      //  Toast.makeText(p0!!,"Alarm fired with request code"+requestCode,Toast.LENGTH_SHORT).show()
        notificationManager = MainApplication.applicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(MainApplication.applicationContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(MainApplication.applicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(MainApplication.applicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Dawno Ciebie nie było w aplikacji")
                .setContentText("Dodaj nowe super wspomnienie! :)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(MainApplication.applicationContext())) {
            notify(100, builder.build())
        }
        show_notification()

    }
    private fun show_notification(){
        notificationManager = MainApplication.applicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(MainApplication.applicationContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(MainApplication.applicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(MainApplication.applicationContext().packageName, R.layout.fragment_home_page)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(MainApplication.applicationContext(),channelId)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_baseline_add_a_photo_24)
                    .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(MainApplication.applicationContext())
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.ic_baseline_add_a_photo_24)
                    .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())
    }


    }


