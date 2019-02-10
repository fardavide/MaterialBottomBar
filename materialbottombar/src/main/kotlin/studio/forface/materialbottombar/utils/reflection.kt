package studio.forface.materialbottombar.utils

import java.lang.reflect.Field
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Davide Giuseppe Farella.
 * A [ReadWriteProperty] for get and set value by Reflection.
 *
 * @param T the type of the Field
 */
internal class ReflectionField<T>(
        /**
         * The Optional name of the field to get/set. If null, the [KProperty.name]
         * will be used
         */
        private val _fieldName: String?,
        /**
         * This [Int] represent how deep in the parent hierarchy we should search for the field.
         * E.g. if [superclassLevel] is 2, the field will be searched in the Parent of the Parent of
         * the current class.
         */
        private val superclassLevel: Int

): ReadWriteProperty<Any, T> {

    /** A reference to the current class */
    private lateinit var thisRef: Any

    /**
     * A lazy reference to the queried superclass
     * @see [superclassLevel]
     */
    private val clazz by lazy {
        var clazz = thisRef::class.java as Class<in Nothing>
        for ( i in 0 until superclassLevel ) clazz = clazz.superclass!!
        clazz
    }

    /**
     * A function that execute a [block] on the queried [Field], it will set [Field.isAccessible]
     * to true before run the [block], then set it again to false.
     *
     * @return [V] the result of the [block]
     */
    private fun <V> useField( fieldName: String, block: Field.() -> V ) : V {
        return clazz.getDeclaredField( fieldName ).run {
            isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val result = this.block()
            isAccessible = false

            return@run result
        }
    }

    /** @see ReadWriteProperty.getValue */
    @Suppress("UNCHECKED_CAST")
    override fun getValue( thisRef: Any, property: KProperty<*> ): T {
        if ( ! this::thisRef.isInitialized ) this.thisRef = thisRef
        return useField(_fieldName ?: property.name ) { get( thisRef ) as T }
    }

    /** @see ReadWriteProperty.setValue */
    override fun setValue( thisRef: Any, property: KProperty<*>, value: T ) {
        if ( ! this::thisRef.isInitialized ) this.thisRef = thisRef
        return useField(_fieldName ?: property.name ) { set( thisRef, value ) }
    }
}

/**
 * A function for delegate a [KProperty] by [ReflectionField]
 *
 * @see ReflectionField._fieldName default is null
 * @see ReflectionField.superclassLevel default is 1
 */
internal fun <T> reflection( fieldName: String? = null, superclassLevel: Int = 1 ) =
        ReflectionField<T>( fieldName, superclassLevel )
