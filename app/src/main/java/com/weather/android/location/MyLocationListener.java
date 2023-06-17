package com.weather.android.location;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import javax.security.auth.callback.Callback;

public class MyLocationListener implements BDLocationListener {
    private    final  String TAG=MyLocationListener.class.getSimpleName();

    //定位回调
    private    LocationCallback locationCallback;

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (locationCallback==null){
            Log.d(TAG,"callback is Null");
            return;
        }
        locationCallback.onReceiveLocation(bdLocation);
    }
}
