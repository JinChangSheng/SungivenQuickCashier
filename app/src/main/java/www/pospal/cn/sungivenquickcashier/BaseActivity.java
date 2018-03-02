package www.pospal.cn.sungivenquickcashier;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.pospal.www.debug.D;
public class BaseActivity extends FragmentActivity{

    protected Context context;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        D.out("baseActity onDestroy");
    }

    //设置字体大小不随手机设置而改变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        D.out("baseActity onPause");
    }

    @Override
    protected void onResume() {
        D.out("baseActity onResume");
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
