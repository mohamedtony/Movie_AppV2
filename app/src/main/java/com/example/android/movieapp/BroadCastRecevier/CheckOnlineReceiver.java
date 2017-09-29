package com.example.android.movieapp.BroadCastRecevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by medo on 05-Sep-17.
 */

public class CheckOnlineReceiver extends BroadcastReceiver {
    public static boolean stateChanged=false;

    final  private  MyInterFace myInterFace;

    public CheckOnlineReceiver(MyInterFace myInterFace){
        this.myInterFace=myInterFace;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        //  NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        stateChanged = activeNetwork != null && activeNetwork.isConnected();

/*        if (stateChanged) {
            Toast.makeText(context,"You Are Online!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,context.getString(R.string.connectivity),Toast.LENGTH_LONG).show();
        }*/


        myInterFace.onChanged(stateChanged);
    }
    public interface MyInterFace{
        public void onChanged(boolean changed);
    }

}