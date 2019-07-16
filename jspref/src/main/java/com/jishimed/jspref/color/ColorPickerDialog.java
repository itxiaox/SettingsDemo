/*
 * Copyright (C) 2010 Daniel Nilsson
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

package com.jishimed.jspref.color;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.jishimed.jspref.R;

import java.util.Objects;

public class ColorPickerDialog extends Dialog
        implements ColorPickerView.OnColorChangedListener, View.OnClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private ColorPickerView mColorPicker;

    private ColorView mOldColor;
    private ColorView mNewColor;

    private OnColorChangedListener mListener;
    private int mOrientation;
    private View mLayout;

    @Override
    public void onGlobalLayout() {
        if (getContext().getResources().getConfiguration().orientation != mOrientation) {
            final int oldColor = mOldColor.getColor();
            final int newColor = mNewColor.getColor();
            mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            setUp(oldColor);
            mNewColor.setColor(newColor);
            mColorPicker.setColor(newColor);
        }
    }

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    public ColorPickerDialog(Context context, int initialColor) {
        super(context);

        init(initialColor);
    }

    private void init(int color) {
        // To fight color banding.
        Objects.requireNonNull(getWindow()).setFormat(PixelFormat.RGBA_8888);
        setUp(color);
    }

    private void setUp(int color) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLayout = Objects.requireNonNull(inflater).inflate(R.layout.dialog_color_picker, null);
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mOrientation = getContext().getResources().getConfiguration().orientation;
        setContentView(mLayout);

        setTitle(R.string.dialog_color_picker);

        mColorPicker = mLayout.findViewById(R.id.color_picker_view);
        mOldColor = mLayout.findViewById(R.id.old_color_panel);
        mNewColor = mLayout.findViewById(R.id.new_color_panel);

        ((LinearLayout) mOldColor.getParent()).setPadding(
                Math.round(mColorPicker.getDrawingOffset()),
                0,
                Math.round(mColorPicker.getDrawingOffset()),
                0
        );

        mOldColor.setOnClickListener(this);
        mNewColor.setOnClickListener(this);
        mColorPicker.setOnColorChangedListener(this);
        mOldColor.setColor(color);
        mColorPicker.setColor(color, true);
    }

    @Override
    public void onColorChanged(int color) {
        mNewColor.setColor(color);
    }

    public void setAlphaSliderVisible(boolean visible) {
        mColorPicker.setAlphaSliderVisible(visible);
    }

    public boolean getAlphaSliderVisible() {
        return mColorPicker.getAlphaSliderVisible();
    }

    /**
     * Set a OnColorChangedListener to get notified when the color
     * selected by the user has changed.
     *
     * @param listener      listener to set
     */
    public void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    public int getColor() {
        return mColorPicker.getColor();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_color_panel) {
            if (mListener != null) {
                mListener.onColorChanged(mNewColor.getColor());
            }
        }
        dismiss();
    }

    @Override
    public @NonNull Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("old_color", mOldColor.getColor());
        state.putInt("new_color", mNewColor.getColor());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int old_color = savedInstanceState.getInt("old_color");
        int new_color = savedInstanceState.getInt("new_color");
        mOldColor.setColor(old_color);
        mNewColor.setColor(new_color);
        mColorPicker.setColor(new_color, true);
    }
}
