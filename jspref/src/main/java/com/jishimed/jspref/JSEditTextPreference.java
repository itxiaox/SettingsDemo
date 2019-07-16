package com.jishimed.jspref;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class JSEditTextPreference extends JSPreference<EditText, String> {
    private static final String TAG = "JSEditTextPreference";

    private int mInputType;

    public JSEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public JSEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JSEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JSEditTextPreference(Context context) {
        super(context);
    }

    public void setInputType(int inputType) {
        if (inputType != mInputType) {
            mInputType = inputType;
            EditText edit = getWidget();
            if (edit != null) {
                edit.setInputType(mInputType);
            }
        }
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.inputType});
        mInputType = a.getInt(0, InputType.TYPE_CLASS_TEXT);
        a.recycle();

        super.init(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onUpdateWidget(EditText widget, String value, View view) {
        widget.setText(value);
        if (value != null) {
            widget.setSelection(value.length());//将光标移至文字末尾
        }
    }

    @Override
    protected EditText createWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new EditText(context, attrs, android.R.attr.editTextStyle);
    }

    @Override
    protected void initWidget(EditText widget) {
        widget.setInputType(mInputType);
        widget.setMinimumWidth(mWidgetWidth);
        widget.setOnFocusChangeListener(this::onWidgetFocusChange);
        //widget.setFocusable(false);
    }

    private void onWidgetFocusChange(View v, boolean hasFocus) {
        EditText edit = (EditText)v;
        String str = edit.getText().toString().trim();
        if (!hasFocus) {
            edit.clearFocus();
            if (TextUtils.isEmpty(str)) {
                Toast.makeText(getContext(), "不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            setValue(str);
            //edit.setFocusable(false);
        }
//        else {
//            edit.requestFocus();
//        }
    }

    @Override
    protected boolean handleClick(EditText widget) {
        widget.post(() -> {
                widget.setFocusableInTouchMode(true);
                widget.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (lManager != null) {
                    lManager.showSoftInput(widget, InputMethodManager.SHOW_IMPLICIT);
                }
        });
        return true;
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
    protected String getPersistedValue(String defaultValue) {
        return getPersistedString(defaultValue);
    }

    @Override
    protected int _getWidgetLayoutResId() {
        return R.layout.preference_widget_edittext;
    }

    @Override
    protected String parseString(String value) {
        return value;
    }

}
