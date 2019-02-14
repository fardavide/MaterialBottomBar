package studio.forface.demo

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import studio.forface.materialbottombar.demo.R

class DemoActivity: AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )

        panelsDemoButton.setOnClickListener { startActivity<PanelsDemoActivity>() }
        navigationDemoButton.setOnClickListener { startActivity<NavigationDemoActivity>() }
    }
}

private inline fun <reified A: AppCompatActivity> ContextWrapper.startActivity() {
    startActivity( Intent(this, A::class.java ) )
}