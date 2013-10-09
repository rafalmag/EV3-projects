package lejos.internal.io;
import java.nio.Buffer;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class NativeHCI {
    static class LibBlue {
        native public int hci_get_route(Pointer addr) throws LastErrorException;
        
        native public int hci_open_dev(int dev_id) throws LastErrorException;
        
        native public int hci_inquiry(int dev_id, int len, int num_rsp, Pointer lap, PointerByReference ii, long flags) throws LastErrorException;
        
        native public int hci_send_cmd(int sock, short ogf, short ocf, byte plen, Buffer param) throws LastErrorException;

        native public  int hci_read_remote_name(int dd, byte[] bdaddr, int len, Buffer name, int to) throws LastErrorException;
        
        static {
            try {
                Native.register("bluetooth");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static LibBlue blue = new LibBlue();
    
	public static final int AF_UNIX = 1;
	public static final int SOCK_STREAM = 1;
	public static final int SOCK_RAW = 3;
	
	public static final int PROTOCOL = 0;
	
	public static final int AF_BLUETOOTH = 31;
	
	public static final int BTPROTO_RFCOMM = 3;
	public static final int BTPROTO_HCI = 1;
	
	public static final int SCAN_DISABLED = 0x00;
	public static final int SCAN_INQUIRY = 0x01;
	public static final int SCAN_PAGE = 0x02;
	
	public static final short OGF_HOST_CTL = 0x03;
	public static final short OCF_WRITE_SCAN_ENABLE = 0x001A;
	
	public void hciOpenDevice(int deviceId) throws LastErrorException {
		blue.hci_open_dev(deviceId);
	}
	
	public int hciGetRoute() throws LastErrorException {
		return blue.hci_get_route(null);
	}

}
