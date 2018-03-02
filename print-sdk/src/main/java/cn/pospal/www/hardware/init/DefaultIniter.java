package cn.pospal.www.hardware.init;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.pospal.www.mo.SdkUsbInfo;

/**
 * Created by Near Chan on 2016/10/26 0026.
 * Copyright © Zhundong Network 2010
 */

public class DefaultIniter {
	public boolean uiInit() {
        return false;
	}
	public void welcomeInit() {

	}
	public void appInit() {

	}
    public void cashierInit() {

    }
    public void loadOemPayMethods() {

    }

    public void exit() {

    }

    public void mainInit(Activity activity) {

    }

    public void mainExit() {

    }

    protected List<SdkUsbInfo> SdkUsbInfos = new ArrayList<>();

    public List<SdkUsbInfo> getInnerUsbPrinters() {

        return SdkUsbInfos;
    }

    public String getSn() {

        return null;
    }

}
