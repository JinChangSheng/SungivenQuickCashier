package com.google.zxing.client.android.encode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QuickEANEncoder {
    private static final int WHITE = 0xFFFFFF;
    private static final int BLACK = 0x000000;

    public static Bitmap createEANCode(String text, int width, int height) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix result = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, codeWidth, height, null);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap createEANCode(String text) {
        return createEANCode(text, 300, 80);
    }
}
