package posidon.keyboard.view

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import posidon.keyboard.layout.Layout
import posidon.keyboard.tools.dp

class KeyboardView(
    context: Context,
    val layout: Layout
) : LinearLayout(context) {

    init {
        orientation = VERTICAL
        setBackgroundColor(0xff000000.toInt())
        for (row in layout.keys) {
            val rowView = LinearLayout(context).apply {
                orientation = HORIZONTAL
            }
            addView(rowView, LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                val m = 4.dp.toInt()
                setMargins(0, m, 0, m)
            })
            for (key in row) {
                rowView.addView(KeyView(context, key).apply {
                    val p = 8.dp.toInt()
                    setPadding(p, p, p, p)
                    clipToPadding = false
                }, LayoutParams(0, WRAP_CONTENT, 1f).apply {
                    val m = 4.dp.toInt()
                    setMargins(m, 0, m, 0)
                })
            }
        }
    }
}