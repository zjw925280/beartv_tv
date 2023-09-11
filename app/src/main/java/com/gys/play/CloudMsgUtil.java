package com.gys.play;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class CloudMsgUtil {

    private static final String TAG = "CloudMsgUtil";

    public static void init() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    showLog("token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                showLog("token success token = " + token, null);
                //vivo测试机
                //efZnhKqeQMyEA5ZOEjbQX-:APA91bHuKPPWILTJd8iQSIxXBvHKqaz7OI7tpyXhnrepo4cR4XSxn4oJRSploRtFyBrNpktgoiQUOdccizWmOhnsc11jrtKcJixgHVnlZHypoIJ96FGLpaaHk1nug6tSWbt0JxfFtjPb
                //Google android12
                //cslx5cDrR3uELqGVcZJODa:APA91bF0VFeLkPOF7zCuoDLfP7GKsZdMkU_lg0slSszk9clAXU1foMaAAA2PLQFiqLLUQ6QjWxKht05xgCToQt_qh36giwByd-1TerovOrxQaImdLjpJuaq0XYV4DzWsNjcKu4DjyHom
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt("bookId",88);
        bundle.putString("google.to","12asd5f4as5df2as1df21asd5f4as5d1f2as1df");
        RemoteMessage message = new RemoteMessage(bundle);
        FirebaseMessaging.getInstance().send(message);
    }

    public static void showLog(String text, Throwable tr) {
        if (tr == null) {
            Log.e(TAG, text);
        } else {
            Log.e(TAG, text, tr);
        }
    }
} 
