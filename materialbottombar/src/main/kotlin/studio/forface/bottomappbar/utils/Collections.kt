package studio.forface.bottomappbar.utils

operator fun <T> List<T>.times( times: Int ): List<T> {
    var list = listOf<T>()
    repeat( times ) { list += this }
    return list
}