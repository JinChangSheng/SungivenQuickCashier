package cn.pospal.www.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jinchangsheng on 18/2/8.
 */

public class H5SuperPrintContent implements Serializable{
    /**
     * {
     "company": "沙坡尾吃堡",
     "markNo": "001",
     "orderNum": "201802061427158450001",
     "datetime": "2018-02-06 14:54:13",
     "allCnt": "3",
     "originalAmount": "45",
     "discountAmount": "45",
     "takeMoney": "45",
     "discount": "100",
     "payment": "支付宝",
     "printLists": [
     {
     "windowName": "古龙沙茶面",
     "windowNum":"201"
     "product": [
     {
     "productName": "沙茶面",
     "price": "12",
     "num": "2",
     "totalAmount": "24"
     },
     {
     "productName": "酱油冰激凌",
     "price": "5",
     "num": "1",
     "totalAmount": "5"
     }
     ]
     },
     {
     "windowName": "沙县小吃",
     "product": [
     {
     "productName": "飘香拌面",
     "price": "10",
     "num": "1",
     "totalAmount": "10"
     }
     ]
     }
     ]
     }"
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
    public List<WindowItem> printLists;
    public class WindowItem implements Serializable{
        private String windowName;
        private String windowNum;
        private List<ProductItem> product;
        public class ProductItem implements Serializable{
            private String productName;
            private String price;
            private String num;
            private String totalAmount;

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

        public String getWindowName() {
            return windowName;
        }

        public void setWindowName(String windowName) {
            this.windowName = windowName;
        }

        public String getWindowNum() {
            return windowNum;
        }

        public void setWindowNum(String windowNum) {
            this.windowNum = windowNum;
        }

        public List<ProductItem> getProduct() {
            return product;
        }

        public void setProduct(List<ProductItem> product) {
            this.product = product;
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

    public List<WindowItem> getPrintLists() {
        return printLists;
    }

    public void setPrintLists(List<WindowItem> printLists) {
        this.printLists = printLists;
    }
}
