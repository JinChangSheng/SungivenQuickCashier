package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.service.SystemService;
import cn.pospal.www.service.fun.PrinterFun;

public class MainActivity extends BaseActivity {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //PrintH5Job superJob = new PrintH5Job();
        //PrinterFun.getInstance().offerPrintJob(superJob);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QrCodeActivity.REQUEST){
            if (resultCode == RESULT_OK){
                String code = data.getStringExtra(QrCodeActivity.INTENT_QR_CODE);
                D.out("codecodecodecode......" + code);
            }
        }
    }

    @OnClick({R.id.menu_iv, R.id.scan_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_iv:
                Intent intent = new Intent(MainActivity.this, LoginOutActivity.class);
                startActivity(intent);
                break;
            case R.id.scan_tv:
                Intent intent1 = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult(intent1,QrCodeActivity.REQUEST);
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
        return super.onKeyDown(keyCode, event);
    }

}
