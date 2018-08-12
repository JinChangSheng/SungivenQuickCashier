package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.otto.BusProvider;
import cn.pospal.www.service.SystemService;
import cn.pospal.www.service.fun.PrinterFun;

public class MainActivity extends BaseActivity implements ApiResponseJsonListener {

    @Bind(R.id.menu_iv)
    ImageView menuIv;
    @Bind(R.id.scan_tv)
    TextView scanTv;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        ManagerApp.getInstance().startAllService();
        AbstractPrinter innerPrinter = InnerPrinter.getInstance();
        if(innerPrinter != null) {
            if (innerPrinter.getName() != null) {
                D.out("XXXXXX registCustomerPrinters：" + innerPrinter.getName());
                PrinterFun.getInstance().registPrinter(innerPrinter);
//                // 解锁打印机
//                innerPrinter.closePrinter();
            } else {
                innerPrinter.closePrinter();
                innerPrinter = null;
            }
        }
        PrinterFun.getInstance().startPrintJob();

        Map<String, Object> map = new HashMap<>();
        ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_PAYTERMS),this,map,null,ApiConstans.REQUEST_PAYTERMS,this);

    }

    @Override
    public void success(ApiRespondData response) {
        D.out("response...." + response.getRaw());

    }

    @Override
    public void error(ApiRespondData response) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.menu_iv, R.id.scan_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_iv:
                startActivityForResult(new Intent(MainActivity.this, LoginOutActivity.class),LoginOutActivity.REQUEST);
                break;
            case R.id.scan_tv:
                startActivityForResult(new Intent(MainActivity.this, ScanAddProductActivity.class),ScanAddProductActivity.REQUEST);
                break;
        }
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,getString(R.string.enter_out),Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        // 停止服务
        try {
            if (SystemService.getInstance() != null) {
                SystemService.getInstance().stopSelf();
            }
        } catch (Exception e) {
            D.out(e);
        }
        // 杀死自身进程
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginOutActivity.REQUEST){
            if (resultCode == RESULT_OK){
                finish();
            }
        }
    }
}
