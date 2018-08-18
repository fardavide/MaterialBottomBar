package studio.forface.bottomappbar.materialpanels.params

interface Identifier<T>: Param<T> {
    var id: Int

    fun id( id: Int ) =
            thisRef.apply { this@Identifier.id = id }
}