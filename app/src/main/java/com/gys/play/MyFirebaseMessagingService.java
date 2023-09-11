package com.gys.play;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        CloudMsgUtil.showLog("onNewToken = " + s, null);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        CloudMsgUtil.showLog("onMessageReceived " + remoteMessage.getData(), null);
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        CloudMsgUtil.showLog("onMessageSent " + s, null);
    }

}


