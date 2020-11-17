package ru.appintheair.flightreviewssubmit

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout

class CollapsingPopupLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private var mImageTopExpanded = 0
    private var mImageTopCollapsed = 0

    private var mOnOffsetChangedListener: OnOffsetChangedListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val parent = parent
        if (parent is AppBarLayout) {
            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = OnOffsetChangedListener()
            }
            parent.addOnOffsetChangedListener(mOnOffsetChangedListener)
        }
    }

    override fun onDetachedFromWindow() {
        val parent = parent
        if (mOnOffsetChangedListener != null && parent is AppBarLayout) {
            parent.removeOnOffsetChangedListener(mOnOffsetChangedListener)
        }
        super.onDetachedFromWindow()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val child = getChildAt(0)
        getViewOffsetHelper(child).onViewLayout()
    }

    inner class OnOffsetChangedListener : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            val scrollRange = appBarLayout.totalScrollRange
            val offsetFactor = (-verticalOffset).toFloat() / scrollRange.toFloat()
            Log.d(TAG, "onOffsetChanged(), offsetFactor = $offsetFactor")
            val child: View = getChildAt(0)
            val offsetHelper = getViewOffsetHelper(child)
            val scaleFactor = -offsetFactor * resources.getDimension(R.dimen.offsetFactor)
            val avatarView = child.findViewById<View>(R.id.avatar)
            val title = child.findViewById<View>(R.id.title)
            val subtitle = child.findViewById<View>(R.id.subtitle)
            val ratingBar = child.findViewById<View>(R.id.flight_rating_bar)
            val popupLayoutContainer = child.findViewById<View>(R.id.popup_container)
            val toolbar = child.findViewById<View>(R.id.toolbar)

            popupLayoutContainer.y = -30f

            title.alpha = -offsetFactor + 1
            subtitle.alpha = -offsetFactor + 1
            ratingBar.alpha = -offsetFactor + 1
            if (isEnabledRatingBar) {
                ratingBar.isEnabled = ratingBar.alpha != 0f
            }
            avatarView.y =
                -scaleFactor + resources.getDimension(R.dimen.avatar_coordinate) - resources.getDimension(
                    R.dimen.scaleFactorAvatarDimen
                ) + offsetFactor * resources.getDimension(R.dimen.avatar_multiply_factor)
            toolbar.y =
                -scaleFactor + resources.getDimension(R.dimen.toolbar_coordinate) - resources.getDimension(
                    R.dimen.scaleFactorToolbarDimen
                ) + offsetFactor * resources.getDimension(
                    R.dimen.toolbar_multiply_factor
                )
            val topOffset =
                ((mImageTopCollapsed - mImageTopExpanded) * offsetFactor).toInt() - verticalOffset
            offsetHelper.setTopAndBottomOffset(topOffset)
        }
    }

    companion object {
        const val TAG = "CollapsingPopupLayout"
        var isEnabledRatingBar = true
        private fun getViewOffsetHelper(view: View): ViewOffsetHelper {
            var offsetHelper = view.getTag(R.id.view_offset_helper) as ViewOffsetHelper?
            if (offsetHelper == null) {
                offsetHelper = ViewOffsetHelper(view)
                view.setTag(R.id.view_offset_helper, offsetHelper)
            }
            return offsetHelper
        }
    }

    class ViewOffsetHelper(private val mView: View) {
        private var mLayoutTop = 0
        private var mOffsetTop = 0
        fun onViewLayout() {
            mLayoutTop = mView.top
            updateOffsets()
        }

        private fun updateOffsets() {
            ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.top - mLayoutTop))
        }

        fun setTopAndBottomOffset(offset: Int) {
            if (mOffsetTop != offset) {
                mOffsetTop = offset
                updateOffsets()
            }
        }
    }
}