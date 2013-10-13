package lejos.remote.ev3;

public interface Menu {
	
	public int runProgram(String programName);
	
	public int runSample(String programName);
	
	public int debugProgram(String programName);
	
	public int deleteFile(String fileName);
	
	public String[] getProgramNames();
	
	public String[] getSampleNames();

}
