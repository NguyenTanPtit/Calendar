package com.example.calendar

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {
    private lateinit var alertDialog : AlertDialog

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {
        val event = intent.getStringExtra("event")
        val time = intent.getStringExtra("time")
        val notiID = intent.getIntExtra("id",0)
        val activityIntent = Intent(context,HomeActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,
        0,activityIntent,PendingIntent. FLAG_IMMUTABLE)

        val chanelID = "chanel_id"
        val name : CharSequence = "Notification Event"
        val des = "description"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(chanelID,name,NotificationManager.IMPORTANCE_HIGH)
            channel.description = des
            val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notify : Notification = NotificationCompat.Builder(context,chanelID).setSmallIcon(R.mipmap.ic_launcher)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle(event)
            .setContentText(time)
            .setDeleteIntent(pendingIntent)
            .setGroup("Cal").build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
          return
        }
        notificationManager.notify(notiID,notify)
    }
}