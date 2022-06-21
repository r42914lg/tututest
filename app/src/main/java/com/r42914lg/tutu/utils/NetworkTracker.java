package com.r42914lg.tutu.utils;

import static com.r42914lg.tutu.Constants.LOG;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.r42914lg.tutu.model.TuTuViewModel;

/**
 * NetworkTracker monitors network status and updates ViewModel by calling setNetworkStatus()
 * when connection becomes available or is lost
 */
public class NetworkTracker {
    public static final String TAG = "LG> NetworkTracker";

    private boolean isOnline;

    public NetworkTracker(AppCompatActivity appCompatActivity, TuTuViewModel tuTuViewModel) {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (!isOnline) {
                    tuTuViewModel.setNetworkStatus(true);
                }
                isOnline = true;
                if (LOG) {
                    Log.d(TAG, ".onAvailable");
                }
            }
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (isOnline) {
                    tuTuViewModel.setNetworkStatus(false);
                }
                isOnline = false;
                if (LOG) {
                    Log.d(TAG, ".onLost");
                }
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) appCompatActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.requestNetwork(networkRequest, networkCallback);

        if (LOG) {
            Log.d(TAG, "  instance created, network callback registered...");
        }
    }
}
