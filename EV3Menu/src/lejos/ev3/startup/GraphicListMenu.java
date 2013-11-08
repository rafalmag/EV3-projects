package lejos.ev3.startup;

import lejos.hardware.LCD;

/**
 * Abrams version of a more detailed GraphicMenu for the file menu.
 * 
 * @author Abram Early
 *
 */
public class GraphicListMenu extends GraphicMenu {
	
	public GraphicListMenu(String[] items, String[] icons) {
		super(items, icons, -1);
	}
	
	@Override
	protected void animate(int selectedIndex, int finalIndex, int animateDirection)
	{
		this.display(finalIndex, animateDirection, 0);
	}
	
	@Override
	protected void display(int selectedIndex, int animateDirection, int tick)
	{
		if(_title != null) {
			System.out.println("Displaying title " + _title + " on line " + (_topRow - 1));
			LCD.drawString(_title, 0, _topRow - 1);
		}
			
		int max = _topRow + _height;
		for (int i = _topRow; i < max; i++){
			LCD.drawString(BLANK, 0, i);
			int idx = i - _topRow + _topIndex;
			if (idx >= 0 && idx < _length){
				LCD.drawChar(idx == selectedIndex ? SEL_CHAR : ' ', 0, i);
				LCD.drawString(_items[idx], 3, i);
			}
		}
		LCD.asyncRefresh();
	}
	@Override
	public void clearArea(){
		LCD.bitBlt(null, 30, 100, 0, 0, 0, 16, 30, 100, LCD.ROP_CLEAR);
	}
	
	@Override
	protected boolean get2IconMode(){return true;} // Wrap With 2 Icons
}
