package com.jishimed.jspref.color;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jishimed.jspref.R;

@SuppressLint("AppCompatCustomView")
public class ColorView extends TextView {
    private static final String TAG = "ColorView";

    private ColorDrawable mBackground;

    public ColorView(Context context) {
        super(context, null, 0, R.style.JSColorView);
        init(context, null);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0, R.style.JSColorView);
        init(context, attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.style.JSColorView);
        init(context, attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float gridSize = 5.0f * context.getResources().getDisplayMetrics().density;  //5dip
        mBackground = new ColorDrawable(gridSize);
        setBackground(mBackground);
        setColor(Color.TRANSPARENT);
    }

    public void setColor(int color) {
        mBackground.setColor(color);

        //Gray = 0.2126 R + 0.7152 G + 0.0722 B
        float gray = 0.2126f * Color.red(color) + 0.7152f * Color.green(color) + 0.0722f * Color.blue(color);
        int textColor = (gray >= 127f || Color.alpha(color) < 0x60)?Color.BLACK:Color.WHITE;

        setTextColor(textColor);
        setText(String.format("#%08X", color));
    }

    public int getColor() {
        return mBackground.getColor();
    }
}
