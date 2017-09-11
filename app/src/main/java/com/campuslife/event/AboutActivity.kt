package com.campuslife.event

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar_about)
        setTitle("版本号：1.0.2")
        toolbar_about.setNavigationOnClickListener { view -> this.finish() }

    }

}
