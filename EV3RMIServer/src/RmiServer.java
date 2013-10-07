import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*; 

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
 
public class RmiServer {
	
    private static final NXTRegulatedMotor leftMotor = Motor.A;
	private static final NXTRegulatedMotor rightMotor = Motor.C;
	private static final float wheelDiameter = 3.0f;
	private static final float trackWidth = 18.0f;
	private static final boolean reverse = true;
	private static final DifferentialPilot delegate = new DifferentialPilot(wheelDiameter,trackWidth,leftMotor,rightMotor,reverse);

    public static void main(String args[]) throws Exception {
        System.out.println("RMI server started");
        System.setProperty("java.rmi.server.hostname", "192.168.0.5");
 
        try { //special exception handler for registry creation
            LocateRegistry.createRegistry(1099); 
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            //do nothing, error means registry already exists
            System.out.println("java RMI registry already exists.");
        }
        
        //Instantiate RmiServer
        RemotePilot pilot = new RemotePilot(delegate);
        RemoteSound sound = new RemoteSound();
        RemoteBattery battery = new RemoteBattery();
        RemoteLCD lcd = new RemoteLCD();
        RemoteMotor motorA = new RemoteMotor(Motor.A);
        RemoteMotor motorB = new RemoteMotor(Motor.B);
        RemoteMotor motorC = new RemoteMotor(Motor.C);
        RemoteMotor motorD = new RemoteMotor(Motor.D);
 
        // Bind the remote objects
        Naming.rebind("//localhost/RemotePilot", pilot);
        Naming.rebind("//localhost/RemoteSound", sound);
        Naming.rebind("//localhost/RemoteBattery", battery);
        Naming.rebind("//localhost/RemoteLCD", lcd);
        Naming.rebind("//localhost/RemoteMotorA", motorA);
        Naming.rebind("//localhost/RemoteMotorB", motorB);
        Naming.rebind("//localhost/RemoteMotorC", motorC);
        Naming.rebind("//localhost/RemoteMotorD", motorD);
        
        System.out.println("PeerServer bound in registry");
    }
}
