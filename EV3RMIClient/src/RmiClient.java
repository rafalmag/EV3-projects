
import java.rmi.Naming;
 
public class RmiClient { 
	
	public static final String remoteHost = "//192.168.0.5/";
	
    public static void main(String args[]) throws Exception {
        Pilot pilot = (Pilot)Naming.lookup(remoteHost + "RemotePilot");
        Sound sound = (Sound)Naming.lookup(remoteHost + "RemoteSound");
        Battery battery = (Battery)Naming.lookup(remoteHost + "RemoteBattery");
        LCD lcd = (LCD)Naming.lookup(remoteHost + "RemoteLCD");
        RegulatedMotor motor = (RegulatedMotor)Naming.lookup(remoteHost + "RemoteMotorB");
        System.out.println("Remote pilot maximum speed is " + pilot.getMaxTravelSpeed());
        System.out.println("Battery voltage is " + battery.getVoltage());
        pilot.setTravelSpeed(10);
        pilot.stop();
        for(int i=0;i<4;i++) {
	        pilot.travel(100);
	        sound.beep();
	        pilot.rotate(90);
	        sound.buzz();
        }
        sound.beepSequenceUp();
        motor.rotate(360);;
    }
}