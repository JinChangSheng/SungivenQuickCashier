package cn.pospal.www.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jinchangsheng on 18/7/30.
 */

public class DatabaseHelper {
    public static final String DATABASE_NAME = "Sungiven.db";
    // 销售交易记录
    public static final String TABLE_RETAILSALE_UPDATE = "retailsaleUpdate";
    private static SQLiteDatabase database;
    private static Context context;

    public static synchronized void initDatabase(Context context) {
        DatabaseHelper.context = context;
        if (database == null) {
            database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        }
    }

    public synchronized static SQLiteDatabase getDatabase() {
        if (database == null) {
            database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        }
        return database;

    }

    public static void createAllTables() {
        TableRetailSaleUpdate tableRetailSaleUpdate = TableRetailSaleUpdate.getInstance();
        tableRetailSaleUpdate.createTable();
    }
}
