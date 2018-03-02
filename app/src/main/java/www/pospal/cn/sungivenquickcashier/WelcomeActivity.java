package www.pospal.cn.sungivenquickcashier;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.pospal.www.app.PermissionsChecker;
import cn.pospal.www.debug.D;

/**
 * Created by jinchangsheng on 18/2/24.
 */

public class WelcomeActivity extends BaseActivity {
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
    };
    private boolean requestPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionsChecker.lacksPermissions(PERMISSIONS)) {
            D.out("lacksPermissions");
            if (!requestPermission) {
                requestPermission = true;
                PermissionsActivity.startActivityForResult(this, getString(R.string.request_permission_msg), PERMISSIONS);
            }else{
                finish();
            }
        } else {
            goMain();
            D.out("hasPermissions");
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void goMain() {
        handler.sendEmptyMessageDelayed(666, 3000);
    }
}
