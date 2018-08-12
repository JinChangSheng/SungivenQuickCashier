package www.pospal.cn.sungivenquickcashier;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pospal.www.api.ApiRespondData;
import cn.pospal.www.api.ApiResponseJsonListener;
import cn.pospal.www.database.TableRetailSaleUpdate;
import cn.pospal.www.requestBean.UpdateOrder;

/**
 * Created by jinchangsheng on 18/2/24.
 * 历史单据
 */

public class HistoryOrderActivity extends BaseActivity implements ApiResponseJsonListener {

    List<UpdateOrder> orderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        orderList = TableRetailSaleUpdate.getInstance().searchDatas("sentState=?", new String[]{TableRetailSaleUpdate.stateUnsent + ""});

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void success(ApiRespondData response) {

    }

    @Override
    public void error(ApiRespondData response) {

    }


    class SaleProductAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public SaleProductAdapter(Activity activity) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (orderList == null) {
                return 0;
            }
            return orderList.size();
        }

        @Override
        public Object getItem(int i) {
            return orderList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup viewGroup) {
            UpdateOrder order = orderList.get(position);

            if (contentView == null) {
                contentView = inflater.inflate(R.layout.adapter_orderline_product, viewGroup, false);
            }

            ViewHolder holder = (ViewHolder) contentView.getTag();
            if (holder == null) {
                holder = new ViewHolder(contentView);
            }
            if (holder.updateOrder == null || holder.updateOrder != order) {
                holder.setViews(position, order);
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

            UpdateOrder updateOrder;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void setViews(int position, final UpdateOrder updateOrder) {
                this.updateOrder = updateOrder;
                //nameTv.setText(product.getGdname());
               // priceTv.setText("¥" + NumUtil.dcm2String(product.getUnitPrice()));
                //numTv.setText("x " + NumUtil.dcm2String(product.getQty()));

            }
        }
    }
}
