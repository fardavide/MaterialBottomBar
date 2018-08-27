package studio.forface.bottomappbar.panels.items.extra

import android.graphics.Color
import android.widget.Button
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.drawer.items.extra.SimpleExtraItem
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.params.ButtonStyle
import studio.forface.bottomappbar.panels.params.RippleBackgroundStyle
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