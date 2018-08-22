package studio.forface.bottomappbar.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Like a normal [lazy], this value is set at the first call BUT, if the value is null, the
 * [initializer] will be called until the value is not null.
 * @param V the type of the [KProperty].
 * @param initializer the init that will initialize the value.
 */
internal fun <V> retryIfNullLazy( initializer: () -> V ) =
        RetryIfNullLazy<Any, V>( initializer )

/**
 * [ReadOnlyProperty] extended class for [retryIfNullLazy].
 */
internal class RetryIfNullLazy<T, V>( val init: () -> V ): ReadOnlyProperty<T, V> {
    private object EMPTY
    private var value: Any? = EMPTY
    override fun getValue( thisRef: T, property: KProperty<*> ): V {
        if ( value == EMPTY ) value = init()
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}

/**
 * Same as [retryIfNullLazy], BUT here the [initializer] will be called until the value is not null
 * and different from the [default] value.
 * @param V the type of the [KProperty].
 * @param default the default value.
 * @param initializer the init that will initialize the value.
 */
internal fun <V> retryIfDefaultLazy( default: V, initializer: () -> V ) =
        RetryIfDefaultLazy<Any, V>( default, initializer )

/**
 * [ReadOnlyProperty] extended class for [retryIfDefaultLazy].
 */
internal class RetryIfDefaultLazy<T, V>( val default: V, val init: () -> V ): ReadOnlyProperty<T, V> {
    private var value = default
    override fun getValue( thisRef: T, property: KProperty<*> ): V {
        if ( value == default || value == null ) value = init()
        if ( value == null ) value = default
        return value
    }
}

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

inline fun <reified T> ViewGroup.findChildType() = children.find { it is T } as? T
