package com.project.agu.steamstalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

    static Toast toast;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()) {
                // Wifi is connected
                /*Toast toast = Toast.makeText(context,
                        "You are connected to Wifi you can stalk new users and games!",
                        Toast.LENGTH_SHORT);

                toast.show();*/
                showAToast(context,"You are connected to Wifi you can stalk new users and games!");
                Log.d("WifiConnection", "Wifi is connected: " + String.valueOf(networkInfo));
            } else {
                showAToast(context,"Wifi connection has been lost you can only use stored data.");
                /*Toast toast = Toast.makeText(context,
                        "Wifi connection has been lost you can only use stored data.",
                        Toast.LENGTH_SHORT);*/

                toast.show();
                Log.d("WifiConnection", "Wifi is disssconnected: " + String.valueOf(networkInfo));
            }
        } else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    ! networkInfo.isConnected()) {
                // Wifi is disconnected
                Toast toast = Toast.makeText(context,
                        "Wifi connection has been lost you can only use stored data.",
                        Toast.LENGTH_SHORT);

                toast.show();
                Log.d("WifiConnection", "Wifi is disconnected: " + String.valueOf(networkInfo));
            }
        }

        /*
        Log.d("network" , "1");

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            Log.d("network" , "1");
            Toast toast = Toast.makeText(context,
                    "You are connected to Wifi you can stalk new users and games!",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
        else
        {
            Log.d("network" , "2");
            Toast toast = Toast.makeText(context,
                    "Wifi connection has been lost you can only use stored data.",
                    Toast.LENGTH_SHORT);

            toast.show();
        }*/
    }

    static public void showAToast (Context context, String st){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(context, st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }
}
