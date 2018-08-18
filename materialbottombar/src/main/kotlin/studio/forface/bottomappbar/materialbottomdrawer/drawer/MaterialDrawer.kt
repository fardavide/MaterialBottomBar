package studio.forface.bottomappbar.materialbottomdrawer.drawer

import android.widget.ImageView
import studio.forface.bottomappbar.materialbottomdrawer.draweritems.DrawerItem
import studio.forface.bottomappbar.materialpanels.holders.*
import studio.forface.bottomappbar.materialpanels.panelitems.BasePanelItem
import studio.forface.bottomappbar.materialpanels.panelitems.PanelItem
import studio.forface.bottomappbar.materialpanels.params.*
import studio.forface.bottomappbar.utils.Drawables
import java.util.*

class MaterialDrawer(
    _header: Header? = null,
    _body: Body? = null
): Observable() {

    var header = _header
        set(value) {
            field = value
            setChanged()
            notifyObservers()
        }
    var body = _body
        set(value) {
            field = value
            notifyChange( Change.BODY )
        }

    private fun notifyChange( change: Change ) {
        setChanged()
        notifyObservers( change )
    }

    internal fun observe( change: (MaterialDrawer, Change) -> Unit ) {
        addObserver { observable, any -> change( observable as MaterialDrawer, any as Change ) }
    }

    class Header:
            Background<Header>,
            Title<Header>,
            Icon<Header>
    {
        override val thisRef: Header get() = this

        override var backgroundColorHolder =            ColorHolder()
        override var backgroundCornerRadiusSizeHolder =  SizeHolder()

        override var titleTextHolder =                  TextHolder()
        override var titleTextStyleHolder =             TextStyleHolder()
        override var titleTextSizeHolder =              TextSizeHolder()
        override var titleColorHolder =                 ColorHolder()

        override var iconImageHolder =                  ImageHolder()
        override var iconColorHolder =                  ColorHolder()
        override var iconSizeHolder =                   SizeHolder()

        private var hasCustomShape = false

        override fun iconShape( imageShape: ImageShape ) {
            super.iconShape( imageShape )
            hasCustomShape = true
        }

        override fun applyIconTo( imageView: ImageView, applyOrHide: Boolean ) {
            if ( ! hasCustomShape ) iconImageHolder.imageShape = ImageShape.ROUND
            super.applyIconTo( imageView,true )
        }
    }

    class Body(
        _items: List<PanelItem> = listOf()
    ): Observable(), Selection<Body> {
        override val thisRef: Body get() = this

        override var selectionColorHolder =             ColorHolder()
        override var selectionCornerRadiusSizeHolder =  SizeHolder( dp = Drawables.CORNER_RADIUS_SOFT )

        override var onItemClickListener: OnItemClickListener = { _, _ ->  }

        var items = _items
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }

        fun items( items: List<PanelItem> ) =
                apply { this.items = items }

        fun setSelected( selectedId: Int ) = apply {
            items = items.mapBasePanelItems { it.selected = it.id == selectedId && it.selectable }
        }
    }

    internal sealed class Change {
        object HEADER :             Change()
        object BODY:                Change()
        class PANEL( val id: Int ): Change()
    }
}

fun List<PanelItem>.mapBasePanelItems(mapper: (BasePanelItem) -> Unit ) =
        this.map { ( it as? BasePanelItem )?.apply { mapper( this ) } ?: it }

fun List<PanelItem>.forEachBasePanelItem(block: (BasePanelItem) -> Unit ) {
    forEach { ( it as? BasePanelItem )?.run( block ) }
}