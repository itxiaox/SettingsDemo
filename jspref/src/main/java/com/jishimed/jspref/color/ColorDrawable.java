package com.jishimed.jspref.color;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

public class ColorDrawable extends AlphaPatternDrawable {
    private int mColor;

    public ColorDrawable(float gridSize) {
        super(gridSize);
        mColor = Color.TRANSPARENT;
    }

    public void setColor(int color) {
        mColor = color;
        invalidateSelf();
    }

    public int getColor() {
        return mColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(mColor);
    }
}
