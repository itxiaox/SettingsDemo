package com.jishimed.jspref;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

@SuppressWarnings("WeakerAccess")
abstract class JSPreference<T extends View, VType> extends Preference
        implements IPreference, Preference.OnPreferenceClickListener
{
    private static final String TAG = "JSPreference";

    private T mWidget;
    private String mDefaultValue;
    private VType mValue;
    private boolean mValueSet;

    private boolean needComplete;
    private boolean needNotify;
    private boolean mFromWidgetLayout;
    private int mFlag;  //bit 0: Engineering mode; bit 1: Development mode

    int mWidgetWidth;

    JSPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
   }

    JSPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    JSPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    JSPreference(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JSPreference);
        needComplete = a.getBoolean(R.styleable.JSPreference_needComplete,false);
        needNotify = a.getBoolean(R.styleable.JSPreference_needNotify,false);
        boolean fromLayout = a.getBoolean(R.styleable.JSPreference_fromWidgetLayout,false);
        mFlag = a.getInt(R.styleable.JSPreference_flag,0);
        a.recycle();

        mWidgetWidth = Math.round(context.getResources().getDimension(R.dimen.widget_width));
        setFromWidgetLayout(context, attrs, fromLayout);
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean setFromWidgetLayout(boolean fromWidgetLayout) {
        return setFromWidgetLayout(getContext(), null, fromWidgetLayout);
    }

    @Override
    public boolean isFromWidgetLayout() {
        return mFromWidgetLayout;
    }

    private boolean setFromWidgetLayout(Context context, AttributeSet attrs, boolean fromWidgetLayout) {
        int widgetLayoutResId = _getWidgetLayoutResId();
        boolean newFromLayout = (widgetLayoutResId != 0) && fromWidgetLayout;
        if (newFromLayout) {
            mWidget = null;
            setWidgetLayoutResource(widgetLayoutResId);
        }
        else {
            setWidgetLayoutResource(0);
            mWidget = createWidget(context, attrs, 0, 0);
            mWidget.setId(R.id.widget_item);
            mWidget.setEnabled(true);
            initWidget(mWidget);
        }
        mFromWidgetLayout = newFromLayout;
        return true;
    }

    @Override
    public boolean isNeedNotify() {
        return needNotify;
    }

    @Override
    public boolean isNeedComplete() {
        return needComplete;
    }

    @Override
    public int getFlag() {
        return mFlag;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }

    public void setNeedComplete(boolean needComplete) {
        this.needComplete = needComplete;
    }

    public T getWidget() {
        return mWidget;
    }

    public VType getValue() {
        return mValue;
    }

    public void setValue(VType value) {
        // Always persist/notify the first time.
        final boolean changed = (mValue == null || !mValue.equals(value));
        if (changed || !mValueSet) {
            mValue = value;
            mValueSet = true;
            persistValue(mValue);
            if(changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    protected abstract T createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);
    protected abstract boolean handleClick(T widget);
    protected abstract void onUpdateWidget(T widget, VType value, View view);
    protected abstract VType parseString(String value);
    protected abstract VType getPersistedValue(String defaultValue);
    protected abstract void initWidget(T widget);

    protected void persistValue(VType value) {
        if (value instanceof String) {
            persistString((String)value);
        }
        else if (value instanceof Boolean) {
            persistBoolean((Boolean)value);
        }
        else if (value instanceof Integer) {
            persistInt((Integer)value);
        }
        else if (value instanceof Float) {
            persistFloat((Float)value);
        }
        else if (value instanceof Long) {
            persistLong((Long)value);
        }
        else {
            Log.e(TAG, "Unknown Type in persistValue");
        }
    }
    protected int _getWidgetLayoutResId() {
        return 0;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout = super.onCreateView(parent);

        final ViewGroup widgetFrame = layout.findViewById(android.R.id.widget_frame);
        if (widgetFrame != null) {
            //int width = Math.round(getContext().getResources().getDimension(R.dimen.widget_width));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            widgetFrame.setLayoutParams(params);
            //widgetFrame.setMinimumWidth(width);
            //mWidget.setMinimumWidth(width);

            if (mFromWidgetLayout) {
                mWidget = widgetFrame.findViewById(R.id.widget_item);
                initWidget(mWidget);
            }
            else {
                ViewParent oldFrame = mWidget.getParent();
                if (oldFrame != widgetFrame) {
                    if (null != oldFrame && oldFrame instanceof ViewGroup) {
                        ((ViewGroup) oldFrame).removeView(mWidget);
                    }

                    mWidget.setEnabled(true);
                    //widgetFrame.removeAllViews();
                    widgetFrame.addView(mWidget, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    mWidget.setVisibility(View.VISIBLE);
                    widgetFrame.setVisibility(View.VISIBLE);
                    widgetFrame.setEnabled(true);
                    widgetFrame.requestLayout();
                    widgetFrame.invalidate();
                }
            }
        }

        return layout;
    }

    /*
    @Override
    public View getView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = super.onCreateView(parent);
        }
        onBindView(convertView);
        return convertView;

        if (mWidget == null) {
            return super.getView(convertView, parent);
        }
        else {
            return mWidget;
        }
    }
    */

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        onUpdateWidget(mWidget, mValue, view);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(defaultValue);
        mDefaultValue = (String)defaultValue;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return mWidget != null && handleClick(mWidget);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedValue(mDefaultValue) : parseString((String)defaultValue));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue().toString();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(parseString(myState.value));
    }

    private static class SavedState extends BaseSavedState {
        String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
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
}
