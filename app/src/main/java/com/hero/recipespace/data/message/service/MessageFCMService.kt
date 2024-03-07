package com.hero.recipespace.data.message.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageFCMService : FirebaseMessagingService() {
    // onNewToken()은 클라우드 서버에 등록되었을 때 호출되고,
    // 파라미터로 전달된 token 이 앱을 구분하기 위한 고유한 키가 됩니다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // FCM 이 아닌 서버간 토큰 연동 작업 실행
    }

    // onMessageReceived()는 클라우드 서버에서 메시지를 전송하면 자동으로 호출되고,
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // 수신된 메세지 처리
        sendNotification(
            message.notification?.title.orEmpty(),
            message.notification?.body.orEmpty(),
        )
    }

    private fun sendNotification(
        title: String,
        messageBody: String,
    ) {
//        val intent = ChatActivity.getIntent(this)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//
//        val channelId = getString(R.string.app_name) // R.string.default_notification_channel_id
//        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ic_camera)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Since android Oreo notification channel is needed.
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel("fcm_default_channel",
//                "fcm_default_channel",
//                NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
