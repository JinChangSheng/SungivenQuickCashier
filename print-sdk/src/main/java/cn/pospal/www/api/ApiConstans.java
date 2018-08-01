package cn.pospal.www.api;



/**
 * Created by jinchangsheng on 17/4/7.
 */
public class ApiConstans {

    //public static String API_URL = "http://ip:port/selfretail/";
    //public static String API_URL = "http://192.168.0.218:8314/selfretail/";
    public static String API_IP = "";
    public static String API_PORT = "";
    public static String API_URL = "http://117.25.182.122:8314/selfretail/";

    public static String getApiUrl(String methor){
        return API_URL + methor;
    }
    //用户登录
    public static final String CHECK_USER = "checkUser";
    public final static int REQUEST_LOGIN = 1;
    //销售商品价格查询
    public static final String GET_PRICE = "retailsale/getprice";
    public final static int REQUEST_PRICE = 2;
    //销售准备付款
    public static final String GET_PREPARE = "retailsale/prepare";
    public final static int REQUEST_PREPARE = 3;
    //查询付款方式
    public static final String GET_PAYTERMS = "retailsale/getpayterms";
    public final static int REQUEST_PAYTERMS = 4;
    //支付提交
    public static final String GET_PAYSUBMIT = "walletPay/paySubmit";
    public final static int REQUEST_PAYSUBMIT = 5;
    //支付结果查询
    public static final String GET_PAYSTATEQUERY = "walletPay/payStateQuery";
    public final static int REQUEST_PAYSTATEQUERY = 6;
    //上传保存销售交易
    public static final String GET_UPDATE = "retailsale/update";
    public final static int REQUEST_UPDATE = 7;
    //获取销售小票打印内容
    public static final String GET_GETTALLY = "retailsale/getTally";
    public final static int REQUEST_GETTALLY = 8;
    //会员信息查询
    public static final String GET_QUERYMEMBER = "member/queryMember";
    public final static int REQUEST_QUERYMEMBER = 9;


}
