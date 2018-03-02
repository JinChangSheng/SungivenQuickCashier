
package cn.pospal.www.service.fun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.os.SystemClock;
import android.text.TextPaint;

import com.google.zxing.client.android.encode.QuickEANEncoder;
import com.google.zxing.client.android.encode.QuickQRcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.AbstractPrinter;
import cn.pospal.www.hardware.printer.Constance;
import cn.pospal.www.hardware.printer.PrintByBluetooth;
import cn.pospal.www.hardware.printer.PrintByNet;
import cn.pospal.www.hardware.printer.PrintEZPLByNet;
import cn.pospal.www.hardware.printer.PrintEscByUSB;
import cn.pospal.www.hardware.printer.PrintEscByUsb2Serial;
import cn.pospal.www.hardware.printer.PrintLabelByBluetooth;
import cn.pospal.www.hardware.printer.PrintLabelByNet;
import cn.pospal.www.hardware.printer.PrintTscByUSB;
import cn.pospal.www.hardware.printer.PrintTscByUsb2Serial;
import cn.pospal.www.hardware.printer.SupportPrintType;
import cn.pospal.www.hardware.printer.oject.EscTestJob;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.PrinterTestJob;
import cn.pospal.www.hardware.printer.oject.ReceiptJob;
import cn.pospal.www.hardware.printer.oject.TscTestJob;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.manager.ManagerFile;
import cn.pospal.www.mo.SdkUsbInfo;
import cn.pospal.www.otto.BusProvider;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.otto.InitEvent;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.StringUtil;
import cn.pospal.www.util.SystemUtil;

/**
 * 打印机功能模块
 * 使用观察者模式
 * @author pospal
 *
 */
public class PrinterFun implements IServiceFun {
	private boolean isRunning = true;

	private PrintByNet printByNet;
	private PrintLabelByNet printLabelByNet;
    private PrintEZPLByNet printEZPLByNet;
	private PrintByBluetooth printByBluetooth;
    private PrintLabelByBluetooth printLabelByBluetooth;


	private List<AbstractPrinter> printers = new ArrayList<AbstractPrinter>();

    // 统计所有supportType
    private List<SupportPrintType> allSupportPrintTypes = new ArrayList<SupportPrintType>(10);

    public List<SupportPrintType> getAllSupportPrintTypes() {
        return allSupportPrintTypes;
    }

    public boolean hasPrintType(Class<? extends PrintJob> clazz, long index) {
        SupportPrintType type = new SupportPrintType(clazz, index);
        return allSupportPrintTypes.contains(type);
    }

    public boolean hasPrintType(SupportPrintType supportPrintType) {
        return allSupportPrintTypes.contains(supportPrintType);
    }

    public synchronized void registPrinter(AbstractPrinter printer) {
		if(!printers.contains(printer)) {
			D.out("XXXXXXXX add");
			printers.add(printer);
			printer.initPrinter();
            printer.initSupportPrintTypes();
		} else {
			D.out("XXXXXX eeeee");
		}
	}
	
	public synchronized void unregistPrinter(AbstractPrinter printer) {
		if(printers.contains(printer)) {
            allSupportPrintTypes.removeAll(printer.getSupportPrintTypes());
            printer.closePrinter();
			printers.remove(printer);
			printer.shutdown();
		}
	}

