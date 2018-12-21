package studio.forface.bottomappbar.panels.items.extra

import android.graphics.Color
import android.os.Build
import android.widget.Button
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.params.ButtonStyle
import studio.forface.bottomappbar.panels.params.Identifier
import studio.forface.bottomappbar.panels.params.RippleBackgroundStyle
import studio.forface.bottomappbar.utils.Drawables
import studio.forface.bottomappbar.utils.elevationCompat

/**
 * @author Davide Giuseppe Farella
 */
open class ButtonItem: SimpleExtraItem<ButtonItem>(), Identifier<ButtonItem> {
    override val thisRef get() = this
    override fun newInstance() = ButtonItem()

    override var id = Int.MIN_VALUE

    /** Apply the [ButtonItem] params to a [Button], with the given [ButtonStyle] */
    fun applyTo( button: Button, buttonStyle: ButtonStyle ) {
        button.doOnPreDraw {
            applyContentTo( button,true )

            val isFlat = buttonStyle == RippleBackgroundStyle.FLAT
            if ( isFlat && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                button.elevation = 0f
                button.stateListAnimator = null
            }

            val color = backgroundColorHolder.resolveColor( button.context ) ?: Color.TRANSPARENT
            val cornerLimit = Math.min( button.height, button.width ) / 2
            val cornerRadius = ( backgroundCornerRadiusSizeHolder.resolveFloatSize() ?: 0f )
                    .coerceAtMost( cornerLimit.toFloat() )

            button.background = Drawables.selectableDrawable(
                    color,
                    cornerRadius,
                    startTransparent = isFlat
            )
        }
    }

    override var contentTextSizeHolder =            TextSizeHolder( sp = 12f )
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}