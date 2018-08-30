package studio.forface.bottomappbar.utils

import android.animation.Animator
import android.view.ViewPropertyAnimator

inline fun ViewPropertyAnimator.doOnEnd( crossinline action: () -> Unit ): ViewPropertyAnimator =
        setListener( object : Animator.AnimatorListener {
            override fun onAnimationRepeat( animator: Animator ) {  }
            override fun onAnimationEnd( animator: Animator ) { action() }
            override fun onAnimationCancel( animator: Animator ) {  }
            override fun onAnimationStart( animator: Animator ) {  }
        } )
