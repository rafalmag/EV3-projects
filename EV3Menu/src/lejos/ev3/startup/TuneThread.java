package lejos.ev3.startup;

import lejos.hardware.Sound;

/**
 * Thread to play the tune
 */
public class TuneThread extends Thread
{
    int stState = 0;
	
	@Override
	public void run()
	{
		Utils.fadeIn();
		this.waitState(1);
		playTune();
		// Tell others, that tune is complete
		this.setState(2);
        // Wait for init to complete
        this.waitState(3);
        // Fade in
        Utils.fadeIn();
	}
	
	/**
	 * Set the current state
	 */
    public synchronized void setState(int s)
    {
    	this.stState = s;
    	this.notifyAll();
    }
    
    /**
     * Wait for a specific state
     */
    public synchronized void waitState(int s)
    {
    	while (this.stState < s)
    	{
    		try
    		{
    			this.wait();
    		}
    		catch (InterruptedException e)
    		{
    			// nothing
    		}
    	}
    }
    
    /**
     * Play the leJOS startup tune.
     */
    static void playTune()
    {
        int[] freq = { 523, 784, 659 };
        for (int i = 0; i < 3; i++) {
            Sound.playTone(freq[i], 300);
            Sound.pause(300);
        }
    }
}
