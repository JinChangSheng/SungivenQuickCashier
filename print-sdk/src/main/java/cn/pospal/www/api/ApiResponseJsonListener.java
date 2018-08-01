package cn.pospal.www.api;


/**
 * Created by jinchangsheng on 17/4/6.
 */
public interface ApiResponseJsonListener {
    /**
     * 成功
     */
     void success(ApiRespondData response);

    /**
     * 失败
     */
     void error(ApiRespondData response);
}
