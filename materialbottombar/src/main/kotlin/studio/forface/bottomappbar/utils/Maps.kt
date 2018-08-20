package studio.forface.bottomappbar.utils

fun <K, V> Map<K, V>.joinToString( joiner: (Map.Entry<K, V>) -> String ) = StringBuilder().run {
    var count = 0
    this@joinToString.forEach { entry ->
        count ++
        append( joiner( entry ) )
        if ( count < size ) append( ", " )
    }
    toString()
}