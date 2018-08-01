package cn.pospal.www.service.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pospal.www.api.ApiConstans;
import cn.pospal.www.api.ApiRequestHelper;
import cn.pospal.www.api.ApiRespondData;
import cn.pospal.www.api.ApiResponseJsonListener;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.database.TableRetailSaleUpdate;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintTotal;
import cn.pospal.www.manager.ManagerNet;
import cn.pospal.www.requestBean.UpdateOrder;
import cn.pospal.www.responseBean.GetTally;
import cn.pospal.www.util.jsonParserUtils.JsonUtils;

/**
 * 数据上传
 */
public class DataUploaderFun implements IServiceFun, Runnable {
	private static final int TIMEOUT = 60000; 	// 60s超时
    public static final int ERROR_CHECK_TIME = 6;

	private boolean isRunning = true;

	@Override
	public void start() {
		D.out("QQQQQQQ DataUploaderFun start");

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {
		D.out("QQQQQQQ DataUploaderFun stop");
		isRunning = false;
	}

    // 服务器是根据收据上传和交接班顺序判断单据是否属于这个交接班
    // 而不是根据单据时间和交接班时间判断
    // 因此需要按照顺序上传：收银员登录->登录之后的单据->收银员交接班
	@Override
	public void run() {
		while (isRunning) {
			if (ManagerNet.isNetAlive()) {
				D.out("uploadAllTickets..........start");
				if (!isSending){
					uploadAllTickets();
				}
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	boolean isSending = false;
    private void uploadAllTickets(){
        ArrayList<UpdateOrder> list = TableRetailSaleUpdate.getInstance().searchDatas("sentState=?", new String[]{TableRetailSaleUpdate.stateUnsent + ""});
        if (!ManagerNet.isNetAlive()) {
            D.out("EEEEE NONET");
            // 断网也暂停
            try {
                Thread.sleep(NET_ERROR_SLEEP_TIMEOUT);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (list != null && list.size() > 0){
			isSending = true;
            final UpdateOrder updateOrder = list.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("tranId",updateOrder.getTranId());
            map.put("order",updateOrder.getOrder());
            ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_UPDATE), ManagerApp.getInstance(), map, null, ApiConstans.REQUEST_UPDATE, new ApiResponseJsonListener() {
				@Override
				public void success(ApiRespondData response) {
					D.out("response......." + response.getRaw());
					updateOrder.setSentState(TableRetailSaleUpdate.stateSent);
					TableRetailSaleUpdate.getInstance().editData(updateOrder);
					isSending = false;

					/**
					 * 打印单据
					 */
					Map<String, Object> map = new HashMap<>();
					map.put("filler", RamStatic.checkUser.getUser());
					map.put("saleTranId", updateOrder.getTranId());
					ApiRequestHelper.post(ApiConstans.getApiUrl(ApiConstans.GET_GETTALLY), ManagerApp.getInstance(), map, null, ApiConstans.REQUEST_GETTALLY, new ApiResponseJsonListener() {
						@Override
						public void success(ApiRespondData response) {
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
					isSending = false;
				}
			});
        }
    }

	private static final int NET_ERROR_SLEEP_TIMEOUT = 5000; 	// 断网时候休眠5s

}
