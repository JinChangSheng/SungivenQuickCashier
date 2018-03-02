package cn.pospal.www.hardware.printer;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cn.pospal.www.app.AppConfig;
import cn.pospal.www.app.ManagerApp;
import cn.pospal.www.app.RamStatic;
import cn.pospal.www.debug.D;
import cn.pospal.www.hardware.printer.oject.ReceiptLabelJob;
import cn.pospal.www.manager.ManagerData;
import cn.pospal.www.posbase.R;
import cn.pospal.www.service.fun.PrinterFun;

/**
 * Created by caihanlin on 17/4/12.
 */

public class PrintLabelByBluetooth extends AbstractTscPrinter {

    private static final String NAME = ManagerApp.getInstance().getResources().getString(R.string.printer_name_bluetooth_label);
    private OutputStream os;
    private BluetoothDevice bondDevice;

    public PrintLabelByBluetooth() {
        connectType =  CONNECT_TYPE_BT;
        lineWidth = (int)(24 * (AppConfig.labelWidth / 40.0f) + 0.5);
    }

    private boolean isPrinterConnected() {

        if(RamStatic.bluetoothSocket != null) {
            os = getPrinterOutputStream();
            if(os != null) {
                return true;
            }
        }

        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean initPrinter() {
        super.initPrinter();
        try {
            D.out("KKKKKKKK RamStatic.bluetoothSocket  = " + RamStatic.bluetoothSocket );
            if(ManagerData.getLabelBtEnable()) {
                if(RamStatic.bluetoothSocket == null) {
                    String labelBtAddr = ManagerData.getLabelBtAddr();
                    D.e("chl", ">>>>>>>>>>>labelBtAddr = " + labelBtAddr);
                    if(!labelBtAddr.equals("")) {
                        bondDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(labelBtAddr);
                        if(bondDevice == null) {
                            D.out("KKKKKKKK bondDevice  = null");
                            return false;
                        } else {
                            UUID uuid = PrinterFun.KNOWN_PRINTER_UUID;

                            // 如果是高版本我们可以获取uuid
                            if(android.os.Build.VERSION.SDK_INT >= 15) {
                                ParcelUuid[] uuids = bondDevice.getUuids();
                                D.out("KKKKKKKK uuids = " + uuids);
                                if(uuids != null && uuids.length > 0) {
                                    for (ParcelUuid parcelUuid : uuids) {
                                        D.out("KKKKKKKK parcelUuid = " + parcelUuid);
                                    }

                                    uuid = uuids[0].getUuid();
                                }
                            }

                            D.out("KKKKKKKK bondDevice  = " + bondDevice);
                            RamStatic.bluetoothSocket = bondDevice.createRfcommSocketToServiceRecord(uuid);
                            if(bondDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                                D.out("KKKKKKKKK bondDevice.getBondState"+bondDevice.getBondState());
                                RamStatic.bluetoothSocket = null;
                                return false;
                            }

                        }
                    } else {
                        return false;
                    }
                }
                RamStatic.bluetoothSocket.connect();

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                RamStatic.bluetoothSocket =(BluetoothSocket) bondDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bondDevice,1);
                RamStatic.bluetoothSocket.connect();
                D.e("chl", ">>>>>>>>>>>>>>>label createRfcommSocket");
            } catch (Exception e1) {
                RamStatic.bluetoothSocket = null;
                e1.printStackTrace();
            }

        }

        return false;
    }

    @Override
    protected boolean isInitedOK() {
        return isPrinterConnected();
    }

    @Override
    protected boolean hasPrinter() {
        return isPrinterConnected();
    }

    @Override
    protected boolean isConnected() {
        return isPrinterConnected();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void closePrinter() {
        close();
    }

    public void close() {
        try {
            if(os != null) {
                os.close();
            }
        } catch (Exception e) {
            D.out(e);
        }
        try {
            if(RamStatic.bluetoothSocket != null) {
                RamStatic.bluetoothSocket.close();
            }
        } catch (Exception e) {
            D.out(e);
        }
        os = null;
        RamStatic.bluetoothSocket = null;
        D.out("KKKKKKKKKK PrintByBluetooth close end");
    }


    @Override
    protected InputStream getPrinterInputStream() {
        return null;
    }

    @Override
    protected OutputStream getPrinterOutputStream() {
        if(RamStatic.bluetoothSocket != null) {
            if(os != null) {
                return os;
            }

            try {
                if(RamStatic.bluetoothSocket != null) {
                    os = RamStatic.bluetoothSocket.getOutputStream();
                    return os;
                }
            } catch (IOException e) {
                e.printStackTrace();
                os = null;
            }

        }

        return null;
    }

    @Override
    public void initSupportPrintTypes() {
        supportPrintTypes.add(new SupportPrintType(ReceiptLabelJob.class, 0));
    }
}
