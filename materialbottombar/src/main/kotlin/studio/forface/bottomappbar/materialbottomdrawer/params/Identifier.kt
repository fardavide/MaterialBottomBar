package studio.forface.bottomappbar.materialbottomdrawer.params

interface Identifier<T>: Param<T> {
    var id: Int

    fun withId( id: Int ) =
            thisRef.apply { this@Identifier.id = id }
}