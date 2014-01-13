ThreeStateCheckBox
==================

ThreeStateCheckbox is an open source Android library that provides a three state widget component.

### States
1. ThreeStateCheckBox.ALL - all items are selected. `isChecked() == true`. XML: `state_all="true"`
2. ThreeStateCheckBox.MULTIPLE - at least one item is selected. `isChecked() == false`. XML: `state_multiple="true"`
3. ThreeStateCheckBox.UNCHECKED - no one item is selected. `isChecked() = false`. XML: `state_unchecked = "true"`

### Usage
1. Checkout library project to local PC
2. Import library to your IDE and set it as reference to your project
3. Add threeStateCheckboxStyle style in your activity theme
```xml
<style name="AppTheme" parent="AppBaseTheme">
    <item name="threeStateCheckboxStyle">@style/ThreeStateCheckboxStyle</item>
</style>
```

4. Add ThreeStateCheckBox to layout. Set up namespace: `xmlns:app="http://schemas.android.com/apk/res-auto"`
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    ... >
```

```xml
<ru.ustimov.widget.ThreeStateCheckBox
    android:id="@+id/threeStateCheckBox"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:state_unchecked="true"
    android:text="ThreeStateCheckBox" />
```

5. Use ThreeStateCheckBox in your activity or fragment. 
```java
public class MainActivity extends Activity implements OnStateChangeListener {
	
	private ThreeStateCheckBox threeStateCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		threeStateCheckBox = (ThreeStateCheckBox) findViewById(R.id.threeStateCheckBox);
		threeStateCheckBox.setOnStateChangeListener(this);
	}
	
	@Override
	public void onStateChanged(ThreeStateCheckBox v, int state) {
		...
	}
}
```

Please see a simple demo app for more details
