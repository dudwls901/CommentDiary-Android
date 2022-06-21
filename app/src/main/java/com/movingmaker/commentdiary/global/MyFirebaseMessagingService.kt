package com.movingmaker.commentdiary.global

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.view.main.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    //    var baseToken = BaseToken()
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


//        Log.d(TAG, "onMessageReceived: $remoteMessage")

        // Data 항목이 있을때.
        // background 처리시
//        if (remoteMessage.data.isNotEmpty()) {
//            val data = remoteMessage.data
//            val messageTitle = data["title"]
//            val messageBody = data["body"]
//            Log.d("fcm_response", "data 알림 메시지: $messageTitle $messageBody")
//            sendMessage("Adfadfa", "DAfdafad", "background")
//        }

        if (remoteMessage.notification != null) {
            val messageBody = remoteMessage.notification!!.body
            val messageTitle = remoteMessage.notification!!.title
            Log.d("fcm_response", "noti 알림 메시지: $messageTitle $messageBody" )
            sendMessage(messageTitle!!, messageBody!!,"foreground")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendMessage(messageTitle: String, messageBody: String, state: String) {
//        val intent = Intent(this, SplashActivity::class.java)
//        intent.putExtra("title", messageTitle)
//        intent.putExtra("body", messageBody)
//        intent.putExtra("alarm", true)
//        baseToken.setAlarm(this, true)

        Log.d(TAG, "sendMessage:push  $state")
        //백그라운드일 때
//        if(state=="background"){
//            intent = Intent(this, SplashActivity::class.java)
//        }
        //포그라운드일 때
//        if(state=="foreground"){
//            Log.d(TAG, "sendMessage: push $yesterDay") //
//        }

        val intent = Intent(this, MainActivity::class.java)
        val yesterDay = DateConverter.getCodaToday().minusDays(1)
        intent.putExtra("pushDate",DateConverter.ymdFormat(yesterDay))
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
            }
        val channelId = "Alarm"

        val defaultSoundUri: Uri =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL)
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

// private const val CHANNEL_ID = "my_channel"
//
//class FirebaseService : FirebaseMessagingService() {
//
//    companion object {
//        var sharedPref: SharedPreferences? = null
//
//        var token: String?
//        get() {
//            return sharedPref?.getString("token", "")
//        }
//        set(value) {
//            sharedPref?.edit()?.putString("token", value)?.apply()
//        }
//    }
//
//    // 사용자 디바이스 토큰 새로 생성
//    override fun onNewToken(newToken: String) {
//        super.onNewToken(newToken)
//        token = newToken
//    }
//
//    //알람이 온 경우 --> 어떻게 보이게 할 것인지, 누르면 어디로 이동하게 할 것인지 정하는 메소드
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        //알림 팝업 누를 시 MainActivity로 이동
//        val intent = Intent(this, MainActivity::class.java)
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationID = Random.nextInt()
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(notificationManager)
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(message.data["title"])
//            .setContentText(message.data["message"])
//            .setSmallIcon(R.drawable.ic_cat_smile)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        notificationManager.notify(notificationID, notification)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(notificationManager: NotificationManager) {
//        val channelName = "channelName"
//        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
//            description = "My channel description"
//            enableLights(true)
//            lightColor = Color.GREEN
//        }
//        notificationManager.createNotificationChannel(channel)
//    }
//}