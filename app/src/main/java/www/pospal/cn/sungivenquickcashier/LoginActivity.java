package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.api.ApiConstans;
import cn.pospal.www.api.ApiRequestHelper;
import cn.pospal.www.api.ApiRespondData;
import cn.pospal.www.api.ApiResponseJsonListener;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.debug.D;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.responseBean.CheckUser;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;

/**
 * Created by jinchangsheng on 18/2/24.
 */

public class LoginActivity extends BaseActivity implements ApiResponseJsonListener {

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
    @Bind(R.id.ip_tv)
    EditText ipTv;
    @Bind(R.id.port_tv)
    EditText portTv;
    String accountStr;
    String passwordStr;
    String ipStr;
    String portStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        accountStr = ManagerData.getUserName();
        passwordStr = ManagerData.getUserPassW();
        ipStr = ManagerData.getIp();
        portStr = ManagerData.getPort();
        ipTv.setText(ipStr + "");
        portTv.setText(portStr + "");
        toLogin(accountStr, passwordStr, ipStr, portStr, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.login_tv)
    public void onViewClicked() {
        accountStr = accountTv.getText().toString();
        passwordStr = passWord.getText().toString();
        ipStr = ipTv.getText().toString();
        portStr = portTv.getText().toString();
        toLogin(accountStr, passwordStr, ipStr, portStr, false);
    }

    private void toLogin(String account, String password, String ip, String port, boolean auto) {
        if (TextUtils.isEmpty(account)) {
            if (!auto) {
                Toast.makeText(this, getString(R.string.enter_account), Toast.LENGTH_LONG).show();
            }
            return;
        }

        if (TextUtils.isEmpty(ip)) {
            if (!auto) {
                Toast.makeText(this, getString(R.string.enter_ip), Toast.LENGTH_LONG).show();
            }
            return;
        }

        if (TextUtils.isEmpty(port)) {
            if (!auto) {
                Toast.makeText(this, getString(R.string.enter_port), Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (auto){
            accountTv.setText(accountStr);
            passWord.setText(passwordStr);
            ipTv.setText(ipStr);
            portTv.setText(portStr);
        }

        ApiConstans.API_IP = ip;
        ApiConstans.API_PORT = port;
        ApiConstans.API_URL = "http://" + ApiConstans.API_IP + ":" + ApiConstans.API_PORT + "/selfretail/";
        D.out("API_URL....." + ApiConstans.API_URL);
        loginTv.setEnabled(false);
        loginTv.setText(getString(R.string.login_ing));
        progress.setVisibility(View.VISIBLE);
        Map<String, Object> map = new HashMap<>();
        map.put("username", account);
        map.put("password", password);
        ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.CHECK_USER), this, map, null, ApiConstans.REQUEST_LOGIN, this);

    }

    @Override
    public void success(ApiRespondData response) {
        D.out("response...." + response.getRaw());
        RamStatic.checkUser = JsonUtils.getBean(response.getRaw(), CheckUser.class);
        ManagerData.saveUserName(accountStr);
        ManagerData.saveUserPassW(passwordStr);
        ManagerData.saveIp(ipStr);
        ManagerData.savePort(portStr);

        loginTv.setText(getString(R.string.login_in));
        loginTv.setEnabled(true);
        progress.setVisibility(View.GONE);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void error(ApiRespondData response) {
        String errStr;
        if (response.getAllErrorMessage() != null){
            errStr = getString(R.string.api_error) + response.getAllErrorMessage();
        }else{
            errStr = response.getEchoMessage() + "";
        }
        Toast.makeText(this, errStr, Toast.LENGTH_LONG).show();
        loginTv.setText(getString(R.string.login_in));
        loginTv.setEnabled(true);
        progress.setVisibility(View.GONE);
    }
}
