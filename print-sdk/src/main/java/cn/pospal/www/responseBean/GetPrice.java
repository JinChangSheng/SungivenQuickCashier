package cn.pospal.www.responseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jinchangsheng on 18/7/26.
 */

public class GetPrice extends BaseResponse implements Serializable{
    /**
     * "products": [
     {
     "amount": 3.00,
     "brandName": null,
     "codeType": 0,
     "favors": [

     ],
     "gdTag": "",
     "gdWtMarker": 0,
     "gdcode": "9000030002",
     "gdgid": "1021088",
     "gdname": "元初手提纸袋",
     "inputCode": "9000030002",
     "isQtyLimitPromotion": false,
     "productBarCode": "2199010001164",
     "qpc": 1,
     "qty": 1,
     "rtlPrc": 3.00,
     "sortName": null,
     "spec": "100个/捆",
     "unit": "个",
     "unitPrice": 3.00
     }
     ]
     */
    public List<GetPriceProduct> products;

    public List<GetPriceProduct> getProducts() {
        return products;
    }

    public void setProducts(List<GetPriceProduct> products) {
        this.products = products;
    }

}
