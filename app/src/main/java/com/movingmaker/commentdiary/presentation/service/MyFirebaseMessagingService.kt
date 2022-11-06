package com.movingmaker.commentdiary.presentation.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.presentation.CodaApplication
import com.movingmaker.commentdiary.presentation.util.DateConverter
import com.movingmaker.commentdiary.presentation.view.main.MainActivity
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "Firebase"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //토큰 갱신
        CodaApplication.deviceToken = token
    }

    //애초에 onMessageReceived는 백그라운드일 때는 호출이 안 됨
    //background인 경우 자동으로 서버에서 보낸 noti의 title,body를 자동으로 푸시 알람을 만들어줌
    //이 데이터를 액티비티에서 가져다 쓰려면 intent?.extras !=null 로 확인
    //onMessageReceived : 받은 메시지에서 title과 body를 추출
    @SuppressLint("InvalidWakeLockTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 화면 깨우기
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(FLAG_KEEP_SCREEN_ON, TAG)
        wakeLock.acquire(3000)

        if (remoteMessage.notification != null) {
            val messageBody = remoteMessage.notification!!.body
            val messageTitle = remoteMessage.notification!!.title
            Timber.d("noti 알림 메시지: $messageTitle $messageBody")
            sendMessage(messageTitle!!, messageBody!!, "foreground")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendMessage(messageTitle: String, messageBody: String, state: String) {

        val intent = Intent(this, MainActivity::class.java)
        val yesterDay = DateConverter.getCodaToday().minusDays(1)
        intent.putExtra("pushDate", DateConverter.ymdFormat(yesterDay))
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            }
        val channelId = "Alarm"

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelName = "Comment"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }
}