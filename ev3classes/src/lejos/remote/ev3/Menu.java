package lejos.remote.ev3;

public interface Menu {
	
	public int runProgram(String programName);
	
	public int runSample(String programName);
	
	public int debugProgram(String programName);
	
	public int deleteFile(String fileName);
	
	public long getFileSize(String filename);
	
	public String[] getProgramNames();
	
	public String[] getSampleNames();

	public int uploadFile(String fileName, byte[] contents);
	
	public byte[] fetchFile(String fileName);
	
	public String getSetting(String setting);
	
	public boolean setSetting(String setting, String value);
}
