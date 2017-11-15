package com.example.user.kinveyprac;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

/**
 * Created by Dongmin on 2017-11-15.
 */

public class App extends MultiDexApplication {

    private Client sharedClient;

    public static final String TAG="App";

    @Override
    public void onCreate() {
        super.onCreate();

        sharedClient = new Client.Builder("kid_HkEnHOtp-", "c536a8bb3efa4a0d9ffbf90081da6920", this).setBaseUrl("https://baas.kinvey.com").build();
        sharedClient.ping(new KinveyPingCallback(){
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d(TAG, "Kinvey Ping Success");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "Kinvey Ping Failed", throwable);
            }
        });
    }

    public Client getSharedClient() { return sharedClient;}
}
