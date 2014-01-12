import java.lang.reflect.Constructor;

import lejos.utility.TextMenu;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.utility.Delay;

public class EV3SensorTest {	
        static final String ports[] = {"Port 1", "Port 2", "Port 3", "Port 4"};
        
        static final String sensors[] = {"EV3 Color",
                                         "EV3 IR",                                     
        	                             "EV3 Gyro",
        	                             "EV3 Touch",
        	                             "NXT Light", 
        	                             "NXT Color",
        	                             "NXT Touch",
        	                             "NXT Sound",
        	                             "NXT Ultrasonic",
        	                             "Cruizcore Gyro",
        	                             "DI Compass",
        	                             "DI IMU",
        	                             "DI Laser",
        	                             "DI Pressure 250",
        	                             "DI Pressure 500",
        	                             "DI Thermal IR",
        	                             "HT Aceleration",
        	                             "HT Color", 
        	                             "HT Gyro",
        	                             "HT Angle",
        	                             "HT Barometer",
        	                             "HT Compass",
        	                             "HT EOPD",
        	                             "HT IRSeeker",
        	                             "HT Magnetic",
        	                             "MS IMU",
        	                             "MS Acceleration",
        	                             "MS Compass",
        	                             "MS Distance",
        	                             "MS Light Array",
        	                             "MS Line Leader",
        	                             "MS Pressure"
        	                            };
        
		static final String[] classes = {
			 "lejos.hardware.sensor.EV3ColorSensor",
			 "lejos.hardware.sensor.EV3IRSensor",
			 "lejos.hardware.sensor.EV3GyroSensor",
			 "lejos.hardware.sensor.EV3TouchSensor",
			 "lejos.hardware.sensor.NXTLightSensor",
			 "lejos.hardware.sensor.NXTColorSensor",
			 "lejos.hardware.sensor.NXTTouchSensor",
			 "lejos.hardware.sensor.NXTSoundSensor",
			 "lejos.hardware.sensor.NXTUltrasonicSensor",
			 "lejos.hardware.sensor.CruizcoreGyro",
			 "lejos.hardware.sensor.DexterCompassSensor",
			 "lejos.hardware.sensor.DexterIMUSensor",
			 "lejos.hardware.sensor.DexterLaserSensor",
			 "lejos.hardware.sensor.DexterPressureSensor250",
			 "lejos.hardware.sensor.DexterPressureSensor500",
			 "lejos.hardware.sensor.DexterThermalIRSensor",
			 "lejos.hardware.sensor.HiTechnicAccelerometer",
			 "lejos.hardware.sensor.HiTechnicColorSensor",
			 "lejos.hardware.sensor.HiTechnicGyro",
			 "lejos.hardware.sensor.HiTechnicAngleSensor",
			 "lejos.hardware.sensor.HiTechnicBarometer",
			 "lejos.hardware.sensor.HiTechnicCompass",
			 "lejos.hardware.sensor.HiTechnicEOPD",
			 "lejos.hardware.sensor.HiTechnicIRSeekerV2",
			 "lejos.hardware.sensor.HiTechnicMagneticSensor",
			 "lejos.hardware.sensor.MindsensorsAbsoluteIMU",
			 "lejos.hardware.sensor.MindsensorsAccelerometer",
			 "lejos.hardware.sensor.MindsensorsCompass",
			 "lejos.hardware.sensor.MindsensorsDistanceSensor",
			 "lejos.hardware.sensor.MindsensorsLihtSensorArray",
			 "lejos.hardware.sensor.MindsensorsLineLeader",
			 "lejos.hardware.sensor.MindsensorsPressureSensor"
			};
    
        static void displayValue(String name, int raw, int calibrated, int line)
        {
            LCD.drawString(name, 0, line);
            LCD.drawInt(raw, 5, 6, line);
            LCD.drawInt(calibrated, 5, 11, line);
        }

        static SensorModes getSensor(int typ, Port p)
        {
        	Class<?> c;
        	
        	try {
				c = Class.forName(classes[typ]);
				Class<?>[] params = new Class<?>[1];
				params[0] = Port.class;
				Constructor<?> con = c.getConstructor(params);
				Object[] args = new Object[1];
				args[0] = p;
				Object o = con.newInstance(args);
				return (SensorModes) o;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
        }
        
        public static void main(String [] args) throws Exception
        {          
            TextMenu portMenu = new TextMenu(ports, 1, "Sensor port");
            TextMenu sensorMenu = new TextMenu(sensors, 1, "Sensor type");

            int portNo = portMenu.select();
            if (portNo < 0) return;
            LCD.clear();
            int sensorType = sensorMenu.select();
            if (sensorType < 0) return;
            Port p = LocalEV3.get().getPort("S"+(portNo+1));
            SensorModes sensor = getSensor(sensorType, p);
            
            for(;;)
            {
            	LCD.clear();
                TextMenu modeMenu = new TextMenu(sensor.getAvailableModes().toArray(new String[1]), 1, "Sensor mode");
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
