/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.sp19.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.PrizeEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.MegaGiftManager;
import com.demo.sp19.manager.ResetDataManager;
import com.demo.sp19.manager.TokenManager;
import com.demo.sp19.screen.dashboard.DashboardActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //  SharedPreferenceHelper.getInstance(getApplicationContext()).pushChangeSetGift()
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String json = data.get("ListBrand");
            if (!TextUtils.isEmpty(json)) {
                SharedPreferenceHelper.getInstance(getApplicationContext()).pushChangeSetGift(json);
            }

            String jsonMega = data.get("ListGiftMega");
            if (!TextUtils.isEmpty(jsonMega)) {
                PrizeEntity prizeEntity = new Gson().fromJson(jsonMega, PrizeEntity.class);
                SharedPreferenceHelper.getInstance(getApplicationContext()).removeMegaGiftObject();
                Log.d(TAG, new Gson().toJson(prizeEntity));
                UserEntity userEntity = com.demo.sp19.manager.UserManager.getInstance().getUser();
                if (userEntity != null && userEntity.getOutlet().getOutletId() == prizeEntity.getOutletId()) {
                    //xác nhận đã nhận thông báo đổi quà từ firebase
                    AndroidNetworking.post("https://sp20.imark.com.vn/WS/api/ReadedNotificationSetGiftMega")
                            .addBodyParameter("pAppCode", "IDS")
                            .addBodyParameter("pOutletID", userEntity.getOutlet().getOutletId() + "")
                            .addBodyParameter("pGiftID", prizeEntity.getGiftId() + "")
                            .addBodyParameter("pTeamOutletID", userEntity.getTeamOutletId() + "")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsObject(BaseResponse.class, new ParsedRequestListener<BaseResponse>() {
                                @Override
                                public void onResponse(BaseResponse o) {
                                    if (o.getStatus() == 1) {
                                        SharedPreferenceHelper.getInstance(getApplicationContext()).pushMegaObject(jsonMega);
                                        List<Integer> list = new ArrayList<>();
                                        for (int i = 0; i < 3; i++) {
                                            list.add(0);
                                        }
                                        int position = (int) (Math.random() * list.size());
                                        list.set(position, prizeEntity.getGiftId());
                                        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPrizeObject(list);
                                    }
                                    Log.d(TAG, "Status: " + o.getStatus() + " Description: " + o.getDescription());
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, "Error");
                                }
                            });
                }

            }
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (data.get("TypeNotifi") != null) {
                if (Integer.parseInt(data.get("TypeNotifi")) == 4) {
                    SharedPreferenceHelper.getInstance(getApplicationContext()).addNotificationNew(true);
                    Intent intent = new Intent("NOTIFICATION.WARNING");
                    //bắn tín hiệu show có thông báo mới ở dashboard
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                //xóa noti đổi quà
                if (Integer.parseInt(data.get("TypeNotifi")) == 10) {
                    SharedPreferenceHelper.getInstance(getApplicationContext()).removeMegaGiftObject();
                }

                if (Integer.parseInt(data.get("TypeNotifi")) == 11) {
                     Intent intent = new Intent();
                    intent.setAction(Constants.ACTION_RESET_DATA);

                    ResetDataManager.getInstance().setResetData(1);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }

                //nhận noti gen list quay quà
                if (Integer.parseInt(data.get("TypeNotifi")) == 9) {
                    if (MegaGiftManager.getInstance().getMegaGiftObject() != null) {
                        Type listType = new TypeToken<List<Integer>>() {
                        }.getType();
                        List<Integer> list = new Gson().fromJson(data.get("listGiftID"), listType);
                        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPrizeObject(list);
                    }
                }
            }
            if (!TextUtils.isEmpty(data.get("Message"))) {

                Intent intent = new Intent(this, DashboardActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                        .setContentTitle(data.get("Title"))
                        .setContentText(data.get("Message"))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_small))

                        .setLights(Color.RED, 1000, 300)
                        .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.drawable.ic_notification_small);


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                    );
                    channel.setDescription(CHANNEL_DESC);
                    channel.setShowBadge(true);
                    channel.canShowBadge();
                    channel.enableLights(true);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);
                }

                assert notificationManager != null;
                notificationManager.notify(0, notificationBuilder.build());
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Intent intent = new Intent();
            intent = new Intent(this, DashboardActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))

                    .setContentTitle("Thông báo")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_small))

                    .setLights(Color.RED, 1000, 300)
                    .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.ic_notification_small);


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription(CHANNEL_DESC);
                channel.setShowBadge(true);
                channel.canShowBadge();
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }

            assert notificationManager != null;
            notificationManager.notify(321, notificationBuilder.build());
            Log.d(TAG, "Message Notification Data: " + remoteMessage.getData().toString());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
//        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("FCM Message")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);

//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        TokenManager.getInstance().setToken(s);
    }
}
