package cn.pospal.www.app;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Near Chan on 2016/7/29.
 * 2010-2015 © Copyright. Zhundong Network
 */
public class PPHurlStack extends HurlStack {
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = super.createConnection(url);
        connection.setChunkedStreamingMode(0);
//        connection.setReadTimeout(30000);
        return connection;
    }
}
