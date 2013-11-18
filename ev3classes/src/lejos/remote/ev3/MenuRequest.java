package lejos.remote.ev3;

public class MenuRequest {
	
	public enum Request {
		RUN_PROGRAM,
		RUN_SAMPLE,
		DEBUG_PROGRAM,
		DELETE_FILE,
		GET_FILE_SIZE,
		GET_PROGRAM_NAMES,
		GET_SAMPLE_NAMES,
		UPLOAD_FILE,
		FETCH_FILE,
		GET_SETTING,
		SET_SETTING,
		DELETE_ALL_PROGRAMS,
		GET_VERSION,
		GET_MENU_VERSION,
		GET_NAME,
		SET_NAME
	}
	
	public Request request;
	
	public String name, value;
	
	public boolean replyRequired = false;
	
	public byte[] contents;
}
