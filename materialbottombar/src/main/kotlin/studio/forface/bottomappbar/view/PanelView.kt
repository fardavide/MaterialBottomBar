package studio.forface.bottomappbar.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.drawer_header.view.*
import studio.forface.bottomappbar.layout.MaterialBottomDrawerLayout
import studio.forface.bottomappbar.panels.MaterialPanel
import studio.forface.bottomappbar.utils.elevationCompat
import studio.forface.materialbottombar.bottomappbar.R

class PanelView @JvmOverloads constructor (
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
): LinearLayout( context, attrs, defStyleAttr ) {

    constructor( layout: MaterialBottomDrawerLayout, panel: MaterialPanel ) : this(
            layout.context, layout.attrs, layout.defStyleAttr
    ) {
        init( layout, panel )
    }

    internal lateinit var header:       View
    internal lateinit var body:         View
    internal lateinit var background:   View

    fun init( layout: MaterialBottomDrawerLayout, panel: MaterialPanel ) {
        orientation = LinearLayout.VERTICAL

        header =        buildHeader( layout, panel.header )
        body =          buildBody( panel.body )
        background =    buildBackground()

        addView( header )
        layout.bottomAppBar?.height?.let { header.layoutParams.height = it }

        addView( body )
        body.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

        addView( background )
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