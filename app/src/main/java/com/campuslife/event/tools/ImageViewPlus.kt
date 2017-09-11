package com.campuslife.event.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by Administrator on 2017/6/6.
 */

class ImageViewPlus(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    private val mPaintBitmap = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRawBitmap: Bitmap? = null
    private var mShader: BitmapShader? = null
    private val mMatrix = Matrix()


    override fun onDraw(canvas: Canvas) {
        val rawBitmap = getBitmap(drawable)
        if (rawBitmap != null) {
            val viewWidth = width
            val viewHeight = height
            val viewMinSize = Math.min(viewWidth, viewHeight)
            val dstWidth = viewMinSize.toFloat()
            val dstHeight = viewMinSize.toFloat()
            if (mShader == null || rawBitmap != mRawBitmap) {
                mRawBitmap = rawBitmap
                mShader = BitmapShader(mRawBitmap!!, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP)
            }
            if (mShader != null) {
                mMatrix.setScale(dstWidth / rawBitmap.width,
                        dstHeight / rawBitmap.height)
                mShader!!.setLocalMatrix(mMatrix)
            }
            mPaintBitmap.shader = mShader
            val radius = viewMinSize / 2.0f
            canvas.drawCircle(radius, radius, radius, mPaintBitmap)
        } else {
            super.onDraw(canvas)
        }
    }


    private fun getBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is ColorDrawable) {
            val rect = drawable.getBounds()
            val width = rect.right - rect.left
            val height = rect.bottom - rect.top
            val color = drawable.color
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color),
                    Color.blue(color))
            return bitmap
        } else {
            return null
        }
    }
}