package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import cn.pospal.www.responseBean.QueryMember;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;

/**
 * Created by jinchangsheng on 18/2/24.
 */

public class CustomerSearchActivity extends BaseActivity implements ApiResponseJsonListener {
    public static final int REQUEST = 888;

    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.title_bar_rl)
    RelativeLayout titleBarRl;
    @Bind(R.id.account)
    ImageView account;
    @Bind(R.id.customer_et)
    EditText customerEt;
    @Bind(R.id.search_tv)
    TextView searchTv;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.customer_tv)
    TextView customerTv;
    @Bind(R.id.scan_iv)
    ImageView scanIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customersearch);
        ButterKnife.bind(this);

        if (RamStatic.memberInfo != null) {
            customerTv.setVisibility(View.VISIBLE);
            customerTv.setText(RamStatic.memberInfo.getMemberName() + "/" + RamStatic.memberInfo.getMemberCode());
            customerEt.setVisibility(View.GONE);
            searchTv.setText(getString(R.string.customer_cancel));
        }

    }

    @Override
    public void success(ApiRespondData response) {
        if (response.getRequestType() == ApiConstans.REQUEST_QUERYMEMBER) {
            D.out("response...." + response.getRaw());
            QueryMember queryMember = JsonUtils.getBean(response.getRaw(), QueryMember.class);
            if (queryMember != null) {
                RamStatic.memberInfo = queryMember.getMemberInfo();
                Toast.makeText(this, getString(R.string.customer_added), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void error(ApiRespondData response) {
        String errStr;
        if (response.getAllErrorMessage() != null) {
            errStr = getString(R.string.api_error) + response.getAllErrorMessage();
        } else {
            errStr = response.getEchoMessage();
        }
        Toast.makeText(this, errStr, Toast.LENGTH_LONG).show();
        searchTv.setClickable(true);
        progress.setVisibility(View.GONE);
    }

    @OnClick({R.id.back_iv, R.id.search_tv,R.id.scan_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:
                if (RamStatic.memberInfo != null) {
                    RamStatic.memberInfo = null;
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }
                String code = customerEt.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    searchTv.setClickable(false);
                    progress.setVisibility(View.VISIBLE);
                    Map<String, Object> map = new HashMap<>();
                    map.put("inputType", 0);
                    map.put("code", code);
                    map.put("filler", RamStatic.checkUser.getUser());
                    ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_QUERYMEMBER), this, map, null, ApiConstans.REQUEST_QUERYMEMBER, this);
                }
                break;
            case R.id.scan_iv:
                if (SystemUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(this, QrCodeActivity.class);
                intent.putExtra(QrCodeActivity.INTENT_TITLE,getString(R.string.customer_search));
                startActivityForResult(intent, QrCodeActivity.REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QrCodeActivity.REQUEST) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra(QrCodeActivity.INTENT_QR_CODE);
                if (!StringUtil.isNullOrEmpty(code)) {
                    customerEt.setText(code);
                    searchTv.performClick();
                    customerEt.setSelection(code.length());
                }
            }
        }
    }

}
