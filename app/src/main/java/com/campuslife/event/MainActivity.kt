package com.campuslife.event

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.arlib.floatingsearchview.FloatingSearchView
import com.campuslife.event.adapter.MyAdapter
import com.campuslife.event.adapter.MyAdapter.OnItemClickListener
import com.campuslife.event.sqliteDatabase.EVENTINFO
import com.orm.SugarContext
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_event.*
import java.util.*
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.yanzhenjie.album.AlbumFile
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var data_date: ArrayList<String> = ArrayList()
    private var data_week: ArrayList<String> = ArrayList()
    private var data_title: ArrayList<String> = ArrayList()
    private var data_time: ArrayList<String> = ArrayList()
    private var data_add: ArrayList<String> = ArrayList()
    private var data_cover_photo: ArrayList<String> = ArrayList()

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var firstTime: Boolean = false

    var coverPhoto: String = ""

    val gson = Gson()
    val type = object : TypeToken<ArrayList<AlbumFile>>() {}.type


    var requestCode: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferences = getSharedPreferences("userSettings", 0)
        firstTime = preferences.getBoolean("firstTime", true)
        if (firstTime) {
            initData()
        }
        editor = preferences.edit()
        editor.putBoolean("firstTime", false)
        editor.commit()

        floating_search_view.attachNavigationDrawerToMenuButton(drawer_layout)
        floating_search_view.setOnQueryChangeListener(FloatingSearchView.OnQueryChangeListener { oldQuery, newQuery ->
            //get suggestions based on newQuery
            //pass them on to the search view
//            floating_search_view.swapSuggestions(newSuggestions)
        })

        fab.setOnClickListener { view -> startActivityForResult(Intent(this, EventActivity::class.java), 0) }
        val toggle = ActionBarDrawerToggle(this, drawer_layout, main_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        updata()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_info -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_setting -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun returnWeek(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        var week: String = ""
        val c = year / 100
        var y = year % 100
        var m = monthOfYear + 1
        if (m == 1 || m == 2) {
            y = year - 1
            m = monthOfYear + 12
        }
        // 运用Zeller公式计算星期
        var w = (y + y / 4 + c / 4 - 2 * c
                + 26 * (m + 1) / 10 + dayOfMonth - 1) % 7
        if (w < 0) {
            w += 7
        }
        if (monthOfYear === 0 || monthOfYear === 1) {
            w += 2
        }
        if (w >= 7) {
            w = w % 7
        }
        when (w) {
            0 -> week = "日"
            1 -> week = "一"
            2 -> week = "二"
            3 -> week = "三"
            4 -> week = "四"
            5 -> week = "五"
            6 -> week = "六"
            else -> {
                return "日"
            }
        }
        week = "周" + week
        return week
    }

    fun initData() {
        try {
            SugarContext.init(this)
            var eventInfo = EVENTINFO(
                    "欢迎使用",
                    year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + "00:00",
                    year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + "23:59",
                    "",
                    "/android_asset/welcome.jpg")
            eventInfo.save()
            Log.w("ex", "add the init data successful")
        } catch (e: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            updata2()
        }
    }

    fun updata() {
        try {
            SugarContext.init(this)

            var list: List<EVENTINFO> = SugarRecord.listAll(EVENTINFO::class.java)
            var timeStart: String = ""
            var timeEnd: String = ""

            for (x in list) {
                timeStart = x.TIMESTART
                timeEnd = x.TIMEEND
                var yearMonthDay: List<String> = timeStart.split(":")
                var yearMonthDay2: List<String> = timeEnd.split(":")
                data_week.add(returnWeek(yearMonthDay[0].toInt(), yearMonthDay[1].toInt(), yearMonthDay[2].toInt()))
                data_time.add(yearMonthDay[3] + ":" + yearMonthDay[4] + "-" + yearMonthDay2[3] + ":" + yearMonthDay2[4])
                data_date.add(yearMonthDay[2])
                data_title.add(x.TITLE)
                data_add.add(x.ADDRESS)
                coverPhoto = x.PHOTOS
                if (coverPhoto == "/android_asset/welcome.jpg") {
                    data_cover_photo.add("/android_asset/welcome.jpg")
                } else {
                    try {
                        val finalOutputCoverPhoto: ArrayList<AlbumFile> = gson.fromJson(coverPhoto, type)
                        Log.e("<<<<<<<<<<$$$$$$$$$$$", finalOutputCoverPhoto[0].path.toString())
                        data_cover_photo.add(finalOutputCoverPhoto[0].path.toString())
                    } catch (e: Exception) {
                        Log.e("<<<<<<<<<<", e.toString())
                    }
                }
//                var photosListEx: ArrayList<String> = ArrayList()
//                finalOutputCoverPhoto.mapTo(photosListEx) { it.path }
//                Log.e(">>>>>>>>>>",photosListEx[0].toString())
            }
        } catch (e: Exception) {
        }


        content_main_recyclerview.layoutManager = LinearLayoutManager(this)
        var mAdapter = MyAdapter(
                data_date,
                data_week,
                data_title,
                data_time,
                data_add,
                data_cover_photo
        )
//
        mAdapter.setOnItemClickListener { pos ->
            adapterItemClickListener(pos)
        }

        content_main_recyclerview.adapter = mAdapter
    }

    fun updata2() {
        try {
            SugarContext.init(this)
            var list: List<EVENTINFO> = SugarRecord.listAll(EVENTINFO::class.java)
            var timeStart: String = ""
            var timeEnd: String = ""
            data_week.clear()
            data_time.clear()
            data_date.clear()
            data_title.clear()
            data_add.clear()
            data_cover_photo.clear()
            for (x in list) {
                timeStart = x.TIMESTART
                timeEnd = x.TIMEEND
                var yearMonthDay: List<String> = timeStart.split(":")
                var yearMonthDay2: List<String> = timeEnd.split(":")

                data_week.add(returnWeek(yearMonthDay[0].toInt(), yearMonthDay[1].toInt(), yearMonthDay[2].toInt()))
                data_time.add(yearMonthDay[3] + ":" + yearMonthDay[4] + "-" + yearMonthDay2[3] + ":" + yearMonthDay2[4])
                data_date.add(yearMonthDay[2])
                data_title.add(x.TITLE)
                data_add.add(x.ADDRESS)
                coverPhoto = x.PHOTOS
                if (coverPhoto == "/android_asset/welcome.jpg") {
                    data_cover_photo.add("/android_asset/welcome.jpg")
                } else {
                    try {
                        val finalOutputCoverPhoto: ArrayList<AlbumFile> = gson.fromJson(coverPhoto, type)
                        Log.e("<<<<<<<<<<$$$$$$$$$$$", finalOutputCoverPhoto[0].path.toString())
                        data_cover_photo.add(finalOutputCoverPhoto[0].path.toString())
                    } catch (e: Exception) {
                        Log.e("<<<<<<<<<<", e.toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("rcex>>>>>>>>", e.toString())
        }

        content_main_recyclerview.layoutManager = LinearLayoutManager(this)
        var mAdapter = MyAdapter(
                data_date,
                data_week,
                data_title,
                data_time,
                data_add,
                data_cover_photo
        )

        mAdapter.setOnItemClickListener { pos ->
            adapterItemClickListener(pos)
        }

        content_main_recyclerview.adapter = mAdapter
    }

    fun adapterItemClickListener(pos: Int) {
        if (pos == 0) {
            var intent: Intent = Intent(this, WelcomeActivity::class.java)
            startActivityForResult(intent, requestCode)
        } else {
            var intent: Intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("currentPosition", pos)
            startActivityForResult(intent, requestCode)
        }
    }

}
