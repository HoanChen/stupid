package me.khrystal.meituandetailpage.business

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import me.khrystal.meituandetailpage.R
import me.khrystal.meituandetailpage.dp
import me.khrystal.meituandetailpage.view.stateRefresh
import me.khrystal.meituandetailpage.view.stateSave
import me.khrystal.meituandetailpage.view.statesChangeByAnimation

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/23
 * update time:
 * email: 723526676@qq.com
 */
class MerchantSettleLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var firstLayout: Boolean = false
    private var isExpanded = false
    private var effected: Float = 0f

    init {
        LayoutInflater.from(context).inflate(R.layout.merchant_settle_layout, this)
    }

    private fun animViews(): Array<View> = arrayOf(this)

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            stateSave(R.id.vs1).a(1F)
            stateSave(R.id.vs2).a(0f)
        }
    }

    fun effectByOffset(transY: Float) {
        effected = when {
            transY <= dp(110) -> 0F
            transY > dp(110) && transY < dp(140) -> (transY - dp(110)) / dp(30)
            else -> 1F
        }
        animViews().forEach { it.stateRefresh(R.id.vs1, R.id.vs2, effected) }
    }

    fun switch(expanded: Boolean) {
        if (isExpanded == expanded) {
            return
        }
        isExpanded = expanded
        val start = effected
        val end = if (expanded) 1F else 0F
        statesChangeByAnimation(animViews(), R.id.vs1, R.id.vs2, start, end,
                null, object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (isExpanded) visibility = View.INVISIBLE
            }
        }, 300)
    }

}