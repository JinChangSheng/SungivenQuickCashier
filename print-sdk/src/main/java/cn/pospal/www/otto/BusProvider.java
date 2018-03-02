package cn.pospal.www.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


/**
 * Otto Bus 单例模式
 * 
 * @author Near Chan
 * 
 */
public class BusProvider {
	private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

	public static Bus getInstance() {
		return BUS;
	}

	private BusProvider() {
		// No instances.
	}

}
