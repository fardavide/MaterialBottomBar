@file:Suppress("FunctionName")

package studio.forface.materialbottombar.panels

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import studio.forface.materialbottombar.appbar.MaterialBottomAppBar
import studio.forface.materialbottombar.panels.adapter.ItemViewHolder
import studio.forface.materialbottombar.panels.holders.*
import studio.forface.materialbottombar.panels.items.PanelItem
import studio.forface.materialbottombar.panels.params.*
import studio.forface.materialbottombar.utils.Drawables
import studio.forface.materialbottombar.view.PanelView
import java.lang.ref.WeakReference
import java.util.*
import studio.forface.materialbottombar.panels.items.BasePanelItem as BasePanelItemT

/**
 * @author Davide Giuseppe Farella.
 * A Panel that can appear from a [MaterialBottomAppBar]
 *
 * Inherit from [Observable]
 */
abstract class AbsMaterialPanel (
        _header: IHeader?,
        _body: IBody?,
        _wrapToContent: Boolean
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
    inline fun observe( crossinline change: (AbsMaterialPanel, Change) -> Unit ) {
        addObserver { observable, any -> change( observable as AbsMaterialPanel, any as Change ) }
    }


    /** A common interface for an header for [MaterialPanel] */
    interface IHeader

    /**
     * An abstract class that implements business logic of a base header.
     * This class is needed since if will be inherited from [ MaterialDrawer.Header]
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
    abstract class BaseBody<T: BaseBody<T>>( _items: List<PanelItem> = listOf() )
        : Observable(), IBody, Selection<T> {

        companion object {
            /**
             * A delay before the item should be set as selected, for let the ripple animation
             * finish first
             */
            const val SELECTION_DELAY_MS = 200L
        }

        /** @see Selection.selectionColorHolder */
        override var selectionColorHolder = ColorHolder()
        /** @see Selection.selectionCornerRadiusSizeHolder */
        override var selectionCornerRadiusSizeHolder = SizeHolder( dp = Drawables.CORNER_RADIUS_SOFT )
        /** @see Selection.onItemClick */
        override var onItemClick: OnItemClickListener = { _, _ ->  }

        /** A [Boolean] representing whether the Panel must be closed when an item in clicked */
        var closeOnClick = false

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

        /** @return an [ItemViewHolder] for this [Body] */
        @Suppress("UNCHECKED_CAST")
        open fun <VH: ItemViewHolder<*>> createViewHolder( itemView: View, closePanel: () -> Unit ) =
                ItemViewHolder( itemView,this, closePanel ) as VH

        /** A builder-style function for update [Body.items] */
        fun items( items: List<PanelItem> ) = thisRef.apply { this@BaseBody.items = items }

        /** Select a [PanelItem] by its [BasePanelItemT.id] in [items] and de-select all th others */
        fun setSelected( selectedId: Int? ) = apply {
            items = items.mapBasePanelItems { it.selected = it.id == selectedId && it.selectable }
        }

        /** Select a [PanelItem] by its [find] lambda in [items] and de-select all th others */
        fun setSelected( find: (BasePanelItem) -> Boolean ) = apply {
            items = items.mapBasePanelItems { it.selected = find( it ) && it.selectable }
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
    enum class Change {
        HEADER, BODY, PANEL_VIEW
    }
}

/**
 * A function for map [BasePanelItem]s from a [List] of generic [PanelItem]s.
 * @return [List] of [PanelItem]
 */
private inline fun List<PanelItem>.mapBasePanelItems( mapper: (BasePanelItem) -> Unit ) =
        this.map { ( it as? BasePanelItem )?.apply { mapper( this ) } ?: it }

/** Call [Collection.forEach] only for [BasePanelItem]s in a [List] of [PanelItem]s */
private inline fun List<PanelItem>.forEachBasePanelItem( block: (BasePanelItem) -> Unit ) {
    forEach { ( it as? BasePanelItem )?.run( block ) }
}

/** A type alias for a avoid to spread `<*>` everywhere */
private typealias BasePanelItem = BasePanelItemT<*>

/** @constructor of [AbsMaterialPanel] for Panel */
@Suppress("FunctionName")
fun MaterialPanel(
        header: AbsMaterialPanel.IHeader? = null,
        body: AbsMaterialPanel.IBody? = null,
        wrapToContent: Boolean = true
) = object : AbsMaterialPanel( header, body, wrapToContent ) {}

/** A set of constructors for Header and Body of [AbsMaterialPanel] for Panel */
@Suppress("unused")
object MaterialPanel {
    fun Header() = AbsMaterialPanel.Header()
    fun Body() = AbsMaterialPanel.Body()
    fun CustomHeader( contentView: View ) = AbsMaterialPanel.CustomHeader( contentView )
    fun CustomBody( contentView: View ) = AbsMaterialPanel.CustomBody( contentView )
}

/** @constructor of [AbsMaterialPanel] for Drawer */
fun MaterialDrawer(
        header: AbsMaterialPanel.IHeader? = null,
        body: AbsMaterialPanel.IBody? = null,
        wrapToContent: Boolean = false
) = MaterialPanel( header, body, wrapToContent )

/** A set of constructors for Header and Body of [AbsMaterialPanel] for Drawer */
@Suppress("unused")
object MaterialDrawer {
    fun Header() = AbsMaterialPanel.Header()
    fun Body() = AbsMaterialPanel.Body()
    fun CustomHeader( contentView: View ) = AbsMaterialPanel.CustomHeader( contentView )
    fun CustomBody( contentView: View ) = AbsMaterialPanel.CustomBody( contentView )
}