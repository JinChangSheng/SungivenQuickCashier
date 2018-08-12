package www.pospal.cn.sungivenquickcashier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.pospal.www.app.Constance;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.database.TableRetailSaleUpdate;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintTotal;
import cn.pospal.www.requestBean.Order;
import cn.pospal.www.requestBean.OrderLine;
import cn.pospal.www.requestBean.OrderPayment;
import cn.pospal.www.requestBean.UpdateOrder;
import cn.pospal.www.responseBean.GetTally;
import cn.pospal.www.responseBean.PaySubmit;
import cn.pospal.www.service.fun.PrinterFun;
import cn.pospal.www.util.DatetimeUtil;
import cn.pospal.www.util.NumUtil;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;

public class CheckOutActivity extends BaseActivity implements ApiResponseJsonListener {
    public static final int REQUEST = 333;
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.title_bar_rl)
    RelativeLayout titleBarRl;
    @Bind(R.id.ls)
    ListView ls;
    @Bind(R.id.total_amount)
    TextView totalAmount;
    @Bind(R.id.amount)
    TextView amount;
    @Bind(R.id.check_out)
    LinearLayout checkOut;

    private List<OrderLine> orderLineList;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            D.out("order....." + order.getShouldPayTotal());
            totalAmount.setText(getString(R.string.total_amount, NumUtil.dcm2String(order.getShouldPayTotal())));
            amount.setText("¥" + NumUtil.dcm2String(order.getShouldPayTotal()));
            orderLineList = order.getProducts();
            SaleProductAdapter adapter = new SaleProductAdapter(this);
            ls.setAdapter(adapter);
        }

    }

    @Override
    public void success(ApiRespondData response) {
        int requestType = response.getRequestType();
        D.out("response....." + response.getRaw());
        switch (requestType) {
            case ApiConstans.REQUEST_PAYSUBMIT:
                handler.removeMessages(loadingWhat);
                paying = false;
                dismissLoading();
                PaySubmit paySubmit = JsonUtils.getBean(response.getRaw(), PaySubmit.class);
                if (paySubmit != null) {
                    if (paySubmit.getPayState() == Constance.payStateUnknow) {
                        showLoading(getString(R.string.check_paying));
                        Map<String, Object> map = new HashMap<>();
                        map.put("tranId", NumUtil.randomStr(32));
                        map.put("payTranType", 2);//鼎付通(支持支付宝、微信)，代码2
                        map.put("filler", RamStatic.checkUser.getUser());
                        map.put("payState", Constance.payStateUnknow);
                        map.put("flowNo", order.getFlowNo());
                        map.put("orderTranId", RamStatic.prepareTranId);
                        ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_PAYSTATEQUERY), this, map, null, ApiConstans.REQUEST_PAYSTATEQUERY, this);
                    } else {
                        upLoadTicket(paySubmit);
                    }
                }else{
                    Toast.makeText(this,response.getEchoMessage() + "",Toast.LENGTH_LONG).show();
                }
                D.out("codecodecodecode......" + response.getRaw());
                break;
            case ApiConstans.REQUEST_PAYSTATEQUERY:
                handler.removeMessages(loadingWhat);
                dismissLoading();
                PaySubmit paySubmit1 = JsonUtils.getBean(response.getRaw(), PaySubmit.class);
                if (paySubmit1 != null) {//支付结果查询接口不再进行二次判断了，直接显示成功或失败
                    upLoadTicket(paySubmit1);
                }else{
                    Toast.makeText(this,response.getEchoMessage() + "",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void error(ApiRespondData response) {
        int requestType = response.getRequestType();
        String errStr;
        switch (requestType) {
            case ApiConstans.REQUEST_PAYSUBMIT:
                paying = false;
                dismissLoading();
                if (response.getAllErrorMessage() != null){
                    errStr = getString(R.string.api_error) + response.getAllErrorMessage();
                }else{
                    errStr = response.getEchoMessage() + "";
                }
                Toast.makeText(this,errStr,Toast.LENGTH_LONG).show();
                break;
            case ApiConstans.REQUEST_PAYSTATEQUERY:
                dismissLoading();
                if (response.getAllErrorMessage() != null){
                    errStr = getString(R.string.api_error) + response.getAllErrorMessage();
                }else{
                    errStr = response.getEchoMessage() + "";
                }
                Toast.makeText(this,errStr,Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void upLoadTicket(final PaySubmit paySubmit){
        final UpdateOrder updateOrder = new UpdateOrder();
        updateOrder.setTranId(RamStatic.prepareTranId);
        updateOrder.setDatetime(DatetimeUtil.getDateTimeStr());
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setPayType("34");//{"payCode":"34","payName":"鼎付通","payType":"dingfutong"}
        orderPayment.setTotal(paySubmit.getAmount());
        orderPayment.setPayWayCode(paySubmit.getPayWayCode());
        orderPayment.setPayWayName(paySubmit.getPayWayName());
        orderPayment.setPayStoreOrdNum(paySubmit.getStoreOrdNum());
        orderPayment.setPayId(paySubmitTranId);
        List<OrderPayment> orderPayments = new ArrayList<>(1);
        orderPayments.add(orderPayment);
        order.setPayments(orderPayments);
        updateOrder.setOrder(order);

        if (paySubmit.getPayState() == Constance.payStateSuccess){
            showLoading(R.string.upload_ticket);
            Map<String, Object> map = new HashMap<>();
            map.put("tranId",updateOrder.getTranId());
            map.put("order",updateOrder.getOrder());
            ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_UPDATE), ManagerApp.getInstance(), map, null, ApiConstans.REQUEST_UPDATE, new ApiResponseJsonListener() {
                @Override
                public void success(ApiRespondData response) {
                    dismissLoading();
                    D.out("response......." + response.getRaw());
                    TableRetailSaleUpdate.getInstance().insertData(updateOrder,TableRetailSaleUpdate.stateSent);

                    Intent intent = new Intent(CheckOutActivity.this, PayResultActivity.class);
                    intent.putExtra("paySubmit", paySubmit);
                    intent.putExtra("order", order);
                    startActivityForResult(intent, PayResultActivity.REQUEST);

                    /**
                     * 打印单据
                     */
                    Map<String, Object> map = new HashMap<>();
                    map.put("filler", RamStatic.checkUser.getUser());
                    map.put("saleTranId", updateOrder.getTranId());
                    ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_GETTALLY), ManagerApp.getInstance(), map, null, ApiConstans.REQUEST_GETTALLY, new ApiResponseJsonListener() {
                        @Override
                        public void success(ApiRespondData response) {
                            D.out("response......." + response.getRaw());
                            GetTally getTally = JsonUtils.getBean(response.getRaw(),GetTally.class);
                            if (getTally != null && getTally.getTally() != null){
                                D.out("response.......and.....print" + getTally.getTally());
                                PrintTotal superJob = new PrintTotal(getTally.getTally());
                                PrinterFun.getInstance().offerPrintJob(superJob);
                            }
                        }

                        @Override
                        public void error(ApiRespondData response) {

                        }
                    });

                }

                @Override
                public void error(ApiRespondData response) {
                    dismissLoading();
                    TableRetailSaleUpdate.getInstance().insertData(updateOrder,TableRetailSaleUpdate.stateUnsent);
                    Intent intent = new Intent(CheckOutActivity.this, PayResultActivity.class);
                    intent.putExtra("paySubmit", paySubmit);
                    intent.putExtra("order", order);
                    startActivityForResult(intent, PayResultActivity.REQUEST);
                }
            });
        }else if (paySubmit.getPayState() == Constance.payStateUnknow){
            TableRetailSaleUpdate.getInstance().insertData(updateOrder,TableRetailSaleUpdate.stateUnknow);
            Intent intent = new Intent(this, PayResultActivity.class);
            intent.putExtra("paySubmit", paySubmit);
            intent.putExtra("order", order);
            startActivityForResult(intent, PayResultActivity.REQUEST);
        }else{
            Toast.makeText(this,paySubmit.getEchoMessage() + "",Toast.LENGTH_LONG).show();
        }
    }


    boolean paying = false;
    private int loadingWhat = 111;
    private String paySubmitTranId = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QrCodeActivity.REQUEST) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra(QrCodeActivity.INTENT_QR_CODE);
                if (!StringUtil.isNullOrEmpty(code) && !paying) {
                    handler.sendEmptyMessageDelayed(loadingWhat,400);
                    paying = true;
                    ApiRequestHelper.cancel(this);
                    D.out("codecodecodecode......" + code);
                    Map<String, Object> map = new HashMap<>();
                    paySubmitTranId = NumUtil.randomStr(32);
                    map.put("tranId", paySubmitTranId);
                    map.put("payTranType", 2);//鼎付通支付(支持支付宝、微信)，代码4
                    map.put("barcode", code);
                    map.put("amount", order.getShouldPayTotal());
                    map.put("filler", RamStatic.checkUser.getUser());
                    map.put("products", order.getProducts());
                    map.put("flowNo", order.getFlowNo());
                    map.put("orderTranId", RamStatic.prepareTranId);
                    ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_PAYSUBMIT), this, map, null, ApiConstans.REQUEST_PAYSUBMIT, this);
                }
            }
        }else if (requestCode == PayResultActivity.REQUEST){
            if (resultCode == RESULT_OK){
                RamStatic.clearCustomer();
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (loadingWhat == msg.what){
                showLoading(R.string.paying);
            }
        }
    };


    @OnClick({R.id.back_iv, R.id.check_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.check_out:
                if (SystemUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(this, QrCodeActivity.class);
                intent.putExtra(QrCodeActivity.INTENT_TITLE,getString(R.string.scan_pay));
                startActivityForResult(intent, QrCodeActivity.REQUEST);
                break;
        }
    }


    class SaleProductAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SaleProductAdapter(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (orderLineList == null) {
                return 0;
            }
            return orderLineList.size();
        }

        @Override
        public Object getItem(int i) {
            return orderLineList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup viewGroup) {
            OrderLine product = orderLineList.get(position);

            if (contentView == null) {
                contentView = inflater.inflate(R.layout.adapter_orderline_product, viewGroup, false);
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
            @Bind(R.id.price_tv)
            TextView priceTv;
            @Bind(R.id.num_tv)
            TextView numTv;

            OrderLine product;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void setViews(int position, final OrderLine product) {
                this.product = product;
                nameTv.setText(product.getGdname());
                priceTv.setText("¥" + NumUtil.dcm2String(product.getUnitPrice()));
                numTv.setText("x " + NumUtil.dcm2String(product.getQty()));

            }
        }
    }
}
