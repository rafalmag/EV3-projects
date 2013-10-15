import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;

import lejos.remote.ev3.RMIMenu;

public class FileCopy {
	public static void main(String[] args) throws Exception {
		RMIMenu menu = (RMIMenu)Naming.lookup("//" + args[0] + "/RemoteMenu");
		File f = new File(args[1]);
		FileInputStream in = new FileInputStream(f);
		byte[] data = new byte[(int)f.length()];
	    in.read(data);
	    in.close();
	    menu.uploadFile(args[2], data);
	}
}
