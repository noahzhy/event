package com.campuslife.event

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.campuslife.event.sqliteDatabase.EVENTINFO
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { view -> this.finish() }
        title = "欢迎使用"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_welcome, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete -> {
                SugarRecord.findById(EVENTINFO::class.java, 0).delete()
                this.setResult(Activity.RESULT_OK)
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
