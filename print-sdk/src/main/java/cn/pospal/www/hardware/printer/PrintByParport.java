package cn.pospal.www.hardware.printer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 并口打印机，未实现
 * 
 * @author Near Chan
 * 
 */
public class PrintByParport extends AbstractEscPrinter {
	private static final String NAME = "并口打印机";

	@Override
	public boolean initPrinter() {
		super.initPrinter();
		
		return false;
	}

	@Override
	public boolean isInitedOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrinter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public void closePrinter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected InputStream getPrinterInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OutputStream getPrinterOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void initSupportPrintTypes() {
		// TODO Auto-generated method stub
		
	}



}
