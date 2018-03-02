package cn.pospal.www.service.fun;

import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pospal.www.debug.D;
import cn.pospal.www.manager.ManagerNet;
import cn.pospal.www.otto.BusProvider;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.util.SystemUtil;

public class NetStatusFun implements IServiceFun {
	public static final String ACTION_NET_STATUS = "cn.pospal.www.netstatus";
	public static final int MSG_NET_NOCONNECTED = 123000;
	public static final int MSG_NET_MOBILE = 123001;
	public static final int MSG_NET_MOBILE_NOCONNECTED = 1230010;
	public static final int MSG_NET_WIFI_OK = 123002;						// wifi内外网都可以
	public static final int MSG_NET_WIFI_ONLY_INNER = 1230020;			// wifi内网
	private Context context;
	private boolean isRunning = true;
	public int currentRetryTimes = 0;

	public NetStatusFun(Context context) {
		super();
		this.context = context;
		D.out("DeviceStatusFun Creator");
	}

	@Override
	public void start() {
		isRunning = true;
		checkNetworkState();
		D.out("DeviceStatusFun start");
	}

	@Override
	public void stop() {
		isRunning = false;
		D.out("DeviceStatusFun stop");
	}
	
	private boolean checkBaidu() {
		URL url = null;
        try {  
            url = new URL("http://www.baidu.com");
            try {  
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(500);
                boolean isConnected = (httpURLConnection.getResponseCode() == 200);
                httpURLConnection.disconnect();
                return isConnected;
            } catch (IOException e) {
            	D.out(e);
                return false;  
            }  
        } catch (MalformedURLException e) { 
            D.out(e);
            return false;
        }
	}
	
	private void checkNetworkState() {
		final long TIME_NETCHECK = 5 * 1000;		// 5s检测一次
		final int RETRYTIME = 3;					// 重试三次失败才是失败
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isRunning) {
					try {
						Thread.sleep(TIME_NETCHECK);
					} catch (InterruptedException e) {
						D.out(e);
					}
					int netState = ManagerNet.getNetStatus();
                    D.out("checkNetworkState netState = " + netState);
                    if(netState == ManagerNet.NET_STATUS_MOBILE
                            || netState == ManagerNet.NET_STATUS_STATE) {
                        if (SystemUtil.pingWeb3()) {
                            D.out("pingWeb3 ok");
                            if (netState == ManagerNet.NET_STATUS_MOBILE) {
                                sendNetStatusBroadcast(MSG_NET_MOBILE);
                            } else {
                                sendNetStatusBroadcast(MSG_NET_WIFI_OK);
                            }
                            currentRetryTimes = 0;
                        } else {
                            if (currentRetryTimes < RETRYTIME) {
                                currentRetryTimes++;
                            } else {
                                if (netState == ManagerNet.NET_STATUS_MOBILE) {
                                    sendNetStatusBroadcast(MSG_NET_MOBILE_NOCONNECTED);
                                } else {
                                    sendNetStatusBroadcast(MSG_NET_WIFI_ONLY_INNER);
                                }
                            }
                            D.out("pingWeb3 error currentRetryTimes = " + currentRetryTimes);
                        }
                    } else {
                        currentRetryTimes = 0;
                        sendNetStatusBroadcast(MSG_NET_NOCONNECTED);
                    }

//					if(netState == ManagerNet.NET_STATUS_MOBILE) {			// 移动网络，没有连接内网
//						if(SystemUtil.pingWeb2()) {
//							sendNetStatusBroadcast(MSG_NET_MOBILE);
//							currentRetryTimes = 0;
//						} else {
//							if(currentRetryTimes  < RETRYTIME) {
//								currentRetryTimes++;
//							} else {
//								sendNetStatusBroadcast(MSG_NET_MOBILE_NOCONNECTED);
//							}
//						}
//                    } else if(netState == ManagerNet.NET_STATUS_STATE) {	// WIFI或者网线
//						if(SystemUtil.pingWeb("www.baidu.com")
//								|| SystemUtil.pingWeb("www.360.cn")) {
//							sendNetStatusBroadcast(MSG_NET_WIFI_OK);
//							currentRetryTimes = 0;
//						} else {
//							if(currentRetryTimes  < RETRYTIME) {
//								currentRetryTimes++;
//							} else {
//								sendNetStatusBroadcast(MSG_NET_WIFI_ONLY_INNER);
//							}
//						}
//					} else {			// 网络状态不确定提示错误
//                        currentRetryTimes = 0;
//                        sendNetStatusBroadcast(MSG_NET_NOCONNECTED);
//                    }
				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

    private static int currentNetState = DeviceEvent.TYPE_CONNECT;
    /**
     * 需要改写成otto
     * @param msgCode
     */
	private void sendNetStatusBroadcast(int msgCode) {
        D.out("sendNetStatusBroadcast msgCode = " + msgCode);
        DeviceEvent event = new DeviceEvent();
        event.setDevice(DeviceEvent.DEVICE_NET);
        if (msgCode == MSG_NET_MOBILE || msgCode == MSG_NET_WIFI_OK) {
            currentNetState = DeviceEvent.TYPE_CONNECT;
            event.setType(currentNetState);
        } else if (msgCode == MSG_NET_WIFI_ONLY_INNER) {
            currentNetState = DeviceEvent.TYPE_CONNECT_INNER;
            event.setType(currentNetState);
        } else if (msgCode == MSG_NET_MOBILE_NOCONNECTED
                || msgCode == MSG_NET_NOCONNECTED) {
            currentNetState = DeviceEvent.TYPE_DISCONNECT;
            event.setType(currentNetState);
        }
        BusProvider.getInstance().post(event);
	}

    public static int getCurrentNetState() {
        return currentNetState;
    }
}
