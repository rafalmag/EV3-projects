import static java.lang.System.out;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.DeviceManager;
import lejos.hardware.port.I2CPort;
import lejos.hardware.sensor.I2CSensor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.UARTPort;

public class MonitorSensors {
    HashMap<String,String> sensorClasses = new HashMap<String,String>();
      
    // Monitor the sensor ports
    void monitorSensorPorts()
    {
    	sensorClasses.put("mndsnsrsNRLink","lejos.nxt.addon.RCXLink");
    	sensorClasses.put("mndsnsrsACCL3X03","lejos.nxt.addon.AccelMindSensor");
    	sensorClasses.put("HiTechncColor   ","lejos.nxt.addon.ColorHTSensor");
    	sensorClasses.put("HiTechncIRLink  ","lejos.nxt.addon.IRLink");
    	sensorClasses.put("HiTechncCompass ","lejos.nxt.addon.CompassHTSensor");
    	sensorClasses.put("NXT Color","lejos.nxt.ColorSensor");
    	// Use LightSensor class for NXT dumb sensors
    	sensorClasses.put("NXT Dumb","lejos.nxt.LightSensor");
    	// Use TouchSensor for EV3 dumb sensors
    	sensorClasses.put("Dumb","lejos.nxt.TouchSensor");
    	sensorClasses.put("LEGOSonar","lejos.nxt.UltrasonicSensor");
    	sensorClasses.put("IR-PROX","lejos.nxt.EV3IRSensor");
    	sensorClasses.put("COL-REFLECT","lejos.nxt.EV3ColorSensor");
    	
        int [] current = new int[DeviceManager.PORTS];

        Port[] port = {SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4};
        DeviceManager dm = new DeviceManager();
        
        while(true) {
            // Look for changes
            for(int i = 0; i < DeviceManager.PORTS; i++) {
                int typ = dm.getPortType(i);
                if (current[i] != typ) {
                    out.println("Port " + i + " changed to " + dm.getPortTypeName(typ));
                    current[i] = typ;

                    if (typ == DeviceManager.CONN_INPUT_UART) {
                        out.println("Open port " + i);
                        UARTPort u = port[i].open(UARTPort.class);
                        String modeName = u.getModeName(0);
                        if (modeName.indexOf(0) >= 0)modeName = modeName.substring(0, modeName.indexOf(0));
                        out.println("Uart sensor: " + modeName);
                        String className = sensorClasses.get(modeName);
                        out.println("Sensor class for " + modeName + " is " + className);
                        callGetMethods(className, UARTPort.class, u);
                        u.close();
                    } else if (typ == DeviceManager.CONN_NXT_IIC){
                        I2CPort ii = port[i].open(I2CPort.class);
                        I2CSensor s = new I2CSensor(ii);
                        String product = s.getProductID();
                        String vendor = s.getVendorID();
                        s.close();
                        out.println("I2c sensor: " + vendor + " " + product);
                        String className = sensorClasses.get(vendor + product);
                        out.println("Sensor class for " + vendor + product + " is " + className);
                        if (className == null) {
                        	out.println("Cannot find sensor class, using I2CSensor");
                        	className = "lejos.nxt.I2CSensor";
                        }
                        callGetMethods(className, I2CPort.class, ii);
                        ii.close();
                    } else if (typ != DeviceManager.CONN_NONE  && typ != DeviceManager.CONN_ERROR) {       	
                    	String key = dm.getPortTypeName(typ);
                		AnalogPort a = port[i].open(AnalogPort.class);
                		String className = sensorClasses.get(key);
                		out.println("Sensor class is " + className);
                		callGetMethods(className,AnalogPort.class, a);
                		a.close();
                    } 
                }
            }
        }
    }
    
    // Construct an instance of the class with a single parameter, and call its parameterless get and is methods
    private void callGetMethods(String className, Class<?> paramClass, Object param) {
    	if (className != null) {
        	Class<?> c;
        	
        	try {
				c = Class.forName(className);
				Class<?>[] params = new Class<?>[1];
				params[0] = paramClass;
				Constructor<?> con = c.getConstructor(params);
				Object[] args = new Object[1];
				args[0] = param;
				Object o = con.newInstance(args);
				out.println("Calling get methods for " + className);
				callGetMethods(c, o);
			} catch (Exception e) {
				e.printStackTrace();
			} 
        }
    }
    
    // Call the parameterless get and is methods of the instance of the class
    void callGetMethods(Class<?> c, Object o) {
		try {	    
		    Method[] allMethods = c.getDeclaredMethods();
		    for (Method m : allMethods) {
		        if (!m.getName().startsWith("get") && !m.getName().startsWith("is")) continue;
		        Class<?>[] pType  = m.getParameterTypes();
		        if (pType.length > 0) continue;

		        if (o != null) {
			        out.println("Invoking " + m.toGenericString());
		        	Object res = m.invoke(o, (Object[]) null);
		        	if (res.getClass().isArray()) {
		        		for(int i=0;i<Array.getLength(res);i++) {
		        			out.println("Element " + i + " is " + Array.get(res, i));
		        		}
		        	} else out.println("Result is " + res);
		        }
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
    }

    public static void main(String[] args) {
    	new MonitorSensors().monitorSensorPorts();
    }
}
