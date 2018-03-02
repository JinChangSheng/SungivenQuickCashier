package cn.pospal.www.hardware.printer;

import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.*;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.otto.DeviceEvent;
import cn.pospal.www.posbase.R;
import cn.pospal.www.util.SystemUtil;

/**
 * 通过网络打印（WIFI或者直连）
 * @author NearSOC
 * 2013.02.18
 */
public class PrintByNet extends AbstractEscPrinter {
	private static final String NAME_RECEIPT = ManagerApp.getInstance().getString(R.string.printer_name_receipt);
	private static final String NAME_TABLE = ManagerApp.getInstance().getString(R.string.printer_name_table);

    private static final int TIMETOUT = 10 * 1000;   // 10s

	protected String printerIpStr = "";
	protected boolean isInitedOK = false;		// 初始化完成标记
	protected Socket socket;
	protected SocketAddress address;
	protected OutputStream outStream;
	protected InputStream inputStream;

    protected int brand = BRAND_OTHER;
	
	
	public PrintByNet(int type) {
		this.type = type;
		connectType = CONNECT_TYPE_NET;
		D.out("PrintByNet type = " +type);
		
		if(type == TYPE_TABLE) {
			MAX_LINE = AppConfig.isW58Table ?
					MAX_BUFFER_SIZE / MAX_LINE_LENGTH_W58 : MAX_BUFFER_SIZE / MAX_LINE_LENGTH;
            lineWidth = AppConfig.isW58Table ? 32 : 42;
		} else {
            lineWidth = AppConfig.isW58 ? 32 : 42;
        }

        if(AppConfig.company.equals(cn.pospal.www.app.Constance.COMPANY_POSIN)
		        || AppConfig.company.equalsIgnoreCase(cn.pospal.www.app.Constance.COMPANY_ROYAL_CHICKEN)) {
            brand = BRAND_JIABO;
        }
        lastCheckTime = System.currentTimeMillis();
	}

	/**
	 * 测试IP是否可以联通
	 * @param ipAddress
	 * @return
	 */
	protected void testIp(final String ipAddress) {
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
                        inputStream = socket.getInputStream();
                        outStream.write(LF_CMD);
                        outStream.write(BEEP_CMD);
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
				}
			}
		};
		
		Thread thread = new Thread(runner);
		thread.start();
	}

	@Override
	public boolean initPrinter() {
		super.initPrinter();
		
		if(type == TYPE_RECEIPT) {
			printerIpStr = ManagerData.getReceiptPrinterIpInfo();
		} else if(type == TYPE_TABLE) {
			printerIpStr = ManagerData.getTablePrinterIpInfo();
		}
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
		if(type == TYPE_TABLE) {
			return NAME_TABLE;
		}
		return NAME_RECEIPT;
	}

	@Override
	public synchronized void closePrinter() {
		D.out("printByNet closePrinter");
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
		
//		isInitedOK = false;
	}

	@Override
	protected synchronized InputStream getPrinterInputStream() {
        if(!isInitedOK || inputStream == null) {
            try {
                if(socket == null || socket.isClosed()) {
	                socket = new Socket();
	                address = new InetSocketAddress(printerIpStr, Constance.NET_PRINTER_PORT_JIABO);
	                socket.connect(address, TIMETOUT);
                    socket.setSoTimeout(TIMETOUT);
	                socket.setKeepAlive(true);
                }
	            if (socket == null || !socket.isConnected() || socket.isInputShutdown()) {
		            isInitedOK = false;
		            return null;
	            }
                inputStream = socket.getInputStream();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return inputStream;
	}

	@Override
	protected synchronized OutputStream getPrinterOutputStream() {
		if(!isInitedOK || outStream == null) {
			try {
				if(socket == null || socket.isClosed()) {
                    int retry = 5;
                    while (retry > 0) {
                        socket = new Socket();
                        if (socket == null) {
                            SystemClock.sleep(20);
                            retry--;
                        } else {
                            break;
                        }
                    }

                    if (socket == null) {
                        throw new IOException("本机连接出错");
                    }

					address = new InetSocketAddress(printerIpStr, Constance.NET_PRINTER_PORT_JIABO);
					socket.connect(address, TIMETOUT);
					socket.setKeepAlive(true);
				}
				if (socket == null || !socket.isConnected() || socket.isOutputShutdown()) {
					isInitedOK = false;
					return null;
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
		initGeneralPrintType();
	}

    /**
     * 佳博网口打印机可以检测状态
     * @return
     */
    @Override
    protected synchronized int getStatus() {
        D.out("PrintByNet getStatus");
	    if (!SystemUtil.pingLocalIp(printerIpStr)) {
		    D.out("XXXXXX000 TYPE_DISABLE");
		    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
		    return STATUS_ERROR;
	    }

	    if (AppConfig.checkNetPrinterByCmd) {
		    D.out("getStatus checkNetPrinterByCmd");
		    if (outStream == null) {
			    getPrinterOutputStream();
		    }
		    if (inputStream == null) {
			    getPrinterInputStream();
		    }
		    D.out("inputStream = " + inputStream);
		    if (inputStream == null) {
			    isInitedOK = false;
			    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
			    return STATUS_ERROR;
		    }
		    try {
			    outStream.write(SERIAL_STATUS_CMD);
			    outStream.flush();
			    long currentTime = System.currentTimeMillis();
			    // 最多1s等待状态返回，如果没有返回当作出错
			    while (System.currentTimeMillis() - currentTime < 1000) {
				    if (inputStream.available() > 0) {
					    byte[] status = new byte[1];
					    int len = inputStream.read(status);
					    D.out("111 getStatus len = " + len);
					    for (int i = 0; i < len; i++) {
						    D.out("status[" + i + "]=" + status[i]);
					    }
					    D.out("useTime=" + (System.currentTimeMillis() - currentTime));
					    if (len == 1) {
						    // 0000 1000 = 0x08 // 屏蔽其他干扰只获取联机状态（出现错误为脱机状态）
						    if ((status[0] & 0x08) == 8) {
							    isInitedOK = false;
							    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
							    return STATUS_ERROR;
						    } else {
							    sendPrinterStatus(DeviceEvent.TYPE_CONNECT);
							    return STATUS_CONNECTED;
						    }
					    } else {
						    isInitedOK = false;
						    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
						    return STATUS_ERROR;
					    }
				    } else {
					    // 读不到休息50ms
					    SystemClock.sleep(50);
				    }
			    }

			    D.out("getStatus checkNetPrinterByCmd error");
			    isInitedOK = false;
			    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
			    return STATUS_ERROR;
		    } catch (IOException e) {
			    e.printStackTrace();

			    isInitedOK = false;
			    sendPrinterStatus(DeviceEvent.TYPE_DISABLE);
			    return STATUS_ERROR;
		    }
		}
	    D.out("XXXXXX TYPE_CONNECT");

	    sendPrinterStatus(DeviceEvent.TYPE_CONNECT);
		return STATUS_CONNECTED;
    }

    @Override
	public boolean equals(Object o) {
		boolean typeEquals = super.equals(o);
		if (typeEquals) {
			PrintByNet other = (PrintByNet) o;

			if (other.printerIpStr != null) {
				return other.printerIpStr.equals(printerIpStr);
			}
		}

		return false;
	}

	public String getPrinterIp() {
		return printerIpStr;
	}
}
