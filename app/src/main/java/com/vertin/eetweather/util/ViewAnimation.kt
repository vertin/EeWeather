package com.vertin.eetweather.util

import android.animation.Animator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.Transformation

object ViewAnimation {
    const val DEFAULT_ANIMATION_TIME = 200L

    /**
     * @param fadeIn  is going to be VISIBLE
     * @param fadeOut is going to be GONE
     */
    fun crossfade(fadeIn: View?, fadeOut: View?) {
        fadeIn(fadeIn)
        fadeOut(fadeOut)

    }

    fun crossfade(fadeIn: View?, fadeOut: View?, delay: Long?) {
        fadeIn(fadeIn, DEFAULT_ANIMATION_TIME, delay)
        fadeOut(fadeOut)

    }


    fun fadeIn(view: View?, duration: Long = DEFAULT_ANIMATION_TIME) {
        fadeIn(view, duration, 0L)
    }

    fun fadeIn(fadeIn: View?, duration: Long?, delay: Long?) {
        //try to cancel all the animations possible and reset all listeners
        fadeIn?.animation?.cancel()
        fadeIn?.animate()?.setListener(null)?.cancel()
        fadeIn?.clearAnimation()
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        fadeIn
            ?.takeIf { it.visibility != View.VISIBLE || it.alpha < 1f }
            ?.also {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                it.alpha = 0f
                it.visibility = View.VISIBLE
            }
            ?.animate()
            ?.alpha(1f)
            ?.setStartDelay(delay!!)
            ?.setDuration(duration!!)
            ?.setListener(null)

    }


    fun fadeOut(fadeOut: View?, duration: Long = DEFAULT_ANIMATION_TIME) {
        fadeOut(fadeOut, duration, View.GONE, {})
    }

    fun fadeOut(fadeOut: View?, duration: Long = DEFAULT_ANIMATION_TIME, endAction: () -> Unit) {
        fadeOut(fadeOut, duration, View.GONE, endAction)
    }

    fun fadeOut(
        fadeOut: View?,
        duration: Long = DEFAULT_ANIMATION_TIME,
        visibility: Int,
        endAction: () -> Unit
    ) {
        //try to cancel all the animations possible and reset all listeners
        fadeOut?.animation?.cancel()
        fadeOut?.animate()?.setListener(null)?.cancel()
        fadeOut?.clearAnimation()
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        fadeOut?.takeIf { it.visibility == View.VISIBLE && it.alpha > 0f }
            ?.animate()
            ?.alpha(0f)
            ?.setDuration(duration!!)
            ?.setListener(object : SafeAnimatorListener(fadeOut) {

                override fun onAnimationEnd(animator: Animator) {
                    animatedView?.visibility = visibility
                    endAction()
                }
            })
    }

    fun fadeOutInvisible(fadeOut: View?, duration: Long = DEFAULT_ANIMATION_TIME) {
        fadeOut(fadeOut, duration, View.INVISIBLE, {})
    }

    fun expand(v: View): Animation {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.alpha = 0f
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.alpha = interpolatedTime
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }
        a.interpolator = OvershootInterpolator()
        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
        return a
    }

    fun collapse(v: View): Animation {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.alpha = 1f - interpolatedTime
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean = true
        }
        //a.interpolator = OvershootInterpolator()
        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
        return a
    }
}