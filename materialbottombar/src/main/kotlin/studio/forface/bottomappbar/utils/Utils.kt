package studio.forface.bottomappbar.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

internal fun <T> Any.getField( field: String, superclassLevel: Int = 1 ): T {
    var clazz = this::class.java as Class<in Nothing>
    for ( i in 0 until superclassLevel ) clazz = clazz.superclass

    var value: T? = null

    clazz.getDeclaredField( field ).run {
        isAccessible = true
        @Suppress("UNCHECKED_CAST")
        value = this.get( this@getField ) as T
        isAccessible = false
    }
    return value!!
}

internal fun Any.setField(field: String, value: Any?, superclassLevel: Int = 1 ) {
    var clazz = this::class.java as Class<in Nothing>
    for ( i in 0 until superclassLevel ) clazz = clazz.superclass

    clazz.getDeclaredField( field ).run {
        isAccessible = true
        @Suppress("UNCHECKED_CAST")
        this.set( this@setField, value )
        isAccessible = false
    }
}

internal fun Context.useAttributes(
        attrs: AttributeSet?,
        styleable: IntArray,
        block: TypedArray.() -> Unit
) {
    attrs ?: return

    val a = obtainStyledAttributes( attrs, styleable, 0, 0  )
    block( a )
    a.recycle()
}

inline fun <reified T> ViewGroup.findChild() = children.find { it is T } as? T
