package lejos.ev3.ev3androidmenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import lejos.ev3.ev3androidmenu.R;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {
	
    private static final int DEFAULT_PORT = 3016;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Map<String,EV3Info> ev3s = new HashMap<String,EV3Info>();
    private int selectedRow = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Allow network access in main thread, until I decide on a better solution
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
        try {
            socket = new DatagramSocket(DEFAULT_PORT);
        } catch( Exception e) {
        	Toast.makeText(getApplicationContext(), "Failed to create UDP socket: " + e, Toast.LENGTH_LONG).show();
            return;
        }
        
        packet = new DatagramPacket (new byte[100], 100);

        long start = System.currentTimeMillis();
        
        while ((System.currentTimeMillis() - start) < 2000) {
            try {
                socket.receive (packet);
                String message = new String(packet.getData(), "UTF-8");
                String ip = packet.getAddress().getHostAddress();
                //Toast.makeText(getApplicationContext(), "Adding " + ip, Toast.LENGTH_LONG).show();
                ev3s.put(ip, new EV3Info(message.trim(),ip));

            } catch (IOException e) {
            	Toast.makeText(getApplicationContext(), "Failed to read UDP packets: " + e, Toast.LENGTH_LONG).show();
            }
        }
        
        if (socket != null) socket.close();
        
        EV3Info[] devices = new EV3Info[ev3s.size()];
        int i = 0;
        for(String ev3: ev3s.keySet()) {
        	EV3Info info = ev3s.get(ev3);
        	devices[i++] = info;
        	//Toast.makeText(getApplicationContext(), "Found " + info.getName() + " " + info.getIPAddress(), Toast.LENGTH_LONG).show();
        }
        
        try {
        	TableLayout tl = (TableLayout) getLayoutInflater().inflate(R.layout.search_layout, null, false);
	        
	        refreshTable(tl, getApplicationContext(), devices);
			
			setContentView(tl);
        } catch (Exception e) {
        	Toast.makeText(getApplicationContext(), "Failed to refresh table: " + e, Toast.LENGTH_LONG).show();
        }
	}
	
	private void refreshTable(TableLayout ll, final Context context, final EV3Info[] ev3s) {
		final TableRow[] rows = new TableRow[ev3s.length];
		
	    for (int i = 0; i <ev3s.length; i++) {
	        final TableRow row= new TableRow(context);
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        rows[i] = row;
	        row.setBackgroundResource(drawable.screen_background_light_transparent);
	        row.setLayoutParams(lp);
	        row.setOnClickListener(new OnClickListener() {
	            @Override
	             public void onClick(View v) {
	            	for(int i=0;i<rows.length;i++) {
	            		rows[i].setBackgroundResource(drawable.screen_background_light_transparent);
	            		if (row == rows[i]) selectedRow = i;
	            	}
	            	row.setBackgroundResource(drawable.screen_background_dark_transparent);
	            	try {
	            		Intent intent = new Intent(Search.this.getApplicationContext(), MainActivity.class);
	            		Bundle b = new Bundle();
	            		b.putString("address", ev3s[selectedRow].getIPAddress());
	            		intent.putExtras(b);
	            		startActivity(intent);
	            		finish();
	            	} catch (Exception e) {
	            		Toast.makeText(getApplicationContext(), "Failed to start main activity: " + e, Toast.LENGTH_LONG).show();
	            	}
	             }   
	        });
	        TextView tv1 = new TextView(context);
	        tv1.setText(ev3s[i].getName());
	        row.addView(tv1);
	        TextView tv2 = new TextView(context);
	        tv2.setText(ev3s[i].getIPAddress());
	        row.addView(tv2);
	        ll.addView(row);
	    }			
	}
	
}
