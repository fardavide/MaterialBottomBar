package studio.forface.materialbottombar.panels.params

/**
 * @author Davide Giuseppe Farella.
 * An interface for items that have an [Identifier]
 * Inherit from [Param]
 */
interface Identifier<T>: Param<T> {

    /** An [Int] id for the Item */
    var id: Int

    /**
     * Set an [Int] Identifier
     * @return [T]
     */
    fun id( id: Int ) = thisRef.apply { this@Identifier.id = id }
}