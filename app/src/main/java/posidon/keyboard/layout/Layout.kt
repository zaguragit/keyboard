package posidon.keyboard.layout

import android.content.res.Resources
import org.json.JSONException
import org.json.JSONObject
import posidon.keyboard.Keyboard
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

class Layout(
    val name: String,
    val keys: Array<Array<Key>>
) {

    companion object {

        fun parse(keyboard: Keyboard, resources: Resources, id: Int) = parse(keyboard, loadFile(resources, id))
        fun parse(keyboard: Keyboard, string: String): Layout = try {
            val json = JSONObject(string)
            val name = json.getString("name")
            val jsonKeys = json.getJSONArray("keys")
            val keys = Array(jsonKeys.length()) {
                val jsonRow = jsonKeys.getJSONArray(it)
                Array(jsonRow.length()) {
                    val jsonKey = jsonRow.getJSONObject(it)
                    val jsonLabel = jsonKey.getString("label")
                    val jsonAction = jsonKey.getString("action")
                    Key(keyboard, jsonLabel, jsonAction)
                }
            }
            Layout(name, keys)
        } catch (e: JSONException) {
            e.printStackTrace()
            Layout("error", arrayOf())
        }

        fun loadFile(resources: Resources, id: Int): String {
            lateinit var string: String
            StringWriter().use { writer ->
                val buffer = CharArray(1024)
                resources.openRawResource(id).use { stream ->
                    val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
                    var n: Int
                    while (reader.read(buffer).also { n = it } != -1) {
                        writer.write(buffer, 0, n)
                    }
                }
                string = writer.buffer.toString()
            }
            return string
        }
    }
}