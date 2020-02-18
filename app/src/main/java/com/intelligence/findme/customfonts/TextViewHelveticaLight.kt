package com.intelligence.findme.customfonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class TextViewHelveticaLight : TextView {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        if (!isInEditMode) {
            val tf: Typeface = Typeface.createFromAsset(
                context.assets,
                "fonts/maven_pro_regular.ttf"
            )

            typeface = tf
        }
    }
}