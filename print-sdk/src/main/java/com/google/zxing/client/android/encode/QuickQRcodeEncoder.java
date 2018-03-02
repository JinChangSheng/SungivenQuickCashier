package com.google.zxing.client.android.encode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import cn.pospal.www.app.ManagerApp;

public class QuickQRcodeEncoder {
	
	public static Bitmap createQRCode(String text) {
	    return createQRCode(text, 200);
	}

	public static Bitmap createQRCodeWithLogo(String text, @DrawableRes int logoId) {
        Bitmap qrBmp = createQRCode(text, 200);
        qrBmp = addLogo(qrBmp, logoId);

        return qrBmp;
	}
	
	public static Bitmap createQRCode(String text, int dimension) {
		try {
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text, dimension, BarcodeFormat.QR_CODE);
			
			return qrCodeEncoder.encodeAsBitmap();
		} catch (WriterException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	public static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		//获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		//logo大小为二维码整体大小的1/6
		float scaleFactor = srcWidth * 1.0f / 6 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

	public static Bitmap addLogo(Bitmap src, @DrawableRes int logoId) {
		Bitmap logo = BitmapFactory.decodeResource(ManagerApp.getInstance().getResources(), logoId);
		return addLogo(src, logo);
	}

}
