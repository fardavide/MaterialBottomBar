package studio.forface.bottomappbar.panels.items.extra

import android.widget.TextView
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.params.Background
import studio.forface.bottomappbar.panels.params.Content

abstract class SimpleExtraItem<T>: Content<T>, Background<T>, Cloneable {

    fun applyTo( textView: TextView ) {
        applyContentTo( textView,true )
        val scaleToSquare = textView.text.length == 1
        applyBackgroundTo( textView, scaleToSquare )
    }

    @Suppress("UNCHECKED_CAST")
    fun cloneRef() = clone() as T

    override var contentTextHolder =                TextHolder()
    override var contentTextStyleHolder =           TextStyleHolder( bold = true )
    override var contentTextSizeHolder =            TextSizeHolder()
    override var contentColorHolder =               ColorHolder()

    override var backgroundColorHolder =            ColorHolder()
    override var backgroundCornerRadiusSizeHolder = SizeHolder( dp = 6f )
}