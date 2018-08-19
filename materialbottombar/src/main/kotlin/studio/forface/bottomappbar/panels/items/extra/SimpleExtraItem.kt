package studio.forface.bottomappbar.drawer.items.extra

import android.widget.TextView
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.params.Background
import studio.forface.bottomappbar.panels.params.Content

abstract class SimpleExtraItem<T>: Content<T>, Background<T> {

    fun applyTo( textView: TextView ) {
        applyContentTo( textView,true )
        val scaleToSquare = textView.text.length == 1
        applyBackgroundTo( textView, scaleToSquare )
    }

    @Suppress("UNCHECKED_CAST")
    fun copy() = ( newInstance() as SimpleExtraItem<*> ).apply {
        this@apply.contentTextHolder =                  this@SimpleExtraItem.contentTextHolder
        this@apply.contentTextStyleHolder =             this@SimpleExtraItem.contentTextStyleHolder
        this@apply.contentTextSizeHolder =              this@SimpleExtraItem.contentTextSizeHolder
        this@apply.contentColorHolder =                 this@SimpleExtraItem.contentColorHolder

        this@apply.backgroundColorHolder =              this@SimpleExtraItem.backgroundColorHolder
        this@apply.backgroundCornerRadiusSizeHolder =   this@SimpleExtraItem.backgroundCornerRadiusSizeHolder
    } as T

    internal abstract fun newInstance(): T

    override var contentTextHolder =                TextHolder()
    override var contentTextStyleHolder =           TextStyleHolder( bold = true )
    override var contentTextSizeHolder =            TextSizeHolder()
    override var contentColorHolder =               ColorHolder()

    override var backgroundColorHolder =            ColorHolder()
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}