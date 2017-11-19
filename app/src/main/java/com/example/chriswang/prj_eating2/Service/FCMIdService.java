package com.example.chriswang.prj_eating2.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.chriswang.prj_eating2.Service.SharedPrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ChrisWang on 2017/11/15.
 */

public class FCMIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public static final String TOKEN_BROADCAST = "fvmtokenbroadcast";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void storeToken(String token){
        SharedPrefManager.getmInstance(getApplicationContext()).storeToken(token);
    }
}
