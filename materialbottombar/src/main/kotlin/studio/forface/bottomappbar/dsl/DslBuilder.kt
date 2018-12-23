package studio.forface.bottomappbar.dsl

/**
 * @author Davide Giuseppe Farella.
 * A common interface for Builders
 *
 * @param T the return value of the Builder
 * @see build
 */
internal interface DslBuilder<out T> {

    /** Create [T] */
    fun build(): T
}