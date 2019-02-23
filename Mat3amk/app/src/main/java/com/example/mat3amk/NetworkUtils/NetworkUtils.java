package com.example.mat3amk.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public  class NetworkUtils {

    public static  boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        if(connectivityManager!=null)
        {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if(infos!=null)
            {
                for (int i=0 ; i<infos.length;i++)
                {
                    if(infos[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
