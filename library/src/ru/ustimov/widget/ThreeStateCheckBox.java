/*
 * Copyright (C) 2014 Dmitry Ustimov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.ustimov.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Checkable;

public class ThreeStateCheckBox extends Button implements Checkable {

	public static interface OnStateChangeListener {

		public void onStateChanged(ThreeStateCheckBox v, int state);

	}

	public static final int UNCHECKED = 0;
	public static final int MULTIPLE = -1;
	public static final int ALL = -2;

	private static final int[] UNCHECKED_STATE_SET = { R.attr.state_unchecked };
	private static final int[] MULTIPLE_STATE_SET = { R.attr.state_multiple };
	private static final int[] ALL_STATE_SET = { R.attr.state_all };

	private int mState = UNCHECKED;
	private boolean mBroadcasting;
	private int mButtonResource;
	private Drawable mButtonDrawable;
	private OnStateChangeListener mOnStateChangeListener;
	private OnStateChangeListener mOnStateChangeWidgetListener;

	public ThreeStateCheckBox(Context context) {
		this(context, null);
	}

	public ThreeStateCheckBox(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.threeStateCheckboxStyle);
	}

	public ThreeStateCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ThreeStateCheckbox, defStyle, 0);

		Drawable d = a.getDrawable(R.styleable.ThreeStateCheckbox_button);
		if (d != null) {
			setButtonDrawable(R.drawable.three_state_check_box_holo_light);
		}

		boolean isUnchecked = a.getBoolean(
				R.styleable.ThreeStateCheckbox_state_unchecked, false);
		boolean isMultiple = a.getBoolean(
				R.styleable.ThreeStateCheckbox_state_multiple, false);
		boolean isAll = a.getBoolean(R.styleable.ThreeStateCheckbox_state_all,
				false);

		setState(isAll, isMultiple);

		if (isUnchecked) {
			setState(UNCHECKED);
		}

		a.recycle();
	}


	public int getState() {
		return mState;
	}
	
	public void setState(boolean isAll, boolean isMultiple) {
		if (isAll) {
			setState(ALL);
		} else if (isMultiple) {
			setState(MULTIPLE);
		} else {
			setState(UNCHECKED);
		}
	}

	public void setState(int state) {
		if (mState != state) {
			mState = state;
			refreshDrawableState();

			// Avoid infinite recursions if setChecked() is called from a
			// listener
			if (mBroadcasting) {
				return;
			}
			mBroadcasting = true;
			if (mOnStateChangeListener != null) {
				mOnStateChangeListener.onStateChanged(this, mState);
			}
			if (mOnStateChangeWidgetListener != null) {
				mOnStateChangeWidgetListener.onStateChanged(this, mState);
			}

			mBroadcasting = false;
		}
	}

	public void setOnStateChangeListener(OnStateChangeListener listener) {
		mOnStateChangeListener = listener;
	}

	public void setOnStateChangeWidgetListener(OnStateChangeListener listener) {
		mOnStateChangeWidgetListener = listener;
	}

	public void setButtonDrawable(int resid) {
		if (resid != 0 && resid == mButtonResource) {
			return;
		}

		mButtonResource = resid;

		Drawable d = null;
		if (mButtonResource != 0) {
			d = getResources().getDrawable(mButtonResource);
		}
		setButtonDrawable(d);
	}

	public void setButtonDrawable(Drawable d) {
		if (d != null) {
			if (mButtonDrawable != null) {
				mButtonDrawable.setCallback(null);
				unscheduleDrawable(mButtonDrawable);
			}
			d.setCallback(this);
			d.setVisible(getVisibility() == VISIBLE, false);
			mButtonDrawable = d;
			setMinHeight(mButtonDrawable.getIntrinsicHeight());
		}

		refreshDrawableState();
	}

	@Override
	public int getCompoundPaddingLeft() {
		int padding = super.getCompoundPaddingLeft();
		if (!isLayoutRtl()) {
			final Drawable buttonDrawable = mButtonDrawable;
			if (buttonDrawable != null) {
				padding += buttonDrawable.getIntrinsicWidth();
			}
		}
		return padding;
	}

	@Override
	public int getCompoundPaddingRight() {
		int padding = super.getCompoundPaddingRight();
		if (isLayoutRtl()) {
			final Drawable buttonDrawable = mButtonDrawable;
			if (buttonDrawable != null) {
				padding += buttonDrawable.getIntrinsicWidth();
			}
		}
		return padding;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final Drawable buttonDrawable = mButtonDrawable;
		if (buttonDrawable != null) {
			final int verticalGravity = getGravity()
					& Gravity.VERTICAL_GRAVITY_MASK;
			final int drawableHeight = buttonDrawable.getIntrinsicHeight();
			final int drawableWidth = buttonDrawable.getIntrinsicWidth();

			int top = 0;
			switch (verticalGravity) {
			case Gravity.BOTTOM:
				top = getHeight() - drawableHeight;
				break;

			case Gravity.CENTER_VERTICAL:
				top = (getHeight() - drawableHeight) / 2;
				break;
			}
			int bottom = top + drawableHeight;
			int left = isLayoutRtl() ? getWidth() - drawableWidth : 0;
			int right = isLayoutRtl() ? getWidth() : drawableWidth;

			buttonDrawable.setBounds(left, top, right, bottom);
			buttonDrawable.draw(canvas);
		}
	}

	@SuppressLint("NewApi")
	private boolean isLayoutRtl() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
		} else {
			return false;
		}
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
		switch (mState) {
		case ALL:
			mergeDrawableStates(drawableState, ALL_STATE_SET);
			break;

		case MULTIPLE:
			mergeDrawableStates(drawableState, MULTIPLE_STATE_SET);
			break;

		case UNCHECKED:
			mergeDrawableStates(drawableState, UNCHECKED_STATE_SET);
			break;
		}
		return drawableState;
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();

		if (mButtonDrawable != null) {
			int[] myDrawableState = getDrawableState();

			// Set the state of the Drawable
			mButtonDrawable.setState(myDrawableState);

			invalidate();
		}
	}
	
	@Override
	public boolean isChecked() {
		return mState == ALL;
	}

	@Override
	public void setChecked(boolean checked) {
		setState(checked ? ALL : UNCHECKED);
	}

	@Override
	public void toggle() {
		setChecked((mState == ALL || mState == MULTIPLE) ? false : true);
	}
	
	@Override
	public boolean performClick() {
		toggle();
		return super.performClick();
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || who == mButtonDrawable;
	}

	static class SavedState extends BaseSavedState {

		int state;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			state = (Integer) in.readValue(null);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeValue(state);
		}

		@Override
		public String toString() {
			return "ThreeStateCheckBox.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this))
					+ " state=" + state + "}";
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

	}

	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
		setFreezesText(true);
		Parcelable superState = super.onSaveInstanceState();

		SavedState ss = new SavedState(superState);

		ss.state = getState();
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;

		super.onRestoreInstanceState(ss.getSuperState());
		setState(ss.state);
		requestLayout();
	}

}
