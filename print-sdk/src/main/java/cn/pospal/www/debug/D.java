package cn.pospal.www.debug;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Debug打印类 Debug功能受到DEBUG_SWITCH的控制
 *
 * @author Near Chan
 */
public class D {
    // Debug总开关
    public static boolean DEBUG_SWITCH = true;
    public static String fileLogPath = "";

    public static void e(String tag, Object object) {
        if (DEBUG_SWITCH) {
            Log.e(tag, object.toString());
        }
    }

    public static void out(Exception e) {
        if (DEBUG_SWITCH) {
            e.printStackTrace();
        }
    }

    public static void out(Object obj) {
        if (DEBUG_SWITCH) {
            String str = obj.toString();
            if (str.length() > 2000) {
                int cnt = 0;
                while (cnt < str.length()) {
                    int end = (cnt + 2000) > str.length() ? str.length() : cnt + 2000;
                    System.out.println(str.substring(cnt, end));
                    cnt += 2000;
                }
            } else {
                System.out.println(obj);
            }
        }
    }

    public static void t(Context context, Object obj) {
        if (DEBUG_SWITCH) {
            if (context != null) {
                Toast.makeText(context, obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
