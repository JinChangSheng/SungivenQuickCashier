package www.pospal.cn.sungivenquickcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.manager.ManagerData;

/**
 * Created by jinchangsheng on 18/2/24.
 */

public class LoginOutActivity extends BaseActivity{
    public static final int REQUEST = 2131;
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.account_tv)
    TextView accountTv;
    @Bind(R.id.login_out_tv)
    TextView loginOutTv;
    @Bind(R.id.handover_tv)
    TextView handoverTv;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    String userName;
    String passWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_out);
        ButterKnife.bind(this);

        userName = ManagerData.getUserName();
        passWord = ManagerData.getUserPassW();
        accountTv.setText("当前账号:" + userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.back_iv, R.id.login_out_tv,R.id.handover_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.login_out_tv:
                ManagerData.saveUserName("");
                ManagerData.saveUserPassW("");
                ManagerData.savePort("");
                ManagerData.saveIp("");

                setResult(RESULT_OK);
                finish();
                break;
            case R.id.handover_tv:
                ManagerData.saveUserName("");
                ManagerData.saveUserPassW("");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

}
