package cn.pospal.www.database;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cn.pospal.www.app.RamStatic;
import cn.pospal.www.debug.D;
import cn.pospal.www.requestBean.Order;
import cn.pospal.www.requestBean.UpdateOrder;
import cn.pospal.www.util.GsonUtil;

public class TableRetailSaleUpdate {
	private static TableRetailSaleUpdate tableRetailSaleUpdate;
	public static int stateUnsent = 0;
	public static int stateSent = 1;
	public static int stateError = 2;
	public static int stateUnknow = 3;

	private SQLiteDatabase database;

	private TableRetailSaleUpdate() {
		database = DatabaseHelper.getDatabase();
	}

	public static synchronized TableRetailSaleUpdate getInstance() {
		if (tableRetailSaleUpdate == null) {
			tableRetailSaleUpdate = new TableRetailSaleUpdate();
		}

		return tableRetailSaleUpdate;
	}

	public boolean createTable() {
		database = DatabaseHelper.getDatabase();
		database.execSQL("CREATE TABLE IF NOT EXISTS " + DatabaseHelper.TABLE_RETAILSALE_UPDATE + " (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," + 
				"myOrder TEXT," +
				"tranId TEXT," +
				"prepareTranId TEXT," +
				"datetime TEXT," +
				"sentState INTEGER);");
		return true;
	}


	@SuppressWarnings("unused")
	public ArrayList<UpdateOrder> searchDatas(String searchKeywords, String[] values) {
		ArrayList<UpdateOrder> updateOrders = new ArrayList<>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_RETAILSALE_UPDATE, null, searchKeywords, values, null, null, null);

		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					String orderStr = cursor.getString(1);
					String tranId = cursor.getString(2);
					String prepareTranId = cursor.getString(3);
					String datetime = cursor.getString(4);
					int sentState = cursor.getInt(5);
					UpdateOrder updateOrder = new UpdateOrder();
					Order order = GsonUtil.getInstance().fromJson(orderStr, Order.class);
					updateOrder.setOrder(order);
					updateOrder.setTranId(tranId);
					updateOrder.setPrepareTranId(prepareTranId);
					updateOrder.setDatetime(datetime);
					updateOrder.setSentState(sentState);
					updateOrders.add(updateOrder);
					cursor.moveToNext();
				}
			}
			cursor.close();
		}

		return updateOrders;
	}


	public synchronized void insertData(UpdateOrder order,int stateSent) {
		// 数据已经存在则没必要插入
		if (searchDatas("prepareTranId=?", new String[] { RamStatic.prepareTranId + "" }).size() > 0) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put("myOrder", GsonUtil.getInstance().toJson(order.getOrder()));
		values.put("tranId", order.getTranId());
		values.put("sentState", stateSent);
		values.put("datetime", order.getDatetime());
		values.put("prepareTranId", RamStatic.prepareTranId);
		database.insert(DatabaseHelper.TABLE_RETAILSALE_UPDATE, null, values);
	}


	/**
	 * 编辑某项数据可以直接删除再插入
	 */
	public synchronized void editData(UpdateOrder updateOrder) {
		D.out("RRRRR editData, getPrepareTranId = " + updateOrder.getPrepareTranId());

		D.out("DDDDDD NNNN state = " + updateOrder.getSentState());
		// 数据不存在则没必要编辑
		if (searchDatas("prepareTranId=?", new String[]{updateOrder.getPrepareTranId() + ""}).size() == 0) {
			D.out("DDDDDD NNNN");
			return;
		}
		ContentValues values = new ContentValues();
		values.put("myOrder", GsonUtil.getInstance().toJson(updateOrder.getOrder()));
		values.put("tranId", updateOrder.getTranId());
		values.put("sentState", updateOrder.getSentState());
		values.put("datetime", updateOrder.getDatetime());
		values.put("prepareTranId", updateOrder.getPrepareTranId());
		database.update(DatabaseHelper.TABLE_RETAILSALE_UPDATE, values, "prepareTranId=?", new String[]{updateOrder.getPrepareTranId() + ""});

	}
}
