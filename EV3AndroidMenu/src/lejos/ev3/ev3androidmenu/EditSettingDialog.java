package lejos.ev3.ev3androidmenu;

import lejos.ev3.ev3androidmenu.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditSettingDialog extends DialogFragment implements OnEditorActionListener {
	public static final String TAG = "EV3AndroidMenu";
	private String setting;
	private String value;
	
    public interface EditSettingDialogListener {
        void onFinishEditDialog(String setting, String value);
    }

    private EditText mEditText;

    public EditSettingDialog() {
        // Empty constructor required for DialogFragment
    }
    
    public void setSetting(String setting, String value) {
    	this.setting = setting;
    	this.value = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_setting_layout, container);
        mEditText = (EditText) view.findViewById(R.id.txt_setting);
        if (value != null) mEditText.setText(value);
        getDialog().setTitle("Edit " + setting);
        
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
        	try {
	            EditSettingDialogListener activity = (EditSettingDialogListener) getActivity();
	            activity.onFinishEditDialog(setting, mEditText.getText().toString());
        	} catch (Exception e) {
        		Log.e(TAG,"Failed to call listener: " + e);
        		//Toast.makeText(container.getContext(), "Failed to call listener: " + e, Toast.LENGTH_SHORT).show();
        	}
            this.dismiss();
            return true;
        }
        return false;
	}
}
