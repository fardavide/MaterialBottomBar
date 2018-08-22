package studio.forface.bottomappbar.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_header.view.*
import studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.utils.children
import studio.forface.bottomappbar.utils.elevationCompat
import studio.forface.materialbottombar.bottomappbar.R

class PanelView @JvmOverloads constructor (
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): LinearLayout( context, attrs, defStyleAttr ) {

    internal var wrapToContent: Boolean = false
    internal val contentHeight get() = if ( wrapToContent )
        header.height + body.height
    else height

    constructor(
            layout: MaterialBottomDrawerLayout, panel: MaterialPanel
    ) : this( layout.context, layout.attrs, layout.defStyleAttr ) {
        init( layout, panel )
    }

    internal lateinit var header:       View
    internal lateinit var body:         View
    internal lateinit var background:   View

    fun init( layout: MaterialBottomDrawerLayout, panel: MaterialPanel ) {
        wrapToContent = panel.wrapToContent
        orientation = LinearLayout.VERTICAL
        y = layout.height.toFloat()

        setHeader( layout, panel.header )
        setBody( panel.body )
        setBackground()
    }

    fun fadeHeader( alpha: Float, enabled: Boolean ) {
        header.children.forEach {
            it.alpha =          alpha
            it.isClickable =    enabled
            it.isEnabled =      enabled
        }
    }

    internal fun setHeader( layout: MaterialBottomDrawerLayout, panelHeader: MaterialPanel.IHeader? ) {
        if ( this::header.isInitialized ) removeView( header )
        header = buildHeader( layout, panelHeader )
        fadeHeader(0f,false )
        addView( header,0 )
        layout.bottomAppBar?.height?.let { header.layoutParams.height = it }
    }

    internal fun setBody( panelBody: MaterialPanel.IBody? ) {
        if ( this::body.isInitialized ) removeView( body )
        body = buildBody( panelBody )
        addView( body,1 )
        body.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
    }

    private fun setBackground() {
        if ( this::background.isInitialized ) removeView( background )
        background = buildBackground()
        addView( background,2 )
        background.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
    }

    private fun buildHeader( layout: MaterialBottomDrawerLayout, header: MaterialPanel.IHeader? )  =
            when ( header ) {

                is MaterialPanel.AbsHeader<*> -> LayoutInflater.from( context )
                        .inflate( R.layout.drawer_header,this, false )
                        .apply {
                            header_shadow.elevationCompat = 14f
                            elevationCompat = 8f
                            header_close.setOnClickListener { layout.closePanel() }
                        } as ConstraintLayout

                is MaterialPanel.CustomHeader -> header.contentView

                null -> View( context )

                else -> throw IllegalStateException( "header is instance of $header" )
            }

    private fun buildBody( body: MaterialPanel.IBody? ) =
            when( body ) {

                is MaterialPanel.AbsBody<*> -> LayoutInflater.from( context )
                        .inflate( R.layout.drawer_body,this, false )
                        as RecyclerView

                is MaterialPanel.CustomBody -> body.contentView

                null -> View( context )

                else -> throw IllegalStateException( "body is instance of $body" )
            }

    private fun buildBackground() = View( context ).apply {
        setBackgroundColor( Color.WHITE )
        elevationCompat = 1f
    }

}