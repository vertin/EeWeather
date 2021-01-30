package com.vertin.eetweather.util

import android.animation.Animator
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

/**
 * [Animator.AnimatorListener]'s callbacks can be called when Activity/Fragment that started animation is already dead.
 * This class ensures that activity or fragment are present at the time of callback execution
 */
open class SafeAnimatorListener : Animator.AnimatorListener {
    private var fragment: Fragment? = null
    protected var animatedView: View? = null
        private set

    /**
     * @param view If you animate inside Activity use this constructor and pass here any view from this activity (animated view is preferred)
     */
    constructor(view: View) {
        animatedView = view
    }

    /**
     * @param fragment If you animate inside Fragment use this constructor
     */
    constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    fun startAction(animator: Animator?) {}
    fun endAction(animator: Animator?) {}
    fun cancelAction(animator: Animator?) {}
    fun repeatAction(animator: Animator?) {}
    override fun onAnimationStart(animator: Animator) {
        if (isAlive) {
            startAction(animator)
        }
    }

    override fun onAnimationEnd(animator: Animator) {
        if (isAlive) {
            endAction(animator)
        }
    }

    override fun onAnimationCancel(animator: Animator) {
        if (isAlive) {
            cancelAction(animator)
        }
    }

    override fun onAnimationRepeat(animator: Animator) {
        if (isAlive) {
            repeatAction(animator)
        }
    }

    val isAlive: Boolean
        get() {
            if (fragment != null) {
                return fragment!!.isAdded
            }
            return if (animatedView != null) {
                ViewCompat.isAttachedToWindow(animatedView!!)
            } else {
                false
            }
        }
}