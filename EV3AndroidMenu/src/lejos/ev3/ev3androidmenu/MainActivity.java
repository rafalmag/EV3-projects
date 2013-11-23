package lejos.ev3.ev3androidmenu;

import java.util.Locale;

import lejos.ev3.ev3androidmenu.R;
import lejos.hardware.Sound;
import lejos.remote.ev3.RemoteRequestMenu;

import android.R.drawable;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements EditSettingDialog.EditSettingDialogListener {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private static RemoteRequestMenu menu;
	
	public static final String TAG = "EV3AndroidMenu";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Bundle b = getIntent().getExtras();
		
		String address = b.getString("address");
		
		Log.d(TAG, "Start main activity with address: " + address);
		// Allow network access in main thread, until I decide on a better solution
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		Log.d(TAG, "Starting menu");
		
		// Create the remote menu 
		if (menu == null) {
			try {
				menu = new RemoteRequestMenu(address);
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Failed to create menu" + e, Toast.LENGTH_LONG).show();
			}
		}
		
		Log.d(TAG, "Started menu");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public void onFinishEditDialog(String setting, String value) {
		if (value.length() > 0) {
			menu.setSetting(setting, value);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new Fragment();
			switch (position) {
			case 0:
				return fragment = new ProgramsFragment();
			case 1:
				return fragment = new SamplesFragment();
			case 2:
				return fragment = new SettingsFragment();
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class ProgramsFragment extends Fragment {
		private int selectedRow = -1;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.programs_layout, container, false);
			
			Button deleteAllFilesButton = (Button) rootView.findViewById(R.id.deleteAllFilesButton);
			if (deleteAllFilesButton != null) {
				deleteAllFilesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						menu.deleteAllPrograms();		
					}
				});
			}
					
		    refreshTable((TableLayout) rootView.findViewById(R.id.tableLayout1),container.getContext(), rootView);
			return rootView;
		}
		
		private void refreshTable(final TableLayout ll, final Context context, final View rootView) {			
			final String[] programs = menu.getProgramNames();
			final TableRow[] rows = new TableRow[programs.length];
			final CheckBox[] boxes = new CheckBox[programs.length];
			
			// Remove all rows other than header
			int l = ll.getChildCount();
			if (l > 1) ll.removeViewsInLayout(1,l-1);
			
		    for (int i = 0; i <programs.length; i++) {
		        final TableRow row= new TableRow(context);
		        rows[i] = row;
		        row.setBackgroundResource(drawable.screen_background_light_transparent);
		        row.setOnClickListener(new OnClickListener() {
		            @Override
		             public void onClick(View v) {
		            	for(int i=0;i<rows.length;i++) {
		            		rows[i].setBackgroundResource(drawable.screen_background_light_transparent);
		            		if (row == rows[i]) selectedRow = i;
		            	}
		            	row.setBackgroundResource(drawable.screen_background_dark_transparent);
		            	Toast.makeText(context, "Selected row: " + selectedRow, Toast.LENGTH_LONG).show();
		             }   
		        });
		        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
		        row.setLayoutParams(lp);
		        TextView tv1 = new TextView(context);
		        tv1.setText(programs[i]);
		        row.addView(tv1);
		        TextView tv2 = new TextView(context);
		        tv2.setText("" + menu.getFileSize("/home/lejos/programs/" + programs[i]));
		        row.addView(tv2);
		        CheckBox cb = new CheckBox(context);
		        boxes[i] = cb;
		        row.addView(cb);
		        ll.addView(row);
		    }
		   
			Button setDefaultButton = (Button) rootView.findViewById(R.id.setDefaultButton);
			if (setDefaultButton != null) {
				setDefaultButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectedRow < 0) Toast.makeText(context, "No file selected", Toast.LENGTH_LONG).show();
						else menu.setSetting("lejos.default_program", "/home/lejos/programs" + programs[selectedRow]);
					}
				});
			}
			
			Button runButton = (Button) rootView.findViewById(R.id.runProgramButton);
			if (runButton != null) {
				runButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectedRow < 0) Toast.makeText(context, "No file selected", Toast.LENGTH_LONG).show();
						else menu.runProgram(programs[selectedRow]);
					}
				});
			}
			
			Button deleteFilesButton = (Button) rootView.findViewById(R.id.deleteFilesButton);
			if (deleteFilesButton != null) {
				deleteFilesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for(int i=0;i<programs.length;i++) {
							if (boxes[i].isChecked()) menu.deleteFile(programs[i]);
						}
						refreshTable(ll, context, rootView);
					}
				});
			}			
		}
	}
	
	public static class SamplesFragment extends Fragment {
		private int selectedRow = -1;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.samples_layout, container, false);			
			refreshTable((TableLayout) rootView.findViewById(R.id.tableLayout2),container.getContext(),rootView);
			return rootView;
		}
		
		private void refreshTable(TableLayout ll, final Context context, View rootView) {
			final String[] samples = menu.getSampleNames();
			
			// Remove all rows other than header
			int l = ll.getChildCount();
			if (l > 1) ll.removeViewsInLayout(1,l-1);
			
			final TableRow[] rows = new TableRow[samples.length];
		    for (int i = 0; i <samples.length; i++) {
		        final TableRow row= new TableRow(context);
		        rows[i] = row;
		        row.setBackgroundResource(drawable.screen_background_light_transparent);
		        row.setOnClickListener(new OnClickListener() {
		            @Override
		             public void onClick(View v) {
		            	for(int i=0;i<rows.length;i++) {
		            		rows[i].setBackgroundResource(drawable.screen_background_light_transparent);
		            		if (row == rows[i]) selectedRow = i;
		            	}
		            	row.setBackgroundResource(drawable.screen_background_dark_transparent);
		            	//Toast.makeText(context, "Selected row: " + selectedRow, Toast.LENGTH_LONG).show();
		             }   
		        });
		        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
		        row.setLayoutParams(lp);
		        TextView tv1 = new TextView(context);
		        tv1.setText(samples[i]);
		        row.addView(tv1);
		        TextView tv2 = new TextView(context);
		        tv2.setText("" + menu.getFileSize("/home/root/lejos/samples/" + samples[i]));
		        row.addView(tv2);
		        ll.addView(row);
		    }
		    
			Button runButton = (Button) rootView.findViewById(R.id.runSampleButton);
			if (runButton != null) {
				runButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selectedRow < 0) Toast.makeText(context, "No file selected", Toast.LENGTH_LONG).show();
						else menu.runSample(samples[selectedRow].replace(".jar", ""));		
					}
				});
			}
		}
	}
	
	public static class SettingsFragment extends Fragment {
		private int selectedRow = -1;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.settings_layout, container, false);	
		    refreshTable((TableLayout) rootView.findViewById(R.id.tableLayout3), container.getContext(), rootView);
			return rootView;
		}
		
		private void refreshTable(final TableLayout ll, final Context context, final View rootView) {
			final String[] settings = {Sound.VOL_SETTING, lejos.hardware.Button.VOL_SETTING, lejos.hardware.Button.FREQ_SETTING, 
					lejos.hardware.Button.LEN_SETTING, "lejos.default_program", "lejos.default_autorun", "lejos.sleep_time",
					"lejos.bluetooth_pin"};
			
			// Remove all rows other than header
			int l = ll.getChildCount();
			if (l > 1) ll.removeViewsInLayout(1,l-1);
			
			final TableRow[] rows = new TableRow[settings.length];
			
		    for (int i = 0; i <settings.length; i++) {
		        final TableRow row= new TableRow(context);
		        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
		        row.setLayoutParams(lp);
		        rows[i] = row;
		        row.setBackgroundResource(drawable.screen_background_light_transparent);
		        row.setOnClickListener(new OnClickListener() {
		            @Override
		             public void onClick(View v) {
		            	for(int i=0;i<rows.length;i++) {
		            		rows[i].setBackgroundResource(drawable.screen_background_light_transparent);
		            		if (row == rows[i]) selectedRow = i;
		            	}
		            	row.setBackgroundResource(drawable.screen_background_dark_transparent);
		                FragmentManager fm =getFragmentManager();
		                EditSettingDialog editSettingDialog = new EditSettingDialog();
		                try {
			                editSettingDialog.setSetting(settings[selectedRow], menu.getSetting(settings[selectedRow]));
			                editSettingDialog.show(fm, "edit_setting_layout");
			                refreshTable(ll, context, rootView);
		                } catch (Exception e) {
		                	Toast.makeText(context, "Exception in settings dialog: " + e, Toast.LENGTH_LONG).show();
		                }
		             }   
		        });
		        TextView tv1 = new TextView(context);
		        String setting = menu.getSetting(settings[i]);
		        tv1.setText(settings[i]);
		        row.addView(tv1);
		        TextView tv2 = new TextView(context);
		        tv2.setText((setting == null ? "Not set" : setting));
		        row.addView(tv2);
		        ll.addView(row);
		    }			
		}

	}
}