package www.pospal.cn.sungivenquickcashier;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import cn.pospal.www.debug.D;

/**
 * Created by Near on 2016/9/6 0006.
 */
public class PospalPopup extends PopupWindow {
    public PospalPopup(Context context) {
        super(context);
    }

    public PospalPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PospalPopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PospalPopup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PospalPopup() {
        super();
    }

    public PospalPopup(View contentView) {
        super(contentView);
    }

    public PospalPopup(int width, int height) {
        super(width, height);
    }

    public PospalPopup(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public PospalPopup(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFocusable(false);
        } else {
            setFocusable(true);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFocusable(false);
        } else {
            setFocusable(true);
        }
        D.out("PospalPopup 111 showAsDropDown = " + Build.VERSION.SDK_INT);
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFocusable(false);
        } else {
            setFocusable(true);
        }
        D.out("PospalPopup 222 showAsDropDown = " + Build.VERSION.SDK_INT);
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFocusable(false);
        } else {
            setFocusable(true);
        }
        D.out("PospalPopup 333 showAsDropDown = " + Build.VERSION.SDK_INT);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
