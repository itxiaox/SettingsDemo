package com.jishimed.jspref;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class JSButtonPreference extends JSPreference<Button, String> {

    public JSButtonPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public JSButtonPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSButtonPreference(Context context) {
        super(context);
    }

    @Override
    protected int _getWidgetLayoutResId() {
        return R.layout.preference_widget_button;
    }

    @Override
    protected Button createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new Button(context, attrs, android.R.attr.buttonStyle);
    }

    @Override
    protected void initWidget(Button widget) {
        widget.setMinimumWidth(mWidgetWidth);
        widget.setFocusable(false);  //Make the preference clickable.
        widget.setClickable(false);
    }

    @Override
    protected boolean handleClick(Button widget) {
        String text = (String)widget.getText();
        if ("Checked".equals(text)) {
            //widget.setText("Unchecked");
            setValue("Unchecked");
        }
        else {
            //widget.setText("Checked");
            setValue("Checked");
        }
        return true;
    }

    @Override
    protected void onUpdateWidget(Button widget, String value, View view) {
        widget.setText(value);
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
