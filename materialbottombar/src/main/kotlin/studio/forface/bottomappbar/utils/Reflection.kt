package studio.forface.bottomappbar.utils

import java.lang.reflect.Field
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> reflection( fieldName: String? = null, superclassLevel: Int = 1 ) =
        ReflectionField<T>( fieldName, superclassLevel )

class ReflectionField<T>(
        private val _fieldName: String?,
        private val superclassLevel: Int
): ReadWriteProperty<Any, T> {

    private lateinit var thisRef: Any
    private val clazz by lazy {
        var clazz = thisRef::class.java as Class<in Nothing>
        for ( i in 0 until superclassLevel ) clazz = clazz.superclass
        clazz
    }
    private fun <V> useField( fieldName: String, block: Field.() -> V ) : V =
            clazz.getDeclaredField( fieldName ).run {
                isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val result = this.block()
                isAccessible = false

                return@run result
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*> ): T = kotlin.run {
        if ( ! this::thisRef.isInitialized ) this.thisRef = thisRef
        useField(_fieldName ?: property.name) { get( thisRef ) as T }
    }

    override fun setValue( thisRef: Any, property: KProperty<*>, value: T ): Unit = kotlin.run {
        if ( ! this::thisRef.isInitialized ) this.thisRef = thisRef
        useField(_fieldName ?: property.name) { set( thisRef, value ) }
    }

}