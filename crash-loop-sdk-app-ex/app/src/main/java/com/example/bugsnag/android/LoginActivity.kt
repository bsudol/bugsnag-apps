package com.example.bugsnag.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class LoginActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("entrypoint")
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        setupToolbarLogo()
    }

    private fun setupToolbarLogo() {
        val supportActionBar = supportActionBar

        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true)
            supportActionBar.setIcon(R.drawable.ic_bugsnag_svg)
            supportActionBar.title = null
        }
    }
}