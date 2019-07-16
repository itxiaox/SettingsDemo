package com.jishimed.jspref;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

public class JSSwitchPreference extends JSPreference<Switch, Boolean> {
    private static final String TAG = "JSSwitchPreference";

    private CharSequence mSummaryOn;
    private CharSequence mSummaryOff;

    public JSSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public JSSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.summaryOn, android.R.attr.summaryOff});
        mSummaryOn = a.getText(0);
        mSummaryOff = a.getText(1);
        a.recycle();

        super.init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        Log.i(TAG, "onCreateView: " + parent);
        return super.onCreateView(parent);
    }

    @Override
    protected void onBindView(View view) {
        Log.i(TAG, "onBindView: " + view);
        super.onBindView(view);
    }

    @Override
    protected Switch createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new Switch(context, attrs, android.R.attr.switchStyle);
    }

    @Override
    protected void initWidget(Switch widget) {
        widget.setFocusable(false);  //Make the preference clickable.
        widget.setClickable(false);
        widget.setSwitchMinWidth(mWidgetWidth);
        widget.setGravity(Gravity.CENTER);
    }

    public CharSequence getSummaryOn() {
        return mSummaryOn;
    }

    public void setSummaryOn(CharSequence summaryOn) {
        this.mSummaryOn = summaryOn;
    }

    public CharSequence getSummaryOff() {
        return mSummaryOff;
    }

    public void setSummaryOff(CharSequence summaryOff) {
        this.mSummaryOff = summaryOff;
    }

    @Override
    protected boolean handleClick(Switch widget) {
        if (getValue()) {
            setValue(false);
        }
        else {
            setValue(true);
        }
        return true;
    }

    @Override
    protected void onUpdateWidget(Switch widget, Boolean value, View view) {
        widget.setChecked(value);
        widget.invalidate();
        syncSummaryView(view);
    }

    /**
     * Sync a summary view contained within view's sub hierarchy with the correct summary text.
     * @param view View where a summary should be located
     */
    private void syncSummaryView(View view) {
        // Sync the summary view
        TextView summaryView = view.findViewById(android.R.id.summary);
        if (summaryView != null) {
            boolean useDefaultSummary = true;
            boolean checked = getValue();
            if (checked && !TextUtils.isEmpty(mSummaryOn)) {
                summaryView.setText(mSummaryOn);
                useDefaultSummary = false;
            } else if (!checked && !TextUtils.isEmpty(mSummaryOff)) {
                summaryView.setText(mSummaryOff);
                useDefaultSummary = false;
            }

            if (useDefaultSummary) {
                final CharSequence summary = getSummary();
                if (!TextUtils.isEmpty(summary)) {
                    summaryView.setText(summary);
                    useDefaultSummary = false;
                }
            }

            int newVisibility = View.GONE;
            if (!useDefaultSummary) {
                // Someone has written to it
                newVisibility = View.VISIBLE;
            }
            if (newVisibility != summaryView.getVisibility()) {
                summaryView.setVisibility(newVisibility);
            }
        }
    }

    @Override
    protected Boolean getPersistedValue(String defaultValue) {
        Boolean defValue = parseString(defaultValue);
        return getPersistedBoolean(defValue);
    }

    @Override
    protected int _getWidgetLayoutResId() {
        return R.layout.preference_widget_switch;
    }

    @Override
    protected Boolean parseString(String value) {
        return "true".equalsIgnoreCase(value);
    }
}
