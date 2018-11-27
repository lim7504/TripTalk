package com.example.a210.myapplication;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    private static final String TAG = "FirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // sendNotification(remoteMessage.getData().get("message")); 기존
        sendNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("title"));
    }

    private void sendNotification(String messageBody, String title) {

        try {
            messageBody = URLDecoder.decode(messageBody, "UTF-8");
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        /*
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("sep","dap");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        */
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  /*Request code*/ , intent,
                PendingIntent.FLAG_ONE_SHOT);

        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  Request code , intent,
                PendingIntent.FLAG_ONE_SHOT);
        */
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TripTalk")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        ChatMsgFromDBAsync chatMsgFromDBAsync = new ChatMsgFromDBAsync();

        try {
            String strResult = chatMsgFromDBAsync.execute(title).get();

            if(!title.equals("DAP_MESSAGE")) {

                if (strResult.contains("CHAT_MESSAGE:")) {

                    ActivityManager activity_manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);

                    String log_tag = "Task Activity :";
                    for (int i = 0; i < task_info.size(); i++) {

                        Log.d(log_tag, "[" + i + "] activity:" + task_info.get(i).topActivity.getPackageName() + " >> " + task_info.get(i).topActivity.getShortClassName());

                        if (task_info.get(i).topActivity.getShortClassName().contains("RoomActivity")) {
                            Intent intent2 = new Intent("FCM-MESSAGE");
                            intent2.putExtra("CHAT_MESSAGE", strResult);
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                            localBroadcastManager.sendBroadcast(intent2);
                            Log.d("CHAT_MESSAGE", strResult);

                            //Room Activity가 활성화 상태에서 처리

                        } else {
                            //RoomActivity가 아닌 상태에서 처리 진행 필요..
                        }
                    }

                }
            }else
            {
                Intent intent2 = new Intent("DAP-MESSAGE");
                intent2.putExtra("DAP-MESSAGE","TEST");//넣고 싶은 데이터가 있을 경우....
                LocalBroadcastManager localBroadcastManager =  LocalBroadcastManager.getInstance(this);
                localBroadcastManager.sendBroadcast(intent2);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    /*기존
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("sep","dap");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  Request code , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(CHANNEL_ID, name, android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("channel description");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            channelMessage.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channelMessage);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("FCM Push Test")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0  ID of notification , notificationBuilder.build());
        }

    }
    */
}
