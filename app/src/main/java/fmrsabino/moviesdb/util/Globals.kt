package fmrsabino.moviesdb.util

import android.content.Context

fun nop () {}

fun dpsToPixels(context: Context, dps: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dps * scale + 0.5).toInt()
}
