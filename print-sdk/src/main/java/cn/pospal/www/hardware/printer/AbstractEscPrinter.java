package cn.pospal.www.hardware.printer;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.client.android.encode.QuickEANEncoder;
import com.google.zxing.client.android.encode.QuickQRcodeEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.Constance;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.EscTestJob;
import cn.pospal.www.hardware.printer.oject.PrintTotal;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.PrinterTestJob;
import cn.pospal.www.hardware.printer.oject.ReceiptJob;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.otto.PrintEvent;
import cn.pospal.www.posbase.R;
import cn.pospal.www.service.fun.PrinterFun;
import cn.pospal.www.util.ImageUtil;

/**
 * POS58指令小票机抽象类
 *
 * @author Near Chan
 */
public abstract class AbstractEscPrinter extends AbstractPrinter {
    protected int type;

    @Override
    public boolean print(PrintJob printJob) throws Exception {
        D.out("YYYYY name = " + getName() + ", isConnected = " + isConnected());
        sendJobStatus(printJob, PrintEvent.STATUS_START);
        if (!isInitedOK()) {
            closePrinter();
            initPrinter();

            if (!isInitedOK()) {
                sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
                sendJobStatus(printJob, PrintEvent.STATUS_ERROR);
                return false;
            }
        }
        D.out("YYYYY reseted");

        // 获取输出流
        OutputStream os = getPrinterOutputStream();
        D.out("YYYYY os = " + os);
        // 打印完前检测打印机状态
        if (os == null) {
            throw new IOException("无法连接打印机");
        }
        if (getStatus() != STATUS_CONNECTED) {
            throw new PrinterStatusException("打印机错误");
        }
        D.out("YYYYY reseted 2222");

        Class<? extends PrintJob> clazz = printJob.getClass();
        if (clazz == PrinterTestJob.class) {
            return true;
        }

        List<String> printStrings = printJob.toPrintStrings(this);
        int type = PRINTTYPE_NORMAL;

        if (printStrings == null || printStrings.size() == 0) {
            D.out("XXXXXX printStrings == null");
            return true;
        }

        int times = 1;
        if (clazz == ReceiptJob.class) {
            times = AppConfig.receiptPrintCnt;
        }

        if (type == PRINTTYPE_RECEIPT) {
            openDrawer();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resetPrinter();

        for (int i = 0; i < times; i++) {
            if (i > 0) {
                beforeBackupPrint();
            }

            if (printJob.isRetryPrint()
                    && !printStrings.get(0).contains(ManagerApp.getInstance().getString(R.string.error_reprint))) {
                PrintUtil printFun = new PrintUtil(this);
                printStrings.addAll(0, printFun.printCenterLine(ManagerApp.getInstance().getString(R.string.error_reprint)));
            }

            if (i > 0 && !printStrings.get(0).contains(ManagerApp.getInstance().getString(R.string.receipt_backup))) {
                PrintUtil printFun = new PrintUtil(this);
                printStrings.addAll(0, printFun.printCenterLine(ManagerApp.getInstance().getString(R.string.receipt_backup)));
            }
            D.out("YYYYY string = XXXXXXXX");

            int pictureSize = 0;
            int bockSize = printStrings.size();
            for (int j = 0; j < bockSize; j += MAX_LINE) {
                int currentCursor = j + MAX_LINE;
                List<String> tempStrings;
                if (currentCursor >= bockSize) {
                    tempStrings = printStrings.subList(j, bockSize);
                } else {
                    tempStrings = printStrings.subList(j, currentCursor);
                }

                StringBuffer tempBuffer = new StringBuffer(MAX_BUFFER_SIZE);

                pictureSize = 0;
                for (String string : tempStrings) {
                    D.out("KKK string = " + string);
                    if (string.startsWith("####ABCD") && string.endsWith("DCBA####" + LF)) {   // 打印条形码
                        os.write(tempBuffer.toString().getBytes(charset));
                        os.flush();
                        String realCode = string.replace("####ABCD", "").replace("DCBA####" + LF, "");
                        printBarcode(realCode, TYPE_EAN);
                        tempBuffer.delete(0, tempBuffer.length());
                        // 重置打印机防止间距改变
                        resetPrinter();
                        pictureSize++;
                    } else if (string.contains("#QR{") || string.contains("#QRC{")) {       // 打印二维码
                        os.write(tempBuffer.toString().getBytes(charset));
                        os.flush();
                        int strat = string.indexOf("#QR{");
                        if (strat == -1) {
                            strat = string.indexOf("#QRC{");
                        }
                        int end = string.indexOf("}", strat);
                        if (end > strat + 4) {
                            String qrStr = string.substring(strat + 4, end);
                            if (!qrStr.equals("")) {
                                printBarcode(qrStr, TYPE_QR);
                            }
                            tempBuffer.delete(0, tempBuffer.length());
                            // 重置打印机防止间距改变
                            resetPrinter();
                            pictureSize++;
                        }
                    } else if (string.startsWith("##FEEDBACK##")
                            && string.endsWith("##" + LF)) {       // 打印反馈二维码
                        os.write(tempBuffer.toString().getBytes(charset));
                        os.flush();
                        String qrStr = string.replace("##FEEDBACK##", "")
                                .replace("##" + LF, "");
                        if (!qrStr.equals("")) {
                            printBarcode(qrStr, TYPE_QR);
                        }
                        tempBuffer.delete(0, tempBuffer.length());
                        // 重置打印机防止间距改变
                        resetPrinter();
                        pictureSize++;
                    } else if (string.contains("#IMG")) {

                    } else {
                        tempBuffer.append(string);
                    }
                }

                os.write(tempBuffer.toString().getBytes(charset));
                os.flush();
            }

            printFootSpace(printJob);
            cutReceipt();
            completePrint();
            startPrint();
            try {
                // 打印时间按照最慢160mm/s计算，每行高度差不多3.6mm
                int printTime = (int) (1000 * printStrings.size() * 3.6 / 160);
                // 假设图片打印时间是每张200ms
                printTime += pictureSize * 200;
                Thread.sleep(50 + printTime);
            } catch (InterruptedException e) {
                D.out(e);
            }

            // 打印完成后检测打印机状态
            if (getStatus() != STATUS_CONNECTED) {
                throw new PrinterStatusException("打印机错误");
            }

            D.out("YYYYY string = QQQQQQ");
        }

        return true;
    }

    protected void resetPrinter() {
        OutputStream os = getPrinterOutputStream();
        try {
            os.write(CLEAR_CMD);
            os.flush();
            os.write(CLEAR_LINE_SPACE);
            os.flush();
        } catch (Exception e) {
            D.out(e);
        }
    }

    protected void openDrawer() {
        OutputStream os = getPrinterOutputStream();
        try {
            os.write(OPEN_DRAWER_CMD);
            os.flush();
        } catch (Exception e) {
            D.out(e);
        }
    }

    protected int footSpaceLines = 4;

    /**
     * 打印末尾空白
     */
    protected void printFootSpace(PrintJob printJob) {
        OutputStream os = getPrinterOutputStream();
        try {
            for (int i = 0; i < footSpaceLines; i++) {
                os.write(LF_CMD);
            }
            os.flush();
        } catch (Exception e) {
            D.out(e);
        }
    }

    protected void cutReceipt() {
        OutputStream os = getPrinterOutputStream();
        try {
            os.write(CUT_RECEIPT_CMD);
            os.flush();
        } catch (Exception e) {
            D.out(e);
        }
    }

    /**
     * 打印条码
     *
     * @param barcode
     * @param type
     */
    protected void printBarcode(String barcode, int type) {
        OutputStream os = getPrinterOutputStream();
        try {
            // 佳博打印机支持图片快速打印
            if (AppConfig.company.equalsIgnoreCase(Constance.COMPANY_POSIN)
                    || AppConfig.company.equalsIgnoreCase(Constance.COMPANY_ROYAL_CHICKEN)
                    || AppConfig.company.equalsIgnoreCase(Constance.COMPANY_ELC)) {
                printBarCode(os, barcode, type, BRAND_JIABO);
            } else {
                printEscCode(os, barcode, type);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void test() {
        PrinterFun.getInstance().registPrinter(this);
        printJobs.add(new EscTestJob());
    }

    public static final byte[] PRINT_AND_SCROLL = new byte[]{27, 74, 1};        // 打印
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};                // 居中对齐

    /**
     * 通用打印图片
     *
     * @param printOutput 打印机写入管道
     * @param image       图片
     * @return 0：成功，1：失败
     */
    public final int printBmpGen(OutputStream printOutput, Bitmap image) {
        try {
            int width = image.getWidth();
            int height = image.getHeight();
            printOutput.write(CLEAR_CMD);
            printOutput.write(ALIGN_CENTER);
            printOutput.flush();
            byte[] data = new byte[]{0x1B, 0x33, 0x00};
            printOutput.write(data);
            data[0] = (byte) 0x00;
            data[1] = (byte) 0x00;
            data[2] = (byte) 0x00;
            int pixelColor;
            byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};
            escBmp[2] = (byte) 0x21;
            escBmp[3] = (byte) (width % 256);
            escBmp[4] = (byte) (width / 256);
            for (int i = 0; i < (height / 24) + 1; i++) {
                printOutput.write(escBmp, 0, escBmp.length);
                for (int j = 0; j < width; j++) {
                    for (int k = 0; k < 24; k++) {
                        // if within the BMP size
                        if (((i * 24) + k) < height) {
                            pixelColor = image.getPixel(j, (i * 24) + k);
                            if (pixelColor != -1) {
                                data[k / 8] += (byte) (128 >> (k % 8));
                            }
                        }
                    }
                    printOutput.write(data, 0, 3);
                    data[0] = (byte) 0x00;
                    data[1] = (byte) 0x00;
                    data[2] = (byte) 0x00; // Clear to Zero.
                }

                printOutput.write(PRINT_AND_SCROLL);
            }
            printOutput.write(LF_CMD);
            printOutput.write(CLEAR_CMD);
        } catch (IOException e) {
            e.printStackTrace();

            return PRINT_FAIL;
        }

        return PRINT_SUCCESS;
    }

    private static int width;
    private static int height;
    private static int pitch;
    private static byte[] data;

    private static void initData(int w, int h) {
        width = w;
        height = h;
        pitch = h / 8;
        data = new byte[w * pitch];
    }

    private static void setPixel(int x, int y) {
        if (x >= width || y >= height) {
            return;
        }
        int mask = (0x0080 >> (y % 8));
        data[x * pitch + y / 8] |= mask;
    }

    public static void print(OutputStream os) throws IOException {
        final byte[] CMD_INIT = {0x1B, 0x40};
        final byte[] CMD_UPLOAD_IMAGE = {0x1D, 0x2A, 0, 0};
        final byte[] CMD_PRINT_IMAGE = {0x1D, 0x2F, 0};
        final byte[] CMD_ALIGN_CENTER = {0x1B, 0x61, 1};

        os.write(CMD_INIT);
        os.write(CMD_ALIGN_CENTER);

        CMD_UPLOAD_IMAGE[2] = (byte) (width / 8);
        CMD_UPLOAD_IMAGE[3] = (byte) (height / 8);

        os.write(CMD_UPLOAD_IMAGE);
        os.write(data);
        os.write(CMD_PRINT_IMAGE);
    }

    private static void load(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int bw = (w + 7) / 8;
        if (bw > 255)
            bw = 255;

        int bh = (h + 7) / 8;
        if (bh > 24) {
            bh = 24;
        }

        initData(bw * 8, bh * 8);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (bmp.getPixel(x, y) == Color.BLACK)
                    setPixel(x, y);
            }
        }
    }

