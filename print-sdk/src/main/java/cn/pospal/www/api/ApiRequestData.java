package cn.pospal.www.api;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import cn.pospal.www.debug.D;
import cn.pospal.www.responseBean.BaseResponse;
import cn.pospal.www.util.GsonUtil;
import cn.pospal.www.util.SystemUtil;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;


public class ApiRequestData<T> extends JsonRequest<ApiRespondData<T>> {
    private final static Gson GSON = GsonUtil.getInstance();

    private static final ApiRespondData ERROR_RESPOND = new ApiRespondData();

    static {
        ERROR_RESPOND.setStatus(ApiRespondData.STATUS_ERROR);
        ERROR_RESPOND.setMessages(new String[]{"接口出现错误"});

    }

    private Class clazz;

    public ApiRequestData(String url, final Map<String, Object> map, Class clazz, final Integer requestType, final ApiResponseJsonListener listener) {
        super(Method.POST, url, GSON.toJson(map),
                new Response.Listener<ApiRespondData<T>>() {
                    @Override
                    public void onResponse(ApiRespondData<T> response) {
                        response.setRequestType(requestType);
                        BaseResponse baseResponse = JsonUtils.getBean(response.getRaw(), BaseResponse.class);
                        if (baseResponse != null){
                            response.setEchoMessage(baseResponse.getEchoMessage());
                        }
                        if (baseResponse != null && baseResponse.echoCode == 0){
                            listener.success(response);
                        }else{
                            listener.error(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ERROR_RESPOND.setVolleyError(error);
                        ERROR_RESPOND.setRequestType(requestType);
                        ERROR_RESPOND.setRequestJsonStr(GSON.toJson(map));
                        listener.error(ERROR_RESPOND);
                        D.out("ApiRequest error = " + error);
                    }
                });
        header = new HashMap<>(HEADERS_COMMON);

        D.out("xxxx url = " + url);
        D.out("xxxx map = " + GSON.toJson(map));
        this.clazz = clazz;

        DefaultRetryPolicy DEFAULT_RETRY_POLICY = new DefaultRetryPolicy(DEFAULT_TIMEOUT, DEFAULT_RETRY_TIME, BACKOFF_MULT);
        setRetryPolicy(DEFAULT_RETRY_POLICY);
        D.out("DEFAULT_RETRY_POLICY timeout = " + DEFAULT_RETRY_POLICY.getCurrentTimeout() +
                ", retryCnt = " + DEFAULT_RETRY_POLICY.getCurrentRetryCount());
    }

    public ApiRespondData<T> fromJson(String json, Class clazz) {
        Type objectType = type(ApiRespondData.class, clazz);
        return GSON.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Type objectType = type(ApiRespondData.class, clazz);
        return GSON.toJson(this, objectType);
    }

    private ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    private int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    private String getRealString(byte[] data) {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean isGzip = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (isGzip) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected Response<ApiRespondData<T>> parseNetworkResponse(NetworkResponse networkResponse) {
        String jsonString = getRealString(networkResponse.data);
        D.out(jsonString);
        ApiRespondData<T> data = null;
        if (clazz != null) {
            data = fromJson(jsonString, clazz);
        } else {    // 没有指定clazz我们返回初始数据
            data = fromJson(jsonString, Object.class);
            data.setRaw(jsonString);
        }
        D.out("XXX000 data = " + GsonUtil.getInstance().toJson(data));
        if (data.getResult() != null) {
            D.out("XXX000 class = " + data.getResult().getClass());
        }

        return Response.success(data, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    public static final Map<String, String> HEADERS_COMMON;

    static {
        HEADERS_COMMON = new HashMap<>();

        String deviceName = "android_pos";
        String userAgent = deviceName
                + SystemUtil.getVerCode() + ";"
                + android.os.Build.MODEL + ";"
                + android.os.Build.VERSION.RELEASE;
        //HEADERS_COMMON.put("apiKey", "d6fc917f81b74a2cb111846747bec2db");
        HEADERS_COMMON.put("accept-encoding", "gzip");
        HEADERS_COMMON.put("Accept", "application/json");
        HEADERS_COMMON.put("Content-Type", "application/json; charset=utf-8");
        HEADERS_COMMON.put("Expect", "100-continue");
        HEADERS_COMMON.put("user-Agent", userAgent);
    }

    private Map<String, String> header;
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    private static final int DEFAULT_TIMEOUT = 60000;               // 默认超时
    private static final int DEFAULT_RETRY_TIME = 3;        // 默认重试次数
    public static final int DEFAULT_TIMEOUT_QUICK = 5000;   // 快速返回，5S超时
    public static final int DEFAULT_TIMEOUT_NORMAL = 30000; // 正常返回，30S超时
    public static final int DEFAULT_TIMEOUT_MIN = 60000;    // 一分钟返回，60S超时
    public static final int DEFAULT_TIMEOUT_LONG = 90000;   // 慢速返回，90S超时
    public static final int DEFAULT_TIMEOUT_MAX = 150000;   //


    //    public static final float BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    public static final float BACKOFF_MULT = 0;
}
