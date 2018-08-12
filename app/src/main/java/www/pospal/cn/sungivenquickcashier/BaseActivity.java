package www.pospal.cn.sungivenquickcashier;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.pospal.www.debug.D;

public class BaseActivity extends FragmentActivity {

    protected Context context;
    protected boolean isActive;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        D.out("baseActity onDestroy");
    }

    //设置字体大小不随手机设置而改变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 进入全屏
     *
     * @return
     */
    protected View enterFullScreenMode() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
        return decorView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        D.out("baseActity onPause");
    }

    @Override
    protected void onResume() {
        D.out("baseActity onResume");
        isActive = true;
        enterFullScreenMode();
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public void dismissLoading() {
        if (loadingPopupWindow != null && isLoading) {
            loadingPopupWindow.dismiss();
        }

        isLoading = false;
    }

    protected boolean delayInit() {return true;}

    protected boolean hasIninted = false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            if (!hasIninted) {
                hasIninted = true;
                delayInit();
            }
        }
    }

    public void showLoading(String loadingStr) {
        D.out("isFinishing = " + isFinishing() + ", isLoading = " + isLoading + ", isActive = " + isActive);
        showSystemLoading(loadingStr);
    }

    public void showLoading(@StringRes int stringId) {
        showSystemLoading(getString(stringId));
    }


    protected boolean isLoading;
    private PopupWindow loadingPopupWindow;

    /**
     * 显示加载
     */
    public void showSystemLoading(String loadingStr) {
        D.out("hangReceipts isLoading = " + isLoading);
        D.out("hangReceipts loadingStr = " + loadingStr);
        if (isFinishing() && isLoading) {
            return;
        }
        if (!isActive) {
            return;
        }

        if (loadingPopupWindow == null) {
            View contentView = View.inflate(this, R.layout.pop_loading, null);
            TextView loadingTv = (TextView) contentView.findViewById(R.id.load_str);
            loadingTv.setText(loadingStr);
            loadingPopupWindow = new PospalPopup(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        } else {
            View contentView = loadingPopupWindow.getContentView();
            TextView loadingTv = (TextView) contentView.findViewById(R.id.load_str);
            loadingTv.setText(loadingStr);
        }

        loadingPopupWindow.setBackgroundDrawable(new ColorDrawable());
        loadingPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, 0);

        isLoading = true;
    }


}
