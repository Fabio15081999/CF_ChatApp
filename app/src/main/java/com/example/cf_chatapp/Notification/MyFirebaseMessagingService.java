package com.example.cf_chatapp.Notification;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cf_chatapp.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.internal.Util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private static final String TAG = "get_token";


    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        if (firebaseUser != null) {
            updateToken(token);
        }

    }

    private void updateToken(String token) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        databaseReference.setValue(map);

    }
}
