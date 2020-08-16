package com.federicocotogno.minimalnotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.federicocotogno.minimalnotes.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        introAnimation()

        setupActionBarWithNavController(findNavController(R.id.main_fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun introAnimation() {

        v_blackScreen.animate().apply {
            duration = 3000
            alpha(0f)
        }.start()

        iv_starter_icon.animate().apply {
            duration = 1000

        }.withEndAction {
            iv_starter_icon.animate().apply {
                duration = 500
                scaleX(0f)
                scaleY(0f)
            }
        }.start()

    }
}