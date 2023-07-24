package com.findunone.hydrateme

import Utils.HydrationManager
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {


    private lateinit var btnAddWater: Button
    private lateinit var tvWaterCount: TextView


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "hydration_channel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_REQUEST_CODE = 2108
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        btnAddWater.setOnClickListener {
            HydrationManager.addWaterGlass()
            updateWaterCount()
        }

        createNotificationChannel()
        scheduleNotification()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Hydration Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Stay Hydrated!")
            .setContentText("Don't forget to drink water regularly.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }


    private fun scheduleNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = buildNotification().build()

        // Create a PendingIntent for the notification
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule notification to repeat every 2 hours
        val intervalMillis = 15 * 1000L
        notificationManager.notify(NOTIFICATION_ID, notification)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + intervalMillis,
            intervalMillis,
            pendingIntent
        )
    }


    private fun updateWaterCount() {
        tvWaterCount.text = HydrationManager.getTotalWaterConsumed().toString()
    }

    private fun init(){
        btnAddWater = findViewById(R.id.btnAddWater)
        tvWaterCount = findViewById(R.id.tvWaterCount)

}
}