package studio.forface.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import studio.forface.materialbottombar.demo.R

class DemoActivity: AppCompatActivity() {

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_demo )

        panelsDemoButton.setOnClickListener {
            startActivity( Intent(this, PanelsDemoActivity::class.java ) )
        }
    }
}