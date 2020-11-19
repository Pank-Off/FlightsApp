package ru.appintheair.flightreviewssubmit

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener

class KeyboardUtil(activity: Activity, private var contentView: View) {
    private var decorView: View = activity.window.decorView

    private var onGlobalLayoutListener = OnGlobalLayoutListener {
        val rect = Rect()

        decorView.getWindowVisibleDisplayFrame(rect)

        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - rect.bottom

        if (diff != 0) {
            if (contentView.paddingBottom != diff) {
                contentView.setPadding(0, 0, 0, diff)
            }
        } else {
            if (contentView.paddingBottom != 0) {
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }

    init {
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}