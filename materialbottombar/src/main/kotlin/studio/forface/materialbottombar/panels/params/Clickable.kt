package studio.forface.materialbottombar.panels.params

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have a [Clickable]
 * Inherit from [Param]
 */
interface Clickable<T>: Param<T> {

    /** An lambda block for the Item */
    var onClick: (T) -> Unit

    /**
     * Set an lambda block to the Item
     * @return [T]
     */
    fun setOnClick( block: (T) -> Unit ) = thisRef.apply { this@Clickable.onClick = block }
}

/** Set [Clickable.onClick] by infix. E.g. `myClickableItem onClick { doSomething() }` */
infix fun <T: Clickable<T>> Clickable<T>.onClick( block: (T) -> Unit ) = setOnClick( block )