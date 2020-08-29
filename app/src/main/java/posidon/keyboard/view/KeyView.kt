package posidon.keyboard.view

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import posidon.keyboard.layout.Key
import posidon.keyboard.Keyboard
import posidon.keyboard.R

class KeyView(context: Context, val key: Key) : FrameLayout(context) {

    init {
        background = Keyboard.keyDrawable
        addView(TextView(context).apply {
            text = key.label
            setTextColor(0xffffffff.toInt())
            includeFontPadding = false
            textSize = 18f
            gravity = Gravity.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.lexend_deca)
        }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                background = Keyboard.keyPressedDrawable
                return true
            }
            MotionEvent.ACTION_UP -> {
                key.click()
                background = Keyboard.keyDrawable
                return true
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE -> {
                background = Keyboard.keyDrawable
                return true
            }
        }
        return super.onTouchEvent(e)
    }
}