package com.weather.android;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.weather.android.databinding.ActivityMainBinding;
import com.weather.android.location.LocationCallback;
import com.weather.android.location.MyLocationListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationCallback {

    private ActivityMainBinding binding;

    public LocationClient mLocationClient=null;
    private  final MyLocationListener myListener=new MyLocationListener();

    //权限数组
    private   final  String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求意图
    private ActivityResultLauncher<String[]>  requestPermissionIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerIntent();
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLocation();
        requestPermission();

    }

    /**
     * 初始化定位
     */
    private  void  initLocation(){
        try {
            mLocationClient=new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mLocationClient!=null){
             myListener.setLocationCallback(this);
             //注册定位监听
             mLocationClient.registerLocationListener(myListener);
            LocationClientOption  option=new LocationClientOption();
            //如果开发者需要获取当前的地址信息，此处必须为true
            option.setIsNeedAddress(true);
            //设置是否需要最新版本的地址信息
            option.setNeedNewVersionRgc(true);
            mLocationClient.setLocOption(option);

        }
    }
    private  void startLocation(){
          if(mLocationClient!=null){
               mLocationClient.start();
          }
    }
   private  void registerIntent(){
          //请求权限意图
          requestPermissionIntent=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
              @Override
              public void onActivityResult(Map<String, Boolean> result) {
                   boolean fineLocation=Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                   boolean writeStorge=Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                   if(fineLocation&&writeStorge){
                        startLocation();
                   }
              }
          });
   }

   private  void requestPermission(){
          if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
             checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissionIntent.launch(permissions);
                return;
          }

          //开始定位
          startLocation();
   }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
           float  radius=bdLocation.getRadius();  //获取定位精度，默认值为0，0f
          String coorType=bdLocation.getCoorType();
          int errorCode=bdLocation.getLocType();  //表示网络定位结果
          String  addr=bdLocation.getAddrStr();  //获取详细的地址信息
          String  country=bdLocation.getCountry();  //获取国家
          String  province=bdLocation.getProvince(); //获取省份
          String  city=bdLocation.getCity();
          String  district=bdLocation.getDistrict();  //获取区县
          String street=bdLocation.getStreet();
          String  locationDescribe=bdLocation.getLocationDescribe(); //获取位置描述信息
          binding.tvAddressDetail.setText(addr);
    }
}