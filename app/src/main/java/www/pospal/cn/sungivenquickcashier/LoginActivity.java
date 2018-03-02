package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinchangsheng on 18/2/24.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.account)
    ImageView account;
    @Bind(R.id.account_tv)
    EditText accountTv;
    @Bind(R.id.password)
    ImageView password;
    @Bind(R.id.pass_word)
    EditText passWord;
    @Bind(R.id.login_tv)
    TextView loginTv;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.login_tv)
    public void onViewClicked() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
