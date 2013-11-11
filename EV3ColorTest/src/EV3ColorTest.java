import lejos.utility.TextMenu;
import lejos.hardware.Button;
import lejos.hardware.LCD;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class EV3ColorTest {
    
        static void displayColor(String name, int raw, int calibrated, int line)
        {
            LCD.drawString(name, 0, line);
            LCD.drawInt(raw, 5, 6, line);
            LCD.drawInt(calibrated, 5, 11, line);
        }
        
        static SensorModes getSensor(int typ, Port p)
        {
            // TODO: Can probably do this using reflection, or Class, but now very late!
            switch(typ)
            {
            case 0:
                return new NXTLightSensor(p);
            case 1:
                return new NXTColorSensor(p);
            case 2:
                return new EV3ColorSensor(p);
            case 3:
            	return new HiTechnicColorSensor(p);
            }
            return null;
        }
        

        public static void main(String [] args) throws Exception
        {
            
            String ports[] = {"Port 1", "Port 2", "Port 3", "Port 4"};
            TextMenu portMenu = new TextMenu(ports, 1, "Sensor port");
            // TODO: Work out the sensor type automatically
            String sensors[] = {"NXT Light", "NXT Color", "EV3 Color", "HiTechnic Color"};
            TextMenu sensorMenu = new TextMenu(sensors, 1, "Sensor type");
            int colors[] = {Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.NONE};
            String colorNames[] = {"None", "Red", "Green", "Blue", "Yellow",
                                    "Megenta", "Orange", "White", "Black", "Pink",
                                    "Grey", "Light Grey", "Dark Grey", "Cyan", "Brown"};

            int portNo = portMenu.select();
            if (portNo < 0) return;
            int sensorType = sensorMenu.select();
            if (sensorType < 0) return;
            Port p = LocalEV3.get().getPort("S"+(portNo+1));
            SensorModes sensor = getSensor(sensorType, p);
            for(;;)
            {
                TextMenu modeMenu = new TextMenu(sensor.getAvailableModes().toArray(new String[1]), 1, "Color mode");
                int mode = modeMenu.select();
                if (mode < 0) return;
                SensorMode sm = sensor.getMode(mode);
                LCD.clear();
                while (!Button.ESCAPE.isDown())
                {
                    LCD.drawString("Mode: " + sm.getName(), 0, 0);
                    int sampleSize = sm.sampleSize();
                    LCD.drawString("Samples:" + sampleSize, 0, 1);
                    float samples[] = new float[sampleSize];
                    sm.fetchSample(samples, 0);
                    for(int i = 0; i < sampleSize; i++)
                    {
                        LCD.drawString("Val[" + i + "]: " + samples[i], 2, i+2);
                    }
                    LCD.refresh();
                    Delay.msDelay(100);
                }
                LCD.clear();
                while(Button.ESCAPE.isDown())
                    Delay.msDelay(10);
            }
        }
    }
