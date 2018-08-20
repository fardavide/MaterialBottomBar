package studio.forface.bottomappbar.panels

import android.view.View
import android.widget.ImageView
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.items.BasePanelItem
import studio.forface.bottomappbar.panels.items.PanelItem
import studio.forface.bottomappbar.panels.params.*
import studio.forface.bottomappbar.utils.Drawables
import studio.forface.bottomappbar.view.PanelView
import java.lang.ref.WeakReference
import java.util.*

open class MaterialPanel(
        _header: IHeader? = null,
        _body: IBody? = null,
        _wrapToContent: Boolean = true
): Observable() {

    var header = _header
        set(value) {
            field = value
            notifyChange( Change.HEADER )
        }
    var body = _body
        set(value) {
            field = value
            notifyChange( Change.BODY )
        }

    private var panelViewWR: WeakReference<PanelView?> = WeakReference(null )
    var panelView: PanelView?
        get() = panelViewWR.get()
        set( value ) { panelViewWR = WeakReference( value ) }

    var wrapToContent = _wrapToContent
        set( value ) {
            field = value
            notifyChange( Change.PANEL )
        }

    private fun notifyChange( change: Change ) {
        setChanged()
        notifyObservers( change )
    }

    internal fun observe( change: (MaterialPanel, Change) -> Unit ) {
        addObserver { observable, any -> change( observable as MaterialPanel, any as Change ) }
    }

    override fun deleteObservers() {
        super.deleteObservers()
        ( header as? Observable )?.deleteObservers()
        ( body   as? Observable )?.deleteObservers()
    }

    interface IHeader

    abstract class AbsHeader<T>:
            Background<T>,
            Title<T>,
            Icon<T>,
            IHeader
    {
        override var backgroundColorHolder =            ColorHolder()
        override var backgroundCornerRadiusSizeHolder = SizeHolder()

        override var titleTextHolder =                  TextHolder()
        override var titleTextStyleHolder =             TextStyleHolder( bold = true )
        override var titleTextSizeHolder =              TextSizeHolder()
        override var titleColorHolder =                 ColorHolder()

        override var iconImageHolder =                  ImageHolder()
        override var iconColorHolder =                  ColorHolder()
        override var iconSizeHolder =                   SizeHolder()

        private var hasCustomShape = false

        override fun iconShape( imageShape: ImageShape) {
            super.iconShape( imageShape )
            hasCustomShape = true
        }

        override fun applyIconTo(imageView: ImageView, applyOrHide: Boolean ) {
            if ( ! hasCustomShape ) iconImageHolder.imageShape = ImageShape.ROUND
            super.applyIconTo( imageView,true )
        }
    }

    class Header: AbsHeader<Header>() {
        override val thisRef: Header = this
    }

    open class CustomHeader( _contentView: View ): Observable(), IHeader {
        var contentView = _contentView
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }
    }



    interface IBody

    abstract class AbsBody<T>(
            _items: List<PanelItem> = listOf()
    ): Observable(), IBody, Selection<T> {

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

        fun setSelected( selectedId: Int? ) = apply {
            items = items.mapBasePanelItems { it.selected = it.id == selectedId && it.selectable }
        }
    }

    class Body( items: List<PanelItem> = listOf() ): AbsBody<Body>( items ) {
        override val thisRef: Body get() = this
    }

    open class CustomBody( _contentView: View ): Observable(), IBody {
        var contentView = _contentView
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }
    }

    internal sealed class Change {
        object HEADER:      Change()
        object BODY:        Change()
        object PANEL:       Change()
    }
}

fun List<PanelItem>.mapBasePanelItems( mapper: (BasePanelItem) -> Unit ) =
        this.map { ( it as? BasePanelItem )?.apply { mapper( this ) } ?: it }

fun List<PanelItem>.forEachBasePanelItem( block: (BasePanelItem) -> Unit ) {
    forEach { ( it as? BasePanelItem )?.run( block ) }
}