	public synchronized void unregistUsbPrinter(SdkUsbInfo usbInfo) {
		int size = printers.size();
		UsbDevice usbDevice = null;
		for (int i = 0; i < size; i++) {
			// 如果打印机数量发送变化则重新开始处理
			if (size != printers.size()) {
				size = printers.size();
				i = 0;
				continue;
			}

			AbstractPrinter printer = printers.get(i);
			Class<? extends AbstractPrinter> clazz = printer.getClass();
			if (clazz.equals(PrintEscByUsb2Serial.class)) {
				usbDevice = ((PrintEscByUsb2Serial)printer).getDev();
				D.out("unregistUsbPrinter usbDevice = " + usbDevice);
			} else if (clazz.equals(PrintEscByUSB.class)) {
				usbDevice = ((PrintEscByUSB)printer).getDev();
			} else if (clazz.equals(PrintTscByUsb2Serial.class)) {
				usbDevice = ((PrintTscByUsb2Serial)printer).getDev();
			} else if (clazz.equals(PrintTscByUSB.class)) {
				usbDevice = ((PrintTscByUSB)printer).getDev();
			}

			if (usbDevice != null && usbInfo.getProductId() == usbDevice.getProductId()
					&& usbInfo.getVendorId() == usbDevice.getVendorId()) {
				unregistPrinter(printer);
				D.out("unregistUsbPrinter = " + printer);
				break;
			}
		}
	}
	
	public synchronized void unregistAllPrinters() {
        D.out("unregistAllPrinters printers = " + printers);
        for (AbstractPrinter printer : printers) {
            D.out("unregistAllPrinters printer = " + printer);
			printer.closePrinter();
			printer = null;
		}

        allSupportPrintTypes.clear();
		printers.clear();
	}

    public synchronized int indexOfPrinter(AbstractPrinter printer) {
        return printers.indexOf(printer);
    }

    public synchronized AbstractPrinter getPrinterByIndex(int index) {
        if (index > -1) {
            return printers.get(index);
        }

        return null;
    }
	
	/**
	 * 添加打印任务
	 * @param printJob
	 */
	public synchronized void offerPrintJob(PrintJob printJob) {
		D.out("XXXXX PrinterFun.offerPrintJob = " + printJob + ", isRunning = " + isRunning);
		// 存在打印机才添加
		if(!isRunning) {		// 停止之后无法添加任务
			return;
		}
		
		for (AbstractPrinter printer : printers) {
			printer.jobComing(printJob, 0);
		}
	}

	private PrinterFun() {
		super();
	}
	
	private static PrinterFun printerFun;
	public static synchronized PrinterFun getInstance() {
		if(printerFun == null) {
			printerFun = new PrinterFun();
		}
		
		return printerFun;
	}


	@Override
	public void start() {
		D.out("DDDDDDDDDDD PrinterFun start");
		isRunning = true;
        isFunStopping = false;
        isAddStopped = false;
		// 初始化线程池
		pool = Executors.newFixedThreadPool(4);
		// 初始化打印机情况
		initAllPrinters();
	}

	private boolean isFunStopping = false;
	@Override
	public void stop() {
        D.out("DDDDDDDDDDD PrinterFun stop");

		isRunning = false;
		isFunStopping = true;

        if (pool != null) {
            // 关闭打印线程池
            pool.shutdown();

            // 等待任务添加进程停止
            try {
                while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.out.println("线程池未关闭");
                }
                System.out.println("线程池已关闭");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pool = null;
        }

        while (!isAddStopped) {
            System.out.println("打印添加未关闭");
            SystemClock.sleep(100);
        }
        System.out.println("打印添加已关闭");

		// 注销打印机
		unregistAllPrinters();
	}

    /**
     * 开始打印
     * 外调，防止线程修改引起崩溃
     */
    public void startPrintJob() {
        long startTime = System.currentTimeMillis();
        allSupportPrintTypes.clear();
	    for (int i=0; i<printers.size(); i++) {
			AbstractPrinter printer = printers.get(i);
		    D.out("startPrintJob printer = " + printer.getName());

            // 统计所有打印类型
            List<SupportPrintType> printTypes = printer.getSupportPrintTypes();
            for (SupportPrintType supportPrintType : printTypes) {
                if (!allSupportPrintTypes.contains(supportPrintType)) {
                    allSupportPrintTypes.add(supportPrintType);
                }
            }

            printer.setLastCheckTime(startTime);
	    }

        printAllJobs();
    }

