package www.pospal.cn.sungivenquickcashier;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.app.Constance;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.requestBean.Order;
import cn.pospal.www.responseBean.PaySubmit;

public class PayResultActivity extends BaseActivity {
    public static final int REQUEST = 444;
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.title_bar_rl)
    RelativeLayout titleBarRl;
    @Bind(R.id.state_tv)
    TextView stateTv;
    @Bind(R.id.back_tv)
    TextView backTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.retry_tv)
    TextView retryTv;
    @Bind(R.id.error_tv)
    TextView errorTv;
    PaySubmit paySubmit;
    Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);

        paySubmit = (PaySubmit) getIntent().getSerializableExtra("paySubmit");
        order = (Order) getIntent().getSerializableExtra("order");
        if (paySubmit != null) {
            Drawable drawable;
            if (paySubmit.getPayState() == Constance.payStateSuccess) {
                stateTv.setText(getString(R.string.pay_sucess));
                titleTv.setText(getString(R.string.pay_sucess));
                drawable = getResources().getDrawable(R.drawable.pay_success);
                retryTv.setVisibility(View.INVISIBLE);
            } else {
                stateTv.setText(getString(R.string.pay_error));
                titleTv.setText(getString(R.string.pay_error));
                drawable = getResources().getDrawable(R.drawable.pay_error);
                retryTv.setVisibility(View.VISIBLE);
                errorTv.setVisibility(View.VISIBLE);

                String errorStr = "请截屏此页面以备查及核对单据！！！\n";
                errorStr += "orderTranId=" + RamStatic.prepareTranId + "\n";
                errorStr += "amount=" + paySubmit.getAmount() +"\n";
                if (order != null){
                    errorStr += "flowNo=" + order.getFlowNo();
                }
                errorTv.setText(errorStr);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            stateTv.setCompoundDrawables(null, drawable, null, null);
        }
    }

    @OnClick({R.id.back_iv, R.id.back_tv, R.id.retry_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.back_tv:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.retry_tv:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
