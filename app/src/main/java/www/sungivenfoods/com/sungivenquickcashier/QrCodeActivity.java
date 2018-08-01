package www.pospal.cn.sungivenquickcashier;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pospal.www.debug.D;
import cn.pospal.www.util.SystemUtil;

public class QrCodeActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener {
    public static final int REQUEST = 111;
    public static final String INTENT_QR_CODE = "qrCode";
    public static final String INTENT_TITLE = "title";
    @Bind(R.id.barcode_v)
    CompoundBarcodeView barcodeV;
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.title_bar_rl)
    RelativeLayout titleBarRl;
    @Bind(R.id.title_tv)
    TextView titleTv;

    private CaptureManager capture;
    private BeepManager beepManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        ButterKnife.bind(this);

        titleTv.setText(getIntent().getStringExtra(INTENT_TITLE));
        beepManager = new BeepManager(this);

        barcodeV.setTorchListener(this);
        barcodeV.decodeSingle(callback);
        capture = new CaptureManager(this, barcodeV);
        initCamera();
    }

    @Override
    public void onResume() {
        barcodeV.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeV.pause();
        super.onPause();
    }

    private void initCamera() {
        if (SystemUtil.getCameraCount() > 1) {
            CameraSettings settings = barcodeV.getBarcodeView().getCameraSettings();
            if (settings.getRequestedCameraId() != Camera.CameraInfo.CAMERA_FACING_BACK) {
                settings.setRequestedCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
            }

            if (barcodeV.getBarcodeView().isPreviewActive()) {
                barcodeV.pause();
            }

            barcodeV.getBarcodeView().setCameraSettings(settings);
        }
    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            beepManager.playBeepSoundAndVibrate();
            String keyword = result.getText();
            D.out("BarcodeCallback keyword = " + keyword);
            if (keyword != null) {
                Intent data = new Intent();
                data.putExtra(INTENT_QR_CODE, keyword);
                setResult(RESULT_OK, data);
                finish();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }
}
