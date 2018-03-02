package cn.pospal.www.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jinchangsheng on 18/2/8.
 */

public class H5PrintContent implements Serializable{
    /**
     * {
     "company": "测试二区账号",
     "markNo": "002",
     "orderNum": "18020810085933845104",
     "datetime": "2018-02-08 10:08",
     "allCnt": "3",
     "originalAmount": "0.01",
     "discountAmount": "0.01",
     "takeMoney": "0.01",
     "discount": "100",
     "payment": "微信支付",
     "printLists": [
     {
     "productName": "测试商品1",
     "price": "0.01",
     "num": 1,
     "totalAmount": "0.01"
     }
     ]
     }
     */
    public String company;
    public String markNo;
    public String orderNum;
    public String datetime;
    public String allCnt;
    public String originalAmount;
    public String discountAmount;
    public String takeMoney;
    public String discount;
    public String payment;
    public List<ProductItem> printLists;
    public class ProductItem implements Serializable{
        public String productName;
        public String price;
        public String num;
        public String totalAmount;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMarkNo() {
        return markNo;
    }

    public void setMarkNo(String markNo) {
        this.markNo = markNo;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAllCnt() {
        return allCnt;
    }

    public void setAllCnt(String allCnt) {
        this.allCnt = allCnt;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTakeMoney() {
        return takeMoney;
    }

    public void setTakeMoney(String takeMoney) {
        this.takeMoney = takeMoney;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<ProductItem> getPrintLists() {
        return printLists;
    }

    public void setPrintLists(List<ProductItem> printLists) {
        this.printLists = printLists;
    }
}
