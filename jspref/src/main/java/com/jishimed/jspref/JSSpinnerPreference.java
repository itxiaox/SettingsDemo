package com.jishimed.jspref;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class JSSpinnerPreference extends JSPreference<Spinner, String> {

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    public JSSpinnerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public JSSpinnerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSSpinnerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSSpinnerPreference(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JSSpinnerPreference);
        mEntries = a.getTextArray(R.styleable.JSSpinnerPreference_entries);
        mEntryValues = a.getTextArray(R.styleable.JSSpinnerPreference_entryValues);
        a.recycle();

        //Must set mEntries before createWidget in super.init()
        super.init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setEntries(CharSequence[] entries) {
        mEntries = entries;
        Spinner spinner = getWidget();
        if (spinner != null && mEntries != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, mEntries);
            spinner.setAdapter(adapter);
        }
    }

    public void setEntryValues(String[] mEntryValues) {
        this.mEntryValues = mEntryValues;
    }

    public void setValueIndex(int index) {
        if (mEntryValues != null) {
            if (index >= 0 && index < mEntryValues.length) {
                setValue((String)mEntryValues[index]);
            }
        }
        else {
            setValue(Integer.toString(index));
        }
    }

    public int getValueIndex() {
        return findIndexOfValue(getValue());
    }

    private int findIndexOfValue(String value) {
        int ret = -1;
        if (value != null) {
            if (mEntryValues != null) {
                for (int i = mEntryValues.length - 1; i >= 0; i--) {
                    if (value.contentEquals(mEntryValues[i])) {
                        ret = i;
                        break;
                    }
                }
            }
            else {
                try {
                    ret = Integer.parseInt(value);
                }
                catch (NumberFormatException e) {
                    ret = -1;
                }
            }
        }
        return ret;
    }

    @Override
    protected int _getWidgetLayoutResId() {
        return R.layout.preference_widget_spinner;
    }

    @Override
    protected Spinner createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new Spinner(context, attrs, android.R.attr.dropDownSpinnerStyle);
    }

    @Override
    protected void initWidget(Spinner widget) {
        widget.setMinimumWidth(mWidgetWidth);
        widget.setFocusable(false);  //Make the preference clickable.

        if (mEntries != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, mEntries);
            widget.setAdapter(adapter);
        }

        widget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setValueIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected boolean handleClick(Spinner widget) {
        int pos = widget.getSelectedItemPosition();
        widget.setSelection((pos+1) % widget.getCount());
        return true;
    }

    @Override
    protected void onUpdateWidget(Spinner widget, String value, View view) {
        int pos = findIndexOfValue(value);
        if (pos >= 0 && pos < widget.getCount()) {
            widget.setSelection(pos);
        }
    }

    @Override
    protected String getPersistedValue(String defaultValue) {
        return getPersistedString(defaultValue);
    }

    @Override
    protected String parseString(String value) {
        return value;
    }
}
