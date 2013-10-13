import java.rmi.Naming;

import lejos.remote.ev3.RMIMenu;


public class RemoteMenu {
	
	public static final String remoteHost = "//192.168.0.9/";
	
	public static void main(String[] args) throws Exception {
        RMIMenu menu = (RMIMenu)Naming.lookup(remoteHost + "RemoteMenu");
        
        String[] programs = menu.getSampleNames();
        
        for(String program: programs) {
        	System.out.println(program);
        }
        
        menu.runSample("ColorTest");
	}
}
