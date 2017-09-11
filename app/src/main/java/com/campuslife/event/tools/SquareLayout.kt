package com.campuslife.event.tools

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by Administrator on 2017/6/16.
 */

class SquareLayout : RelativeLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec

        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec), View.getDefaultSize(0, heightMeasureSpec))

        // Children are just made to fill our space.
        val childWidthSize = measuredWidth
        val childHeightSize = measuredHeight
        //高度和宽度一样
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, View.MeasureSpec.EXACTLY)
        heightMeasureSpec = widthMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
