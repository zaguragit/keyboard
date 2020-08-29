package posidon.keyboard.layout

import posidon.keyboard.Keyboard

class Key(
    val keyboard: Keyboard,
    val label: String,
    val action: String
) {
    fun click() = keyboard.onKeyClick(this)
}