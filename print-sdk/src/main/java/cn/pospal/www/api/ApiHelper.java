package cn.pospal.www.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.api.ApiRequestByteArray;
import com.android.volley.api.ApiRequestParams;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import cn.pospal.www.debug.D;

@SuppressLint("NewApi")
public class ApiHelper {

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

    public static void cancel(Context context){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(context);
        }
    }

    public static <T> void post(String url, final Context context, ApiRequestParams params, final Integer requestType,
                                final ApiResponseJsonListener listener) {
        init(context);
        commonParams(params,context);
        ApiRequestByteArray request = new ApiRequestByteArray(Request.Method.POST,
                url, params, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] arg0) {
                String data = null;
                try {
                    data = new String(arg0, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                D.out("datadata.....paySuccess=" + data);
            }
        }, null);
        if (context != null) {
            request.setTag(context);
        }
        mRequestQueue.add(request);
    }

    /**
     * 公共参数（必需参数）
     *
     * @param params
     * @param context
     * @return
     */
    public static ApiRequestParams commonParams( ApiRequestParams params, final Context context) {
        params.put("teminalFeature",getDeviceId(context));//终端特征
        return params;
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