    /**
     * 打印图片
     *
     * @param os
     * @param bmp
     * @return 0：成功，1：失败
     */
    public int printBmp(OutputStream os, Bitmap bmp, int brand) {
        try {
            if (brand == BRAND_OTHER) {
                printBmpGen(os, bmp);
            }

            if (brand == BRAND_JIABO) {
                os.write(CLEAR_CMD);
                load(bmp);
                print(os);
                os.write(CLEAR_CMD);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return PRINT_FAIL;
        }

        return PRINT_SUCCESS;
    }

    public synchronized void printBarCode(OutputStream os, String code, int type, int brand)
            throws IOException {
        D.out("GGGGGG couponCode = " + code);
        Bitmap bmp = null;
        if (type == TYPE_EAN) {
            bmp = QuickEANEncoder.createEANCode(code);
        } else {
            bmp = QuickQRcodeEncoder.createQRCode(code);
        }
        ImageUtil.save2File(bmp, "bc.png");

        printBmp(os, bmp, brand);

    }

    public void printEscCode(OutputStream os, String code, int type) throws IOException {
        printBarCode(os, code, type, BRAND_OTHER);
    }

    public int getFootSpaceLines() {
        return footSpaceLines;
    }


    protected void initGeneralPrintType() {
        supportPrintTypes.clear();

        supportPrintTypes.add(new SupportPrintType(ReceiptJob.class, 0));
        supportPrintTypes.add(new SupportPrintType(PrintTotal.class, 0));
    }
}
