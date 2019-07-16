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

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * This drawable that draws a simple white and gray chessboard pattern.
 * It's pattern you will often see as a background behind a
 * partly transparent image in many applications.
 *
 * @author Daniel Nilsson
 */
class AlphaPatternDrawable extends Drawable {

    /**
     * Bitmap in which the pattern will be cached.
     */
    private Bitmap mBitmap;
    private float mGridSize;

    public AlphaPatternDrawable(float gridSize) {
        mGridSize = gridSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, getBounds(), null);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException("Alpha is not supported by this drawable.");
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        throw new UnsupportedOperationException("ColorFilter is not supported by this drawable.");
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBitmap = generatePatternBitmap(bounds.width(), bounds.height(), mGridSize);
    }

    public void setGridSize(float gridSize) {
        if (gridSize > 0 && gridSize != mGridSize) {
            mGridSize = gridSize;

            Rect bounds = getBounds();
            mBitmap = generatePatternBitmap(bounds.width(), bounds.height(), mGridSize);
        }
    }

    /**
     * This will generate a bitmap with the pattern
     * as big as the rectangle we were allow to draw on.
     * We do this to cache the bitmap so we don't need to
     * recreate it each time draw() is called since it
     * takes a few milliseconds.
     */
    private Bitmap generatePatternBitmap(int width, int height, float gridSize) {
        if (width <= 0 || height <= 0) return null;

        int xNum = (int) Math.ceil((width / gridSize));
        int yNum = (int) Math.ceil(height / gridSize);

        Bitmap pattern = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(pattern);

        //Fill with WHITE
        canvas.drawColor(Color.WHITE);

        //Draw grids of light gray
        Paint paintGray = new Paint();
        paintGray.setColor(Color.LTGRAY);

        for (int i = 0; i <= yNum; i++) {
            float top = i * gridSize;
            float bottom = top + gridSize;
            for (int j = (i & 0x1); j <= xNum; j+=2) {
                float left = j * gridSize;
                canvas.drawRect(left, top, left + gridSize, bottom, paintGray);
            }
        }
        return pattern;
    }
}