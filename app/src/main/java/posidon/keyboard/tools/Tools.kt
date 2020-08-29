package posidon.keyboard.tools

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.*
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.vectordrawable.graphics.drawable.Animatable2Compat

object Tools {

    lateinit var publicContext: Context

    inline fun animate(d: Drawable): Drawable {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && d is Animatable2 -> {
                d.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            d is Animatable2Compat -> {
                d.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) = d.start()
                })
                d.start()
            }
            d is AnimationDrawable -> d.start()
        }
        return d
    }

    inline fun clearAnimation(d: Drawable?) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && d is Animatable2 -> d.clearAnimationCallbacks()
            d is Animatable2Compat -> d.clearAnimationCallbacks()
            d is Animatable -> d.stop()
        }
    }

    inline fun isInstalled(packageName: String, packageManager: PackageManager): Boolean {
        var found = true
        try { packageManager.getPackageInfo(packageName, 0) }
        catch (e: Exception) { found = false }
        return found
    }
}

inline fun Drawable.toBitmap(duplicateIfBitmapDrawable: Boolean = false): Bitmap {
    if (this is BitmapDrawable && bitmap != null) return if (duplicateIfBitmapDrawable) Bitmap.createBitmap(bitmap) else bitmap
    val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    else Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    try { draw(canvas) }
    catch (e: Exception) {}
    return bitmap
}

inline fun Drawable.toBitmap(width: Int, height: Int, duplicateIfBitmapDrawable: Boolean = false): Bitmap {
    if (this is BitmapDrawable && bitmap != null) return if (duplicateIfBitmapDrawable) Bitmap.createBitmap(bitmap) else bitmap
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    try { draw(canvas) }
    catch (e: Exception) { e.printStackTrace() }
    return bitmap
}

inline val Number.dp get() = Tools.publicContext.resources.displayMetrics.density * toFloat()

inline fun Context.vibrate() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(14L, VibrationEffect.DEFAULT_AMPLITUDE), AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
    else (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(14L)
}