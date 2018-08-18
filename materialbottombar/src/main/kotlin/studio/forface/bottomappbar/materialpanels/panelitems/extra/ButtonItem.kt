package studio.forface.bottomappbar.materialbottomdrawer.draweritems.extra

import android.graphics.Color
import android.widget.Button
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.materialpanels.holders.*
import studio.forface.bottomappbar.materialpanels.params.ButtonStyle
import studio.forface.bottomappbar.materialpanels.params.RippleBackgroundStyle
import studio.forface.bottomappbar.utils.Drawables

open class ButtonItem: SimpleExtraItem<ButtonItem>() {
    override val thisRef get() = this
    override fun newInstance() = ButtonItem()

    fun applyTo( button: Button, buttonStyle: ButtonStyle ) {
        button.doOnPreDraw {
            applyContentTo( button,true )

            val color = backgroundColorHolder.resolveColor( button.context ) ?: Color.TRANSPARENT
            val cornerLimit = Math.min( button.height, button.width ) / 2
            val cornerRadius = ( backgroundCornerRadiusSizeHolder.resolveFloatSize() ?: 0f )
                    .coerceAtMost( cornerLimit.toFloat() )

            button.background = Drawables.selectableDrawable(
                    color,
                    cornerRadius,
                    buttonStyle == RippleBackgroundStyle.FLAT
            )
        }
    }

    override var contentTextSizeHolder =            TextSizeHolder( sp = 12f )
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}