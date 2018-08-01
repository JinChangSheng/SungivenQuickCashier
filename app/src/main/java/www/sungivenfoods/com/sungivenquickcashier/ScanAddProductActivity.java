package www.pospal.cn.sungivenquickcashier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import cn.pospal.www.requestBean.Order;
import cn.pospal.www.requestBean.OrderLine;
import cn.pospal.www.responseBean.GetPrice;
import cn.pospal.www.responseBean.GetPriceProduct;
import cn.pospal.www.util.DatetimeUtil;
import cn.pospal.www.util.NumUtil;
import cn.pospal.www.util.SystemUtil;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;

public class ScanAddProductActivity extends BaseActivity implements ApiResponseJsonListener {
    public static final int REQUEST = 222;
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.customer_iv)
    ImageView customerIv;
    @Bind(R.id.title_bar_rl)
    RelativeLayout titleBarRl;
    @Bind(R.id.barcode_v)
    CompoundBarcodeView barcodeV;
    @Bind(R.id.ls)
    ListView ls;
    @Bind(R.id.total_amount)
    TextView totalAmount;
    @Bind(R.id.check_out)
    LinearLayout checkOut;

    private CaptureManager capture;
    private BeepManager beepManager;
    BigDecimal totalPrice = BigDecimal.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_addproduct);
        ButterKnife.bind(this);
        beepManager = new BeepManager(this);

        capture = new CaptureManager(this, barcodeV);
        barcodeV.decodeContinuous(callback);
        initCamera();
        totalAmount.setText(getString(R.string.total_amount,NumUtil.dcm2String(totalPrice)));
    }

    @Override
    public void onResume() {
        barcodeV.resume();
        customerIv.setSelected(RamStatic.memberInfo == null ? false : true);
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


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            beepManager.playBeepSoundAndVibrate();
            barcodeV.pause();
            String keyword = result.getText();
            D.out("BarcodeCallback keyword = " + keyword);
            if (SystemUtil.isFastDoubleClick()){
                return;
            }
            if (keyword != null) {
                showLoading(R.string.search_product);
                Map<String, Object> map = new HashMap<>();
                map.put("inputCode",keyword);
                if (RamStatic.memberInfo != null){
                    map.put("memberInfo",RamStatic.memberInfo);
                }
                ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_PRICE),ScanAddProductActivity.this,map,null,ApiConstans.REQUEST_PRICE,ScanAddProductActivity.this);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @OnClick({R.id.back_iv, R.id.check_out,R.id.customer_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.customer_iv:
                Intent intent = new Intent(this,CustomerSearchActivity.class);
                startActivityForResult(intent,CustomerSearchActivity.REQUEST);
                break;
            case R.id.check_out:
                if (priceProducts == null || priceProducts.size() <= 0){
                    Toast.makeText(this,getString(R.string.pls_add_pls),Toast.LENGTH_LONG).show();
                    return;
                }
                showLoading(R.string.on_offer);
                checkOut.setEnabled(false);
                Map<String, Object> map = new HashMap<>();
                RamStatic.prepareTranId = NumUtil.randomStr(32);
                map.put("tranId",RamStatic.prepareTranId);
                Order order = new Order();
                order.setFilDate(DatetimeUtil.getDateTimeStr());
                order.setFiller(RamStatic.checkUser.getUser());
                order.setProducts(copyPriceProducts(priceProducts));
                order.setShouldPayTotal(totalPrice);
                if (RamStatic.memberInfo != null){
                    order.setMemberInfo(RamStatic.memberInfo);
                }
                map.put("order",order);
                ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_PREPARE),ScanAddProductActivity.this,map,null,ApiConstans.REQUEST_PREPARE,ScanAddProductActivity.this);
                break;
        }
    }

    public List<OrderLine> copyPriceProducts(List<GetPriceProduct> priceProducts){
        List<OrderLine> orderLines = new ArrayList<>();
        for (int i = 0; i < priceProducts.size(); i++) {
            GetPriceProduct product = priceProducts.get(i);
            OrderLine orderLine = new OrderLine();
            orderLine.setLine(i+1);
            orderLine.setInputCode(product.getInputCode());
            orderLine.setQty(product.getQty());
            orderLine.setRtlPrc(product.getRtlPrc());
            orderLine.setGdcode(product.getGdcode());
            orderLine.setGdgid(product.getGdgid());
            orderLine.setUnitPrice(product.getUnitPrice());
            orderLine.setFavors(product.getFavors());
            orderLine.setGdname(product.getGdname());
            orderLine.setProductBarCode(product.getProductBarCode());
            orderLine.setQtyLimitPromotion(product.isQtyLimitPromotion());
            orderLine.setCodeType(product.getCodeType());
            orderLine.setGdTag(product.getGdTag());
            //orderLine.setDiscountCode(product.getdi);
            //orderLine.setDiscount(product.getdi);
            orderLines.add(orderLine);
        }
        return orderLines;
    }

    private List<GetPriceProduct> priceProducts = new ArrayList<>();
    private SaleProductAdapter saleProductAdapter;
    @Override
    public void success(ApiRespondData response) {
        switch (response.getRequestType()){
            case ApiConstans.REQUEST_PRICE:
                dismissLoading();
                D.out("response...." + response.getRaw());
                GetPrice getPrice = JsonUtils.getBean(response.getRaw(),GetPrice.class);
                if (getPrice != null){
                    List<GetPriceProduct> list = getPrice.getProducts();
                    if (list != null && list.size() > 0){
                        for (GetPriceProduct priceProduct : list){
                            int pos = -1;
                            for (int i = 0; i < priceProducts.size(); i++) {
                                if (priceProducts.get(i).getInputCode().equals(priceProduct.getInputCode())){
                                    pos = i;
                                }
                            }
                            if (pos != -1){
                                BigDecimal qty = priceProducts.get(pos).getQty();
                                priceProducts.get(pos).setQty(BigDecimal.ONE.add(qty));
                                priceProducts.get(pos).setAmount(priceProducts.get(pos).getUnitPrice().multiply(priceProducts.get(pos).getQty()));
                            }else{
                                priceProducts.add(0,priceProduct);
                            }
                        }
                        setSellData();
                    }
                }else{
                    Toast.makeText(this,"接口返回错误：" + response.getRaw(),Toast.LENGTH_LONG).show();
                }
                handler.sendEmptyMessageDelayed(111,500);
                break;

            case ApiConstans.REQUEST_PREPARE:
                dismissLoading();
                checkOut.setEnabled(true);
                D.out("response...." + response.getRaw());
                Order order = JsonUtils.getBean(response.getRaw(),"order",Order.class);
                if (order != null){
                    D.out("shouldPayTotal...." + NumUtil.dcm2String(order.getShouldPayTotal()));
                    Intent intent = new Intent(this,CheckOutActivity.class);
                    intent.putExtra("order",order);
                    startActivityForResult(intent,CheckOutActivity.REQUEST);
                }else{
                    Toast.makeText(this,"接口返回错误：" + response.getRaw(),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setSellData(){
        saleProductAdapter = new SaleProductAdapter(this);
        ls.setAdapter(saleProductAdapter);
        totalPrice = BigDecimal.ZERO;
        for (GetPriceProduct priceProduct : priceProducts){
            totalPrice = totalPrice.add(priceProduct.getAmount());
            totalAmount.setText(getString(R.string.total_amount,NumUtil.dcm2String(totalPrice)));
        }
    }

    @Override
    public void error(ApiRespondData response) {
        dismissLoading();
        String errStr = "";
        switch (response.getRequestType()){
            case ApiConstans.REQUEST_PRICE:
                handler.sendEmptyMessageDelayed(111,500);
                if (response.getAllErrorMessage() != null){
                    errStr = getString(R.string.api_error) + response.getAllErrorMessage();
                }else{
                    errStr = response.getEchoMessage() + "";
                }
                Toast.makeText(this,errStr,Toast.LENGTH_LONG).show();
                break;
            case ApiConstans.REQUEST_PREPARE:
                if (response.getAllErrorMessage() != null){
                    errStr = getString(R.string.api_error) + response.getAllErrorMessage();
                }else{
                    errStr = response.getEchoMessage() + "";
                }
                Toast.makeText(this,errStr,Toast.LENGTH_LONG).show();
                checkOut.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CheckOutActivity.REQUEST){
            if (resultCode == RESULT_OK){
                priceProducts.clear();
                finish();
            }
        }
        if (requestCode == CustomerSearchActivity.REQUEST){
            if (resultCode == RESULT_OK){
                customerIv.setSelected(true);
            }else if (requestCode == RESULT_CANCELED){
                customerIv.setSelected(false);
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111){
                barcodeV.decodeContinuous(callback);
                barcodeV.resume();
            }
        }
    };

    class SaleProductAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public SaleProductAdapter(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (priceProducts == null) {
                return 0;
            }
            return priceProducts.size();
        }

        @Override
        public Object getItem(int i) {
            return priceProducts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup viewGroup) {
            GetPriceProduct product = priceProducts.get(position);

            if (contentView == null) {
                contentView = inflater.inflate(R.layout.adapter_sale_product, viewGroup, false);
            }

            ViewHolder holder = (ViewHolder) contentView.getTag();
            if (holder == null) {
                holder = new ViewHolder(contentView);
            }
            if (holder.product == null || holder.product != product) {
                holder.setViews(position, product);
                contentView.setTag(holder);
            }

            return contentView;
        }


        class ViewHolder {
            @Bind(R.id.name_tv)
            TextView nameTv;
            @Bind(R.id.unitprice_tv)
            TextView unitpriceTv;
            @Bind(R.id.rtlprice_tv)
            TextView rtlpriceTv;
            @Bind(R.id.subtract_iv)
            ImageView subtractIv;
            @Bind(R.id.qty_et)
            EditText qtyEt;
            @Bind(R.id.add_iv)
            ImageView addIv;
            @Bind(R.id.qty_ll)
            LinearLayout qtyLl;
            GetPriceProduct product;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void setViews(int position , final GetPriceProduct product){
                this.product = product;
                nameTv.setText(product.getGdname());
                unitpriceTv.setText(NumUtil.dcm2String(product.getUnitPrice()));
                qtyEt.setText(NumUtil.dcm2String(product.getQty()));
                subtractIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemUtil.isFastDoubleClick()){
                            return;
                        }
                        for (int i = 0; i < priceProducts.size(); i++) {
                            if (priceProducts.get(i).getInputCode().equals(product.getInputCode())){
                                if (BigDecimal.ONE.compareTo(priceProducts.get(i).getQty()) == 0){
                                    priceProducts.remove(priceProducts.get(i));
                                }else{
                                    priceProducts.get(i).setQty(priceProducts.get(i).getQty().subtract(BigDecimal.ONE));
                                    priceProducts.get(i).setAmount(priceProducts.get(i).getUnitPrice().multiply(priceProducts.get(i).getQty()));
                                }
                                break;
                            }
                        }
                        setSellData();
                    }
                });

                addIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemUtil.isFastDoubleClick()){
                            return;
                        }
                        for (int i = 0; i < priceProducts.size(); i++) {
                            if (priceProducts.get(i).getInputCode().equals(product.getInputCode())){
                                priceProducts.get(i).setQty(priceProducts.get(i).getQty().add(BigDecimal.ONE));
                                priceProducts.get(i).setAmount(priceProducts.get(i).getUnitPrice().multiply(priceProducts.get(i).getQty()));
                                break;
                            }
                        }
                        setSellData();
                    }
                });
            }
        }
    }
}