    /**
     * 发送打印机初始化事件
     */
    private void sendPrinterInitEvent() {
        InitEvent initEvent = new InitEvent();
//        initEvent.setType(InitEvent.TYPE_HARDWARE_PRINTER);
//        initEvent.setStatus(InitEvent.STATUS_SUCCESS);
        BusProvider.getInstance().post(initEvent);
    }

	/**
	 * 初始化所有打印机
	 */
	private void initAllPrinters() {
		D.out("DDDDDDDDDDDDDD initAllPrinters");
		String receiptIp = ManagerData.getReceiptPrinterIpInfo();
		String tableReceiptIp = ManagerData.getTablePrinterIpInfo();

        if(SystemUtil.isIp(receiptIp) && !SystemUtil.isLocalIp(receiptIp)) {
			D.out("QQQQ add receipt printer");
			printByNet = new PrintByNet(PrintByNet.TYPE_RECEIPT);
			registPrinter(printByNet);
		} else {
			receiptIp = "";
		}

		if(SystemUtil.isIp(ManagerData.getLabelPrinterIpInfo())) {
            D.out("init printLabelByNet");
            if (AppConfig.labelPrinterType == ManagerData.TYPE_TSC) {
                printLabelByNet = new PrintLabelByNet();
                registPrinter(printLabelByNet);
            } else if (AppConfig.labelPrinterType == ManagerData.TYPE_EZPL) {
                printEZPLByNet = new PrintEZPLByNet();
                registPrinter(printEZPLByNet);
            }
		}

        // 蓝牙小票机
        boolean btEnable = ManagerData.getBtEnable();
        String btAddr = ManagerData.getBtAddr();
        final boolean have2InitBtReceipt = btEnable && StringUtil.isStringNotNull(btAddr);
        // 蓝牙标签机
        boolean labelBtEnable = ManagerData.getLabelBtEnable();
        String labelBtAddr = ManagerData.getLabelBtAddr();
        final boolean have2InitBtLabel = labelBtEnable && StringUtil.isStringNotNull(labelBtAddr);
        if (have2InitBtReceipt || have2InitBtLabel) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (have2InitBtReceipt) {
                        printByBluetooth = new PrintByBluetooth();
                        registPrinter(printByBluetooth);
                    }

                    if (have2InitBtLabel) {
                        D.e("chl", "registPrinter bluetooth label");
                        printLabelByBluetooth = new PrintLabelByBluetooth();
                        registPrinter(printLabelByBluetooth);
                    }

                    isRunning = true;

                    D.out("DDDDDDD InitEvent");
                    sendPrinterInitEvent();
                }
            }).start();
        } else {
            isRunning = true;
            sendPrinterInitEvent();
        }
	}
	
	private ExecutorService pool;
	/**
	 * 向打印线程池添加打印任务
	 * @param printer
	 * @param printJob
	 */
	public final synchronized void addPrintTask(final AbstractPrinter printer, final PrintJob printJob) {
		D.out("XXXXX PrinterFun addPrintTask:" + printer.getName() + ", printJob = " + printJob);
        if (!isRunning || isFunStopping) {
            return;
        }

        Future<?> future = pool.submit(new Runnable() {

			@Override
			public void run() {
                if (!isRunning || isFunStopping) {
                    return;
                }
				try {
                    D.out("XXXXX pool submit printer = " + printer);
					boolean result = printer.print(printJob);
					// 打印时候也会检测打印机
					printer.setLastCheckTime(System.currentTimeMillis());
					D.out("XXXXX pool submit result:" + result);
					if (!result) {
						// 测试任务不计入错误
						Class<? extends PrintJob> clazz = printJob.getClass();
						if (!clazz.equals(PrinterTestJob.class)
								&& !clazz.equals(EscTestJob.class)
								&& !clazz.equals(TscTestJob.class)) {
							throw new AbstractPrinter.PrinterStatusException("打印出错");
						}
					}
					D.out("XXXXX pool submit end");

					printJob.setStatus(PrintJob.STATUS_PRINTED);
				} catch (Exception e) {
					D.out(e);
                    if (!isRunning || isFunStopping) {
                        return;
                    }
					// 测试任务不计入错误
					Class<? extends PrintJob> clazz = printJob.getClass();
					if (clazz.equals(EscTestJob.class)
							|| clazz.equals(TscTestJob.class)
							|| clazz.equals(PrinterTestJob.class)) {
						printJob.setStatus(PrintJob.STATUS_PRINTED);
					} else {
                        D.out("XXXXX pool SystemClock 10000");
						// 出现异常睡眠10s重试
						SystemClock.sleep(10000);
                        D.out("XXXXX pool SystemClock 10000 end");
						// 出现错误先关闭打印机
						printer.closePrinter();
						// 打印错误标记成等待打印，这样子下次就可以重新打印了
						printJob.setStatus(PrintJob.STATUS_WATTING);
                        printJob.setRetryPrint(true);
					}

					printer.sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
				}

			}
		});

        D.out("XXXXX PrinterFun addPrintTask end");
	}

    // 打印添加线程
    private Thread printAddThread;
    private boolean isAddStopped = false;
    /**
	 * 线程大循环监听打印任务
	 */
	private void printAllJobs() {
		printAddThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				D.out("XXXXXX printAddThread start");
				// 需要判断是否在退出过程
				// 如果处于退出过程我们需要停止
				while (isRunning && !isFunStopping) {
                    SystemClock.sleep(500);

					if(!isRunning || isFunStopping) {
                        isAddStopped = true;
						return;
					}

                    int size = printers.size();
					for (int i=0; i<size; i++) {
						if(!isRunning || isFunStopping) {
                            isAddStopped = true;
							return;
						}

                        // 如果打印机数量修改了
                        if (size != printers.size()) {
                            SystemClock.sleep(1000);
                            break;
                        }

                        if(!isRunning || isFunStopping) {
                            isAddStopped = true;
                            return;
                        }

                        AbstractPrinter printer = printers.get(i);
						if(printer.getCurrentPrintJob() == null
								|| printer.getCurrentPrintJob().getStatus() == PrintJob.STATUS_PRINTED) {
							Queue<PrintJob> waittingJobs = printer.getPrintJobQueue();
							if(waittingJobs != null && waittingJobs.size() > 0) {
                                D.out("waittingJobs.poll()");
                                PrintJob currentPrintJob = waittingJobs.poll();
								if(currentPrintJob != null ) {
									if(currentPrintJob.getStatus() == PrintJob.STATUS_WATTING) {
                                        D.out("currentPrintJob new add priter = " + printer.getName() + ", job = " + currentPrintJob);
										currentPrintJob.setStatus(PrintJob.STATUS_PRINTING);
										printer.setCurrentPrintJob(currentPrintJob);
										addPrintTask(printer, currentPrintJob);
									} else if(currentPrintJob.getStatus() == PrintJob.STATUS_PRINTED) {
                                        D.out("waittingJobs.poll() 222");
										waittingJobs.poll();
									}
								}
							} else {
                                // 空闲时候需要发送检测包
                                // 只对网口和串口有效
								int connectType = printer.getConnectType();
                                if (connectType == AbstractPrinter.CONNECT_TYPE_COM
		                                || connectType == AbstractPrinter.CONNECT_TYPE_NET) {
	                                // 至少间隔10s才能添加一次
	                                long currentTime = System.currentTimeMillis();
	                                if (currentTime - printer.getLastCheckTime() > 10000) {
                                        D.out("PrinterTestJob add priter = " + printer.getName());
		                                printer.getPrintJobQueue().add(new PrinterTestJob());
		                                printer.setLastCheckTime(currentTime);
	                                }
                                }
                            }
						} else {
							PrintJob currentPrintJob = printer.getCurrentPrintJob();
							if(currentPrintJob != null ) {
								if(currentPrintJob.getStatus() == PrintJob.STATUS_WATTING) {
                                    D.out("currentPrintJob re add priter = " + printer.getName()
                                            + ", job = " + currentPrintJob);
									currentPrintJob.setStatus(PrintJob.STATUS_PRINTING);
									printer.setCurrentPrintJob(currentPrintJob);
									addPrintTask(printer, currentPrintJob);
								}
							}
						}

                        SystemClock.sleep(50);

						if(!isRunning || isFunStopping) {
                            isAddStopped = true;
							return;
						}
					}

					SystemClock.sleep(500);
				}

                isAddStopped = true;
                D.out("XXXXXX printAddThread end");
			}
		});
		printAddThread.setDaemon(true);
		printAddThread.start();
	}

    public static final int MIN_TEST_TIME = 10000;      // 10s检测一次
    public static final int PRINTER_TYPE_RECEIPT = 0;
    public static final int PRINTER_TYPE_LABEL = 1;
    public void testIpPrint(final String ip, final int printerType) {
        boolean alreadyHasPrinter = false;

        if (printerType == PRINTER_TYPE_RECEIPT) {
            for (AbstractPrinter printer : printers) {
                if (printer instanceof PrintByNet) {
                    PrintByNet printByNet = (PrintByNet) printer;
                    if (printByNet.getPrinterIp().equals(ip)) {
                        printByNet.test();
                        alreadyHasPrinter = true;
                        break;
                    }
                }
            }

            if (!alreadyHasPrinter) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket socket = null;
                        OutputStream outStream = null;
                        try {
                            int retry = 5;
                            while (retry > 0) {
                                socket = new Socket();
                                if (socket == null) {
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    retry--;
                                } else {
                                    break;
                                }
                            }

                            if (socket == null) {
                                throw new IOException("本机连接出错");
                            }

                            SocketAddress address = new InetSocketAddress(ip, Constance.NET_PRINTER_PORT_JIABO);
                            socket.setKeepAlive(true);
                            socket.connect(address, MIN_TEST_TIME);
                            outStream = socket.getOutputStream();

	                        AbstractPrinter defaultPrinter = new PrintByNet(PrintByNet.TYPE_RECEIPT);
                            outStream.write(defaultPrinter.BEEP_CMD);
                            String testString = ManagerApp.getInstance().getString(R.string.printer_test) + defaultPrinter.LF;
                            outStream.write(testString.getBytes("GBK"));
                            outStream.write(testString.getBytes("GBK"));
                            outStream.write(testString.getBytes("GBK"));
                            outStream.write(defaultPrinter.LF_CMD);
                            outStream.write(defaultPrinter.LF_CMD);
                            outStream.write(defaultPrinter.LF_CMD);
                            outStream.write(defaultPrinter.LF_CMD);
                            outStream.write(defaultPrinter.CUT_RECEIPT_CMD);
                            outStream.flush();

	                        defaultPrinter = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (outStream != null) {
                                try {
                                    outStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                thread.start();

            }
        } else if (printerType == PRINTER_TYPE_LABEL) {
            for (AbstractPrinter printer : printers) {
                if (printer instanceof PrintLabelByNet) {
                    PrintLabelByNet printLabelByNet = (PrintLabelByNet) printer;
                    if (printLabelByNet.getPrinterIp().equals(ip)) {
                        printLabelByNet.test();
                        alreadyHasPrinter = true;
                        break;
                    }
                }
            }

            if (!alreadyHasPrinter) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket socket = null;
                        OutputStream outStream = null;
                        try {
                            int retry = 5;
                            while (retry > 0) {
                                socket = new Socket();
                                if (socket == null) {
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    retry--;
                                } else {
                                    break;
                                }
                            }

                            if (socket == null) {
                                throw new IOException("本机连接出错");
                            }

                            SocketAddress address = new InetSocketAddress(ip, Constance.NET_PRINTER_PORT_JIABO);
                            socket.setKeepAlive(true);
                            socket.connect(address, MIN_TEST_TIME);
                            outStream = socket.getOutputStream();

                            outStream.write("BEEP\n".getBytes());
                            String testString = ManagerApp.getInstance().getString(R.string.printer_test);
                            outStream.write(testString.getBytes("GBK"));
                            outStream.write(testString.getBytes("GBK"));
                            outStream.write(testString.getBytes("GBK"));
                            outStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (outStream != null) {
                                try {
                                    outStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                thread.start();

            }
        }
    }

    public static final int[] DEVICECLASS_PRINTERS = new int[]{1664, 7936};
    // 已知的打印机UUID
    public static final UUID KNOWN_PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public boolean closeBluetoothPrinter() {

        try {
            if (printByBluetooth != null || printLabelByBluetooth != null) {
                if (printByBluetooth != null) {
                    printByBluetooth.close();
                }
                if (printLabelByBluetooth !=null) {
                    printLabelByBluetooth.close();
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static final int TEXT_SIZE = 26;
    /**
     * 打印任务转成图片
     * @param printJob
     * @param bitmapWidth
     * @return
     */
    public static Bitmap printJob2Bitmap(PrintJob printJob, int bitmapWidth, AbstractPrinter printer) {
        List<String> strings = printJob.toPrintStrings(printer);
        if(strings != null && strings.size() > 0) {
            int bitmapHeight = (TEXT_SIZE + 2) * strings.size();
            for (String printStr : strings) {
                if(printStr.startsWith("####ABCD")
                        && printStr.endsWith("DCBA####" + printer.LF)) {
                    bitmapHeight += 120;
                } else if(printStr.startsWith("#QR{")){
                    bitmapHeight += 240;
                } else if (printStr.startsWith("#IMG")) {
                    if (AppConfig.printTicketLogo) {
                        bitmapHeight += 340;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            TextPaint paint = new TextPaint();
            paint.setTextSize(TEXT_SIZE);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(Typeface.MONOSPACE);
            paint.setTextScaleX(0.85f);

            int yOffset = TEXT_SIZE;
            for (String printStr : strings) {
                if(printStr.contains(printer.DHDW_STR)) {
                    printStr = printStr.replace(printer.DHDW_STR, "");
                }
                if(printStr.contains(printer.DH_STR)) {
                    printStr = printStr.replace(printer.DH_STR, "");
                }
                if(printStr.contains(printer.CLR_HW_STR)) {
                    printStr = printStr.replace(printer.CLR_HW_STR, "");
                }
                if (printStr.equals("")) {
                    continue;
                }

                if(printStr.startsWith("####ABCD")
                        && printStr.endsWith("DCBA####" + printer.LF)) {
                    String realCode = printStr.replace("####ABCD", "").replace("DCBA####" + printer.LF, "");
                    Bitmap bmp = QuickEANEncoder.createEANCode(realCode);
                    canvas.drawBitmap(bmp, 0, yOffset, paint);

                    yOffset += 120;
                    continue;
                } else if(printStr.startsWith("#QR{")){
                    int start = printStr.indexOf("#QR{");
                    int end = printStr.indexOf("}", start);
                    String qrStr = printStr.substring(start + 4, end);
                    Bitmap bmp = QuickQRcodeEncoder.createQRCode(qrStr);
                    canvas.drawBitmap(bmp, 0, yOffset, paint);

                    yOffset += 240;
                    continue;
                } else if (printStr.contains("#IMG")) {

                    continue;
                }

                canvas.drawText(printStr, 0, yOffset, paint);
                canvas.save();
                canvas.restore();

                yOffset += TEXT_SIZE + 2;
            }

            String path = ManagerFile.POSPAL_ROOT + "image.png";
            D.out(path);
            try {
                FileOutputStream os = new FileOutputStream(new File(path));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        return null;
    }

}

