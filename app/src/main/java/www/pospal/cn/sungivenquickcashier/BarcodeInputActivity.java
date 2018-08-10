package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;

public class BarcodeInputActivity extends BaseActivity {
    public static final int REQUEST = 3211;

    public static final String INTENT_MARK_NO = "markNo";
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.input_et)
    EditText markNoEt;
    @Bind(R.id.confirm_btn)
    TextView confirmBtn;
    @Bind(R.id.single_btn_ll)
    LinearLayout singleBtnLl;
    @Bind(R.id.root_rl)
    LinearLayout rootRl;

    private String markNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_input);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        markNo = intent.getStringExtra(INTENT_MARK_NO);

        if (StringUtil.isNullOrEmpty(markNo)) {
            markNoEt.setText("");
        } else {
            markNoEt.setText(markNo);
            markNoEt.selectAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterFullScreenMode();
    }

    @Override
    protected boolean delayInit() {
        SystemUtil.openKeyboard(markNoEt);
        return super.delayInit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemUtil.closeKeyboard(markNoEt);
    }

    @OnClick({R.id.close_ib, R.id.confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_ib:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.confirm_btn:
                String markNo = markNoEt.getText().toString();
                if (TextUtils.isEmpty(markNo)) {
                    showLoading(R.string.pls_enter_barcode);
                    return;
                }
                Intent data = new Intent();
                data.putExtra(INTENT_MARK_NO, markNo);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}
