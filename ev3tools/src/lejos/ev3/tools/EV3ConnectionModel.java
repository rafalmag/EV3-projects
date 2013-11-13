package lejos.ev3.tools;

import javax.swing.table.AbstractTableModel;

/**
 * A table model for PC GUI programs.
 * This is used by NXJBrowser and NXJMonitor to allow the user to choose
 * which EV3 to connect to.
 * 
 * @author Lawrie Griffiths
 *
 */
public class EV3ConnectionModel extends AbstractTableModel {
  private static final long serialVersionUID = -2658444314094350609L;
  private static final String[] columnNames = {"Name","Protocol", "Address","Status"};
  private static final int NUM_COLUMNS = 4;
	
  private Object[][] EV3Data;
  private int numEV3s;

  /**
   * Create the model from an array of EV3Info.
   * 
   * @param EV3s the EV3Info array
   * @param numEV3s the number of EV3s in the array
   */
  public EV3ConnectionModel(EV3Info[] EV3s, int numEV3s) {
    setData(EV3s, numEV3s);
  }

  /**
   * Update the data in the model.
   * 
   * @param EV3s the EV3Info array
   * @param numEV3s the number of EV3s
   */
  public void setData(EV3Info[] EV3s, int numEV3s) {
    this.numEV3s = numEV3s;
    EV3Data = new Object[numEV3s][NUM_COLUMNS];

    for(int i=0;i<numEV3s;i++) {
      EV3Data[i][0]  = EV3s[i].getName();
      EV3Data[i][1] = "";
      EV3Data[i][2] = EV3s[i].getIPAddress();
      EV3Data[i][3] = EV3ConnectionState.DISCONNECTED;
    }
  }
  
  public void setConnected(int row, EV3ConnectionState state) {
	  EV3Data[row][3] = state;
  }
 

  /**
   * Return the number of rows
   * @return the number of rows
   */
  public int getRowCount() {
    return numEV3s;
  }

  /**
   * Return the number of columns
   * @return the number of columns
   */
  public int getColumnCount() {
    return NUM_COLUMNS;
  }

  /**
   * Get the data in a specific cell
   * @return the Object from the specific cell
   */
  public Object getValueAt(int row, int column) {
    return EV3Data[row][column];
  }

  /**
   * Get the column name
   * @param column the column index
   * @return the column name
   */
  public String getColumnName(int column) {
    return columnNames[column];
  }

  /** 
   * Get the class of the object held in the column cells
   * @param column the column index
   * @return the class
   */
  public Class<?> getColumnClass(int column) {
    return EV3Data[0][column].getClass();
  }
}


