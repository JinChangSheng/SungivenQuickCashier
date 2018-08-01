package cn.pospal.www.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.UUID;

import cn.pospal.www.debug.D;
import cn.pospal.www.manager.ManagerNet;

/**
 * API请求
 * 回调监听
 * Created by jinchangsheng on 17/4/7.
 */
public class ApiRequestHelper {

    private static final String TAG = "ApiRequestHelper";
    public static RequestQueue mRequestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public static void init(Context context) {
        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static void clear() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll();
            mRequestQueue.stop();
            mRequestQueue = null;
        }
    }

    /**
     * 通过tag取消请求
     * @param context
     */
    public static void cancel(Context context) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(context);
        }
    }

    /**
     * post 返回对象
     *
     * @param url      接口
     * @param context  上下文
     * @param map      post需要传的参数
     * @param listener 回调
     */
    public static <T> void post(String url, final Context context,
                                Map<String, Object> map,Class clazz, final Integer requestType,
                                final ApiResponseJsonListener listener) {
        if (!ManagerNet.isNetAlive()){
            Toast.makeText(context,"网络错误，请检测网络",Toast.LENGTH_LONG).show();
        }

        init(context);
        map = commonParams(map, context);
        ApiRequestData request = new ApiRequestData(url,map,clazz,requestType,listener);
        if (context != null) {
            request.setTag(context);
        }
        mRequestQueue.add(request);
    }

    /**
     * 公共参数（必需参数）
     *
     * @param map
     * @param context
     * @return
     */
    public static Map<String, Object> commonParams(Map<String, Object> map, final Context context) {
        long utcTime = System.currentTimeMillis();
        String sappver = getVersionName(context);
        map.put("terminalFeature",getDeviceId(context));//终端特征
        //map.put("platform", "android");
        //map.put("os_ver", android.os.Build.VERSION.SDK_INT + "");
        //map.put("app_ver", sappver);
        //map.put("timestamp", String.valueOf(utcTime));
        return map;
    }


    /**
     * deviceID的组成为
     *
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3.  Build (ID)
     * 取不到，再采用UUID
     * @param context
     * @return
     */
    public  static  String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if(null!=wifiMac){
            //deviceId.append("\nwifi = ");
            deviceId.append(wifiMac);
        }
        //IMEI（imei）
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if(null!=imei){
            //deviceId.append("\nimei = "+imei);
            deviceId.append(imei);
        }
        //Build (ID)
        String  id= Build.ID;
        if(null!=id){
            //deviceId.append("\nBuild_ID = "+ bd.ID);
            deviceId.append(Build.ID);
        }
        if(null==deviceId.toString()|| deviceId.toString().length()>0){
            deviceId.append(deviceId.toString());
        }else{
            String uuid= UUID.randomUUID().toString().replace("-","");
            deviceId.append(uuid);
        }
        D.out("");
        return  deviceId.toString();
    }

    public static String getVersionName(final Context context) {
        String name = "";
        try {
            if (context != null) {
                name = context
                        .getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            }
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

}
