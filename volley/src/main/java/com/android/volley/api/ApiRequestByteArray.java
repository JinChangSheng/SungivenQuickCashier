package com.android.volley.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApiRequestByteArray extends Request<byte[]> {
    private static final String TAG = "ApiRequestByteArray";
    private final Listener<byte[]> mListener;

    private Object mPostBody = null;

    private HttpEntity httpEntity =null;

    private Map<String, String> headers = null;

    long  startTime = 0l;

    public ApiRequestByteArray(int method, String url, Object postBody, Listener<byte[]> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mPostBody = postBody;
        this.mListener = listener;
        startTime = System.currentTimeMillis();

        if (this.mPostBody != null && this.mPostBody instanceof ApiRequestParams) {// contains file
            this.httpEntity = ((ApiRequestParams) this.mPostBody).getEntity();
        }
    }

    /**
     * mPostBody is null or Map<String, String>, then execute this method
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getParams() throws AuthFailureError {
        if (this.httpEntity == null && this.mPostBody != null && this.mPostBody instanceof Map<?, ?>) {
            return ((Map<String, String>) this.mPostBody);//common Map<String, String>
        }
        return null;//process as json, xml or MultipartRequestParams
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (null == this.headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        return this.headers;
    }

    @Override
    public String getBodyContentType() {
        if (httpEntity != null) {
            return httpEntity.getContentType().getValue();
        }
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String) {//process as json or xml
            String postString = (String) mPostBody;
            if (postString.length() != 0) {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        if (this.httpEntity != null) {//process as MultipartRequestParams
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();// mPostBody is null or Map<String, String>
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

    }

    private String  coverTime(long  timeStamp){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return  format.format(new Date(timeStamp));
    }
}