package cn.pospal.www.hardware.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.PrintJob;
import cn.pospal.www.hardware.printer.oject.ReceiptLabelJob;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.SystemUtil;

/**
 * 通过网络打印（WIFI或者直连）
 * @author NearSOC
 * 2013.02.18
 */
public class PrintLabelByNet extends AbstractTscPrinter {
	private static final String NAME = ManagerApp.getInstance().getString(R.string.printer_name_label);

	private static final int TIMETOUT = 10 * 1000;   // 10s
	
	private String printerIpStr = "";
	private Socket socket;
	protected SocketAddress address;
	private OutputStream outStream;
	private InputStream inputStream;
	private boolean isInitedOK = false;		// 初始化完成标记

    public PrintLabelByNet() {
		connectType = CONNECT_TYPE_NET;
        lineWidth = (int)(24 * (AppConfig.labelWidth / 40.0f) + 0.5);
    }

    /**
	 * 测试IP是否可以联通
	 * @param ipAddress
	 * @return
	 */
	private void testIp(final String ipAddress) {
		D.out("DDDDD testIp, ipAddress = " + ipAddress);
		
		Runnable runner = new Runnable() {
			
			@Override
			public void run() {
				try {
					// 试一试能不能ping通
					if (!SystemUtil.pingLocalIp(printerIpStr)) {
						D.out("XXXXXX000 TYPE_DISABLE");
						isInitedOK = false;
						sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
						return;
					}
					if(outStream != null) {
						try {
							outStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							outStream = null;
						}
					}
					if(inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							inputStream = null;
						}
					}
					if(socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							socket = null;
						}
					}

                    // 有时候创建socket会失败（原因未知）
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

                    if (socket != null) {
                        address = new InetSocketAddress(ipAddress, Constance.NET_PRINTER_PORT_JIABO);
                        socket.setKeepAlive(true);
                        socket.connect(address, TIMETOUT);
                        outStream = socket.getOutputStream();
                        outStream.write("BEEP\n".getBytes());
                        outStream.flush();

                        isInitedOK = true;
                    } else {
                        isInitedOK = false;
                    }
				} catch (UnknownHostException e) {
					D.out(e);
					isInitedOK = false;
				} catch (IOException e) {
					D.out(e);
					isInitedOK = false;
				} catch (Exception e) {
					D.out(e);
					isInitedOK = false;
				} finally {
					if(outStream != null) {
						try {
							outStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							outStream = null;
						}
					}
					
					if(inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							inputStream = null;
						}
					}
					
					if(socket != null) {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							socket = null;
						}
					}
				}
			}
		};
		
		Thread thread = new Thread(runner);
		thread.start();
	}

	@Override
	public boolean initPrinter() {
        super.initPrinter();

		printerIpStr = ManagerData.getLabelPrinterIpInfo();
		D.out("DDDDD printerIpStr = " + printerIpStr);
		if(SystemUtil.isIp(printerIpStr)) {
			testIp(printerIpStr);
		}
		
		try {
			Thread.sleep(INIT_DELAY_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean isInitedOK() {
		return isInitedOK;
	}

	@Override
	public boolean hasPrinter() {
		return SystemUtil.isIp(printerIpStr);
	}

	@Override
	public boolean isConnected() {
		return socket == null ? false : ((socket.isClosed() ? false : socket.isConnected()));
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void closePrinter() {
		if(outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				outStream = null;
			}
		}
		
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				inputStream = null;
			}
		}
		
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				socket = null;
			}
		}
	}

	@Override
	protected InputStream getPrinterInputStream() {
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		if(!isInitedOK || outStream == null) {
			try {
				if(socket == null || socket.isClosed()) {
					socket = new Socket(printerIpStr, Constance.NET_PRINTER_PORT_JIABO);
				}
				outStream = socket.getOutputStream();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outStream;
	}
	
	@Override
	public boolean print(PrintJob printJob) throws Exception {
		boolean result = super.print(printJob);
		closePrinter();
		return result;
	}

	@Override
    public void initSupportPrintTypes() {
		supportPrintTypes.add(new SupportPrintType(ReceiptLabelJob.class, 0));
	}

    @Override
	public boolean equals(Object o) {
		boolean typeEquals = super.equals(o);
		if (typeEquals) {
			PrintLabelByNet other = (PrintLabelByNet) o;

			if (other.printerIpStr != null) {
				return other.printerIpStr.equals(printerIpStr);
			}
		}

		return false;
	}

	/**
	 * 佳博网口打印机可以检测状态
	 * @return
	 */
	@Override
	protected int getStatus() {
		D.out("PrintByNet getStatus");
		// ping
		if (!SystemUtil.pingLocalIp(printerIpStr)) {
			D.out("XXXXXX000 TYPE_DISABLE");
			sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
			return STATUS_ERROR;
		}
		// 发送紧急数据
//		if(socket != null && socket.isConnected()) {
//			try {
//				socket.sendUrgentData(0xFF);
//			} catch (IOException e) {
//				//网络断开
//				e.printStackTrace();
//				D.out("XXXXXX TYPE_DISABLE");
//				sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
//				return STATUS_ERROR;
//			}
//		} else {
//			sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
//			return STATUS_ERROR;
//		}
		D.out("XXXXXX TYPE_CONNECT");

		sendPrinterStatus(DeviceEvent.TYPE_CONNECT);
		return STATUS_CONNECTED;
	}

    public String getPrinterIp() {
        return printerIpStr;
    }

}
