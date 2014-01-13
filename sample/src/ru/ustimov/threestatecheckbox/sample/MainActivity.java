package ru.ustimov.threestatecheckbox.sample;

import ru.ustimov.threestatecheckbox_sample.R;
import ru.ustimov.widget.ThreeStateCheckBox;
import ru.ustimov.widget.ThreeStateCheckBox.OnStateChangeListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity implements OnStateChangeListener {
	
	private ThreeStateCheckBox threeStateCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		threeStateCheckBox = (ThreeStateCheckBox) findViewById(R.id.threeStateCheckBox);
		threeStateCheckBox.setOnStateChangeListener(this);
	}
	
	public void buttonUncheckedClicked(View v) {
		/** 1st option: provide constant value */
		threeStateCheckBox.setState(ThreeStateCheckBox.UNCHECKED);

		/** or 2nd option: provide boolean values.
		 * isAll == true if all objects are selected;
		 * isMultiple = true if at least one object is selected
		 */
		// boolean isAll = false;
		// boolean isMultiple = false;
		// threeStateCheckBox.setState(isAll, isMultiple); 
		
		/** or 3rd option: use native CheckBox interface */
		// threeStateCheckBox.setChecked(false);
	}
	
	public void buttonMultipleClicked(View v) {
		/** 1st option */
		threeStateCheckBox.setState(ThreeStateCheckBox.MULTIPLE);

		/** or 2nd option */
		// boolean isAll = false;
		// boolean isMultiple = true;
		// threeStateCheckBox.setState(isAll, isMultiple);
	}
	
	public void buttonAllClicked(View v) {
		/** 1st option */
		threeStateCheckBox.setState(ThreeStateCheckBox.ALL);

		/** or 2nd option */
		// boolean isAll = true;
		// boolean isMultiple = true;
		// threeStateCheckBox.setState(isAll, isMultiple); 
		
		/** or 3rd option */
		// threeStateCheckBox.setChecked(true);
	}

	@Override
	public void onStateChanged(ThreeStateCheckBox v, int state) {
		String text = "State: ";
		
		if (state == ThreeStateCheckBox.UNCHECKED) {
			text += "ThreeStateCheckBox.UNCHECKED";
			
		} else if (state == ThreeStateCheckBox.MULTIPLE) {
			text += "ThreeStateCheckBox.MULTIPLE";
			
		} else if (state == ThreeStateCheckBox.ALL) {
			text += "ThreeStateCheckBox.ALL";
		}
		
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}

}
