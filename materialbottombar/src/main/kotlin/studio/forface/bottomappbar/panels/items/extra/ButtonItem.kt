package studio.forface.bottomappbar.panels.items.extra

import android.graphics.Color
import android.os.Build
import android.widget.Button
import androidx.core.view.doOnPreDraw
import studio.forface.bottomappbar.panels.holders.SizeHolder
import studio.forface.bottomappbar.panels.holders.TextSizeHolder
import studio.forface.bottomappbar.panels.params.ButtonStyle
import studio.forface.bottomappbar.panels.params.Identifier
import studio.forface.bottomappbar.panels.params.Param
import studio.forface.bottomappbar.panels.params.RippleBackgroundStyle
import studio.forface.bottomappbar.utils.Drawables

/**
 * @author Davide Giuseppe Farella
 * An extra Item for Button.
 *
 * Inherit from [SimpleExtraItem] and [Identifier]
 */
open class ButtonItem: SimpleExtraItem<ButtonItem>(), Identifier<ButtonItem> {

    /** @see Param.thisRef */
    override val thisRef get() = this
    /** @see Identifier.id */
    override var id = Int.MIN_VALUE

    /** A reference to [] for the XXX of the Button */
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
    /** A reference to [] for the XXX of the Button */
    override var contentTextSizeHolder = TextSizeHolder( sp = 12f )

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
}

/** A function for create a [ButtonItem] within a DSL style */
@Suppress("FunctionName")
fun Button( f: ButtonItem.() -> Unit ): ButtonItem {
    val item = ButtonItem()
    item.f()
    return item
}