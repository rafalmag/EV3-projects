
import java.io.IOException;

import lejos.hardware.Sound;
import lejos.remote.ev3.Menu;
import lejos.remote.ev3.RemoteRequestMenu;


public class ObjectStreamMenu {

	public static void main(String[] args) throws IOException {
		Menu menu = new RemoteRequestMenu("192.168.0.9");
		
		System.out.println("Name: " + menu.getName());
		
		System.out.println("Version: " + menu.getVersion());
		
		System.out.println("Menu version: " + menu.getMenuVersion());
		
		System.out.println("Volume: " + menu.getSetting(Sound.VOL_SETTING));
		
		String[] names = menu.getSampleNames();
		
		System.out.println("\nSamples\n");
		for(String name: names) {
			System.out.println(name);
		}
		
		names = menu.getProgramNames();
		
		System.out.println("\nPrograms\n");
		for(String name: names) {
			System.out.println(name);
		}
		
		System.out.println("Size of GyroTest.jar: " + menu.getFileSize("/home/root/lejos/samples/GyroTest.jar"));
		
		menu.setSetting(Sound.VOL_SETTING, "30");
		
		menu.runSample("GyroTest");
		
		byte[] contents = menu.fetchFile("/home/root/lejos/samples/GyroTest.jar");
		
		System.out.println("File size is " + contents.length);

	}

}
