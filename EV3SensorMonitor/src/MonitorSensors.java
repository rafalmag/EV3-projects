import static java.lang.System.out;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import lejos.nxt.DeviceManager;
import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.Port;
import lejos.nxt.SensorPort;
import lejos.nxt.UARTPort;

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
    	sensorClasses.put("NXTColor","lejos.nxt.ColorSensor");
    	sensorClasses.put("LEGOSonar","lejos.nxt.UltrasonicSensor");
    	sensorClasses.put("IR-PROX","lejos.nxt.EV3IRSensor");
    	sensorClasses.put("COL-REFLECT","lejos.nxt.EV3ColorSensor");
    	
        int [] current = new int[DeviceManager.PORTS];
        UARTPort[] uarts = new UARTPort[DeviceManager.PORTS];
        I2CPort[] i2c = new I2CPort[DeviceManager.PORTS];
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
                        uarts[i] = u;
                        String modeName = uarts[i].getModeName(0);
                        if (modeName.indexOf(0) >= 0)modeName = modeName.substring(0, modeName.indexOf(0));
                        out.println("Uart sensor: " + modeName);
                        String className = sensorClasses.get(modeName);
                        out.println("Sensor class for " + modeName + " is " + className);
                        callGetMethods(className, UARTPort.class, u);
                    } else if (typ == DeviceManager.CONN_NXT_IIC){
                        I2CPort ii = port[i].open(I2CPort.class);
                        i2c[i] = ii;
                        I2CSensor s = new I2CSensor(i2c[i]);
                        String product = s.getProductID();
                        String vendor = s.getVendorID();
                        s.close();
                        out.println("I2c sensor: " + vendor + " " + product);
                        String className = sensorClasses.get(vendor + product);
                        out.println("Sensor class for " + vendor + product + " is " + className);
                        callGetMethods(className, I2CPort.class, ii);
                    } else {       	
                    	if (typ == DeviceManager.CONN_NXT_COLOR) {
                    		String className = sensorClasses.get("NXTColor");
                    		out.println("Sensor class is " + className);
                    		// TODO: Call methods of analog port
                    	}
                        if (uarts[i] != null) uarts[i].close();
                        if (i2c[i] != null) i2c[i].close();
                        uarts[i] = null;
                        i2c[i] = null;
                    } 
                }
            }
        }
    }
    
    // Construct an instance of the class with a single parameter, and call its parameterless get methods
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
    
    // Call the parameterless get methods of the instance of the class
    void callGetMethods(Class<?> c, Object o) {
		try {	    
		    Method[] allMethods = c.getDeclaredMethods();
		    for (Method m : allMethods) {
		        if (!m.getName().startsWith("get")) continue;
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
