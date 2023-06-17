package com.weather.android.location;

import com.baidu.location.BDLocation;

public interface LocationCallback {

    /** 接受定位
     * @param bdLocation  定位数据
     */
    void onReceiveLocation(BDLocation bdLocation);
}
