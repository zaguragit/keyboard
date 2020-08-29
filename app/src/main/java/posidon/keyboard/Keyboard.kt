package posidon.keyboard

import android.graphics.drawable.GradientDrawable
import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import posidon.keyboard.layout.Key
import posidon.keyboard.layout.Layout
import posidon.keyboard.tools.Tools
import posidon.keyboard.tools.dp
import posidon.keyboard.tools.vibrate
import posidon.keyboard.view.KeyboardView


class Keyboard : InputMethodService() {

    companion object {
        private var caps = false
        lateinit var keyDrawable: GradientDrawable; private set
        lateinit var keyPressedDrawable: GradientDrawable; private set
    }

    override fun onCreateInputView(): View {
        Tools.publicContext = this
        keyDrawable = GradientDrawable().apply {
            setStroke(1, 0xff252627.toInt())
            setColor(0xff111213.toInt())
            cornerRadius = 5.dp
        }
        keyPressedDrawable = GradientDrawable().apply {
            setStroke(1, 0xff111213.toInt())
            setColor(0xff0d0e0f.toInt())
            cornerRadius = 5.dp
        }
        val en = Layout.loadFile(resources, R.raw.en)
        val layout = Layout.parse(this, en)
        return KeyboardView(this, layout)
    }

    fun onKeyClick(key: Key) {
        vibrate()
        when (key.action) {
            "delete" -> currentInputConnection.deleteSurroundingText(1, 0)
            "shift" -> caps = !caps
            "done" -> currentInputConnection.sendKeyEvent(
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
            )
            else -> if (key.action.length == 1) {
                var code = key.action[0]
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code)
                    caps = false
                }
                currentInputConnection.commitText(code.toString(), 1)
            }
        }
    }
}