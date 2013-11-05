

import lejos.internal.dbus.DBusBluez;

public class PairTest {
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: PairTest bd-addr pin");
			return;
		}
		try {
			DBusBluez db = new DBusBluez();
			db.authenticateRemoteDevice(args[0], args[1]);
			System.out.println("Paired the device");
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
