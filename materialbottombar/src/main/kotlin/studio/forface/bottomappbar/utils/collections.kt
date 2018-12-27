package studio.forface.bottomappbar.utils

/*
 * Author: Davide Giuseppe Farella.
 * A file containing utilities for Collection's
 */


operator fun <T> List<T>.times( times: Int ): List<T> {
    var list = listOf<T>()
    repeat( times ) { list += this }
    return list
}