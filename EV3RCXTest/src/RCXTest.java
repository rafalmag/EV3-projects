import lejos.hardware.device.IRTransmitter;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.SensorSelector;
import lejos.utility.TextMenu;


public class RCXTest {
	public static void main(String[] args) throws Exception {
		IRTransmitter ir = SensorSelector.createIRTransmitter(SensorPort.S1.open(I2CPort.class));
		String[] menuItems = 
			{"Msg 1", "Msg 2", "Msg 3", "A fwd", "B fwd", "C fwd", 
			 "A bwd", "B bwd", "C bwd", "P1", "P2", "P3", "P4", "P5", "Stop", "Beep"};
		
		TextMenu menu = new TextMenu(menuItems,1,"RCX Remote");
		while(true) {
			int m = menu.select();
			if (m < 0) break;
			// Send it three times like the RCX remote does
			for(int i=0;i<3;i++) {
				ir.sendRemoteCommand(1 << m);
			}
		}
	}
}
