package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class WPASupplicant {
	public static void writeConfiguration(String in, String out, String ssid, String pwd) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(in)));
			FileWriter fw = new FileWriter(new File(out));
			String line;
			while((line = br.readLine()) != null) {
				int i = line.indexOf("ssid=");
				if (i >= 0) {
					line += "\"" + ssid + "\"";
				}
				i=line.indexOf("psk=");
				if (i >= 0) {
					line += "\"" + pwd + "\"";
				}
				fw.write(line + "\n");
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			System.err.println("Failed to write wpa supplication configuration: " + e);
		}
	}
}
