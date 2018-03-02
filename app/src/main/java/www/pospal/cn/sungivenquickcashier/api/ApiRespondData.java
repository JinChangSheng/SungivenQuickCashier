package www.pospal.cn.sungivenquickcashier.api;

import com.android.volley.VolleyError;
import com.google.gson.annotations.SerializedName;

/**
 * 接口返回
 * Created by Near Chan on 2016/3/12 0012.
 * Copyright © Zhundong Network 2010
 */
public class ApiRespondData<T> {
	public static final String TAG_STATUS = "status";
	public static final String TAG_MESSAGES = "messages";
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_ERROR = "error";
    private Integer requestType;
	private String requestJsonStr;
	private String status;
	private String[] messages;
	@SerializedName(value = "result", alternate = {"data", "sdkVersion", "sdkPromotionCoupon",
		"productStocks", "users", "sdkCustomerCategories", "sdkSuppliers", "sdkProductGuess"})
	private T result;
	private VolleyError volleyError;
	private String tag;
	private String raw;
	private String message;
	private Integer errorCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getMessages() {
		return messages;
	}

	public void setMessages(String[] messages) {
		this.messages = messages;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public VolleyError getVolleyError() {
		return volleyError;
	}

	public void setVolleyError(VolleyError volleyError) {
		this.volleyError = volleyError;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
		return status != null && status.equalsIgnoreCase(STATUS_SUCCESS);
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getRequestJsonStr() {
		return requestJsonStr;
	}

	public void setRequestJsonStr(String requestJsonStr) {
		this.requestJsonStr = requestJsonStr;
	}

	/**
	 * 获取所有错误信息
	 * @return
	 */
	public String getAllErrorMessage() {
		if (message != null && message.length() > 0) {
			return message;
		}

		if (messages != null && messages.length > 0) {
			StringBuffer sb = new StringBuffer(32);
			for (String msg : messages) {
				sb.append(msg).append(',');
			}

			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}

			return sb.toString();
		}
		return null;
	}
}
