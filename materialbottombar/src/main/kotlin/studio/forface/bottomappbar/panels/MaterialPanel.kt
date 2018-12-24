package studio.forface.bottomappbar.panels

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import studio.forface.bottomappbar.appbar.MaterialBottomAppBar
import studio.forface.bottomappbar.drawer.MaterialDrawer
import studio.forface.bottomappbar.panels.holders.*
import studio.forface.bottomappbar.panels.items.BasePanelItem
import studio.forface.bottomappbar.panels.items.PanelItem
import studio.forface.bottomappbar.panels.params.*
import studio.forface.bottomappbar.utils.Drawables
import studio.forface.bottomappbar.view.PanelView
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Davide Giuseppe Farella.
 * A Panel that can appear from a [MaterialBottomAppBar]
 *
 * Inherit from [Observable]
 */
open class MaterialPanel(
    _header: IHeader? = null,
    _body: IBody? = null,
    _wrapToContent: Boolean = true
): Observable() {

    /** The [IBody] for the panel. On Set: [notifyChange] */
    var body = _body
        set( value ) {
            field = value
            notifyChange( Change.BODY )
        }

    /** The [IHeader] for the panel. On Set: [notifyChange] */
    var header = _header
        set( value ) {
            field = value
            notifyChange( Change.HEADER )
        }

    /**
     * Get and Set the [PanelView] through a [WeakReference]
     * @see panelViewWR
     */
    var panelView: PanelView?
        get() = panelViewWR.get()
        set( value ) { panelViewWR = WeakReference( value ) }

    /** A [WeakReference] to [PanelView] */
    private var panelViewWR: WeakReference<PanelView?> = WeakReference(null )

    /**
     * A [Boolean] that represents whether the panel, when opened, should have the same height of
     * the [ViewGroup.children].
     * If false, the expanded panel, will have an height equal to 2/3 of the Window.
     */
    var wrapToContent = _wrapToContent
        set( value ) {
            field = value
            notifyChange( Change.PANEL_VIEW )
        }

    /** @see Observable.deleteObservers */
    override fun deleteObservers() {
        super.deleteObservers()
        ( header as? Observable )?.deleteObservers()
        ( body   as? Observable )?.deleteObservers()
    }

    /**
     * Notify when some params changes.
     * @see Observable.setChanged
     * @see Observable.notifyObservers
     */
    private fun notifyChange( change: Change ) {
        setChanged()
        notifyObservers( change )
    }

    /**
     * Observe changes.
     * Wraps [Observable.addObserver] casting the [Observable] as [MaterialPanel] and the change
     * `any` as [Change]
     */
    internal inline fun observe( crossinline change: (MaterialPanel, Change) -> Unit ) {
        addObserver { observable, any -> change( observable as MaterialPanel, any as Change ) }
    }


    /** A common interface for an header for [MaterialPanel] */
    interface IHeader

    /**
     * An abstract class that implements business logic of a base header.
     * This class is needed since if will be inherited from [MaterialDrawer.Header]
     *
     * Inherit from [IHeader], [Background], [Title] and [Icon]
     */
    abstract class BaseHeader<T> internal constructor(): IHeader,
            Background<T>, Title<T>, Icon<T> {

        /** @see Background.backgroundColorHolder */
        override var backgroundColorHolder = ColorHolder()
        /** @see Background.backgroundCornerRadiusSizeHolder */
        override var backgroundCornerRadiusSizeHolder = SizeHolder()

        /** @see Title.titleTextHolder */
        override var titleTextHolder = TextHolder()
        /** @see Title.titleTextStyleHolder */
        override var titleTextStyleHolder = TextStyleHolder( bold = true )
        /** @see Title.titleTextSizeHolder */
        override var titleTextSizeHolder = TextSizeHolder()
        /** @see Title.titleColorHolder */
        override var titleColorHolder = ColorHolder()

        /** @see Icon.iconImageHolder */
        override var iconImageHolder = ImageHolder()
        /** @see Icon.iconColorHolder */
        override var iconColorHolder = ColorHolder()
        /** @see Icon.iconSizeHolder */
        override var iconSizeHolder = SizeHolder()

        /** A [Boolean] for keep track whether a custom [ImageShape] has been set */
        private var hasCustomShape = false

        /** @see Icon.iconShape */
        override fun iconShape( imageShape: ImageShape ): T {
            hasCustomShape = true
            return super.iconShape( imageShape )
        }

        /** @see Icon.applyIconTo */
        override fun applyIconTo( imageView: ImageView, applyOrHide: Boolean ) {
            if ( ! hasCustomShape ) iconImageHolder.imageShape = ImageShape.ROUND
            super.applyIconTo( imageView,true )
        }
    }

    /** A solid implementation of [BaseHeader] */
    class Header: BaseHeader<Header>() {
        override val thisRef: Header = this
    }

    /**
     * An implementation of [IHeader] for a type of header that has a custom [View] as content.
     * Inherit from [IHeader] and [Observable]
     */
    open class CustomHeader( _contentView: View ): Observable(), IHeader {
        /**
         * The custom content [View] for the header. On Set: [Observable.setChanged] and
         * [Observable.notifyObservers]
         */
        var contentView = _contentView
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }
    }


    /** A common interface for the body of [MaterialPanel] */
    interface IBody

    /**
     * An abstract class that implements business logic of a base body.
     * This class is needed since if will be inherited from [MaterialDrawer.Body]
     *
     * Inherit from [IBody] and [Selection]
     */
    abstract class BaseBody<T> internal constructor( _items: List<PanelItem> = listOf() ) :
            Observable(), IBody, Selection<T> {

        /** @see Selection.selectionColorHolder */
        override var selectionColorHolder = ColorHolder()
        /** @see Selection.selectionCornerRadiusSizeHolder */
        override var selectionCornerRadiusSizeHolder = SizeHolder( dp = Drawables.CORNER_RADIUS_SOFT )
        /** @see Selection.onItemClickListener */
        override var onItemClickListener: OnItemClickListener = { _, _ ->  }

        /**
         * A [List] of [PanelItem] for the body. On Set: [Observable.setChanged] and
         * [Observable.notifyObservers]
         */
        var items = _items
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }

        /** A builder-style function for update [BaseBody.items] */
        fun items( items: List<PanelItem> ) = apply { this.items = items }

        /** Select a [PanelItem] in [items] and de-select all th others */
        fun setSelected( selectedId: Int? ) = apply {
            items = items.mapBasePanelItems { it.selected = it.id == selectedId && it.selectable }
        }
    }

    /** A solid implementation of [BaseBody] */
    class Body( items: List<PanelItem> = listOf() ): BaseBody<Body>( items ) {
        override val thisRef: Body get() = this
    }

    /**
     * An implementation of [IBody] for a type of body that has a custom [View] as content.
     * Inherit from [IBody] and [Observable]
     */
    open class CustomBody( _contentView: View ): Observable(), IBody {
        /**
         * The custom content [View] for the body. On Set: [Observable.setChanged] and
         * [Observable.notifyObservers]
         */
        var contentView = _contentView
            set( value ) {
                field = value
                setChanged()
                notifyObservers()
            }
    }

    /** An enum representing the changing item in a [MaterialPanel] */
    internal enum class Change {
        HEADER, BODY, PANEL_VIEW
    }
}

/**
 * A function for map [BasePanelItem]s from a [List] of generic [PanelItem]s.
 * @return [List] of [PanelItem]
 */
inline fun List<PanelItem>.mapBasePanelItems( mapper: (BasePanelItem) -> Unit ) =
        this.map { ( it as? BasePanelItem )?.apply { mapper( this ) } ?: it }

/** Call [Collection.forEach] only for [BasePanelItem]s in a [List] of [PanelItem]s */
inline fun List<PanelItem>.forEachBasePanelItem( block: (BasePanelItem) -> Unit ) {
    forEach { ( it as? BasePanelItem )?.run( block ) }
}