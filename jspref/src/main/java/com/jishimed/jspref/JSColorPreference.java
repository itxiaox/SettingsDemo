package com.jishimed.jspref;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jishimed.jspref.color.ColorDrawable;
import com.jishimed.jspref.color.ColorPickerDialog;

public class JSColorPreference extends JSPreference<TextView, Integer> {

    public JSColorPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public JSColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSColorPreference(Context context) {
        super(context);
    }

    private ColorDrawable colorDrawable;
    private boolean mAlphaSliderEnabled = true;

    @Override
    protected int _getWidgetLayoutResId() {
        return R.layout.preference_widget_colorview;
    }

    @Override
    protected TextView createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new TextView(context, attrs, 0, R.style.JSColorView);
    }

    @Override
    protected void initWidget(TextView widget) {
        if (colorDrawable == null) {
            float density = getContext().getResources().getDisplayMetrics().density;
            colorDrawable = new ColorDrawable(Math.round(5.0f*density));
        }
        widget.setMinimumWidth(mWidgetWidth);
        widget.setBackground(colorDrawable);
        widget.setFocusable(false);
        widget.setClickable(false);
    }

    public void setAlphaSliderEnabled(boolean enabled) {
        mAlphaSliderEnabled = enabled;
    }

    @Override
    protected boolean handleClick(TextView widget) {
        showDialog();
        return true;
    }

    @Override
    protected void onUpdateWidget(TextView widget, Integer value, View view) {
        int color = value;
        colorDrawable.setColor(color);
        //Gray = 0.2126 R + 0.7152 G + 0.0722 B
        float gray = 0.2126f * Color.red(color) + 0.7152f * Color.green(color) + 0.0722f * Color.blue(color);
        int textColor = (gray >= 127f || Color.alpha(color) < 0x60)?Color.BLACK:Color.WHITE;
        widget.setText(String.format("#%08X", value));
        widget.setTextColor(textColor);
    }

    private void onColorChanged(int color) {
        setValue(color);
    }

    private void showDialog() {
        int color = getValue();
        ColorPickerDialog clrDlg = new ColorPickerDialog(getContext(), color);
        clrDlg.setOnColorChangedListener(this::onColorChanged);
        clrDlg.setAlphaSliderVisible(mAlphaSliderEnabled);
//        if (state != null) {
//            clrDlg.onRestoreInstanceState(state);
//        }
        clrDlg.show();
    }

    @Override
    protected Integer getPersistedValue(String defaultValue) {
        int color = Color.WHITE;
        if (!TextUtils.isEmpty(defaultValue)) {
            color = Color.parseColor(defaultValue);
        }
        return getPersistedInt(color);
    }

    @Override
    protected Integer parseString(String value) {
        return Color.parseColor(value);
    }
}
