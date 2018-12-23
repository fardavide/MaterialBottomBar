package studio.forface.bottomappbar.dsl

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Davide Giuseppe Farella
 * A [ReadWriteProperty] for a DSL component.
 *
 * @param T the receiver class
 * @param V the value to set
 */
internal class DslComponent<T, V> ( private val setter: T.(V) -> Unit ) : ReadWriteProperty<T, V> {
    /**
     * Returns the value of the property for the given object.
     * @param thisRef the object for which the value is requested.
     * @param property the metadata for the property.
     * @return the property value.
     */
    override fun getValue( thisRef: T, property: KProperty<*> ): V = throwDslGetException()

    /**
     * Sets the value of the property for the given object.
     * @param thisRef the object for which the value is requested.
     * @param property the metadata for the property.
     * @param value the value to set.
     */
    override fun setValue( thisRef: T, property: KProperty<*>, value: V ) {
        thisRef.setter( value )
    }
}

/** Create a [DslComponent] with the given [setter] */
internal fun <T, V> dsl( setter: T.(V) -> Unit ) = DslComponent( setter )