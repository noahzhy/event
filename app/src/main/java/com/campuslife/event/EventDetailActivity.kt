package com.campuslife.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.campuslife.event.adapter.MainAdapter
import com.campuslife.event.sqliteDatabase.EVENTINFO
import com.orm.SugarContext
import com.orm.SugarRecord
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_event_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.yanzhenjie.album.AlbumFile


class EventDetailActivity : AppCompatActivity() {

    private var currentPosition: Int = 0
    private var cover_photo: String = ""

    private var currentTitle: String = ""
    private var currentStartTime: String = ""
    private var currentEndTime: String = ""
    private var currentPhotosList: String = ""
    private var currentAddress: String = ""

    var deleteId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(event_detail_toolbar)
        event_detail_toolbar.setNavigationOnClickListener { view -> this.finish() }
        var intent: Intent = intent
        currentPosition = intent.extras.getInt("currentPosition")
        findData(currentPosition)
        switch_event_all_day.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                alert("该功能仍在测试阶段，可能会有异常", "自动匹配照片") {
                    positiveButton("开启") { toast("匹配异常，请检查后重试") }
                    negativeButton("取消") {
                        switch_event_all_day.isChecked = false
                    }
                }.show()
            }
        }
    }

    fun findData(current: Int) {
        SugarContext.init(this)
        var list: List<EVENTINFO> = SugarRecord.listAll(EVENTINFO::class.java)
        currentTitle = list[current].TITLE
        title = currentTitle
        var timeStart: List<String> = list[current].TIMESTART.split(":")
        var timeEnd: List<String> = list[current].TIMEEND.split(":")

        currentStartTime = list[current].TIMESTART
        currentEndTime = list[current].TIMEEND
        currentPhotosList = list[current].PHOTOS
        currentAddress = list[current].ADDRESS

        tv_date.text = timeStart[0] + "年" + (timeStart[1].toInt() + 1).toString() + "月" + timeStart[2] + "日"
        tv_date2.text = timeEnd[0] + "年" + (timeEnd[1].toInt() + 1).toString() + "月" + timeEnd[2] + "日"
        var linkStartTime: String = timeStart[3] + ":" + timeStart[4]
        var linkEndTime: String = timeEnd[3] + ":" + timeEnd[4]
        if ((linkStartTime == "00:00") and (linkEndTime == "23:59")) {
        } else {
            tv_time.text = timeStart[3] + ":" + timeStart[4]
            tv_time2.text = timeEnd[3] + ":" + timeEnd[4]
        }
        if (currentAddress == "") {
            imageView4.visibility = View.GONE
            divider1.visibility = View.GONE
        } else {
            imageView4.visibility = View.VISIBLE
            divider1.visibility = View.VISIBLE
        }
        event_add_add.text = currentAddress
        cover_photo = list[current].PHOTOS
        val gson = Gson()
        val type = object : TypeToken<ArrayList<AlbumFile>>() {}.type
        val finalOutputPhotosArrayList: ArrayList<AlbumFile> = gson.fromJson(cover_photo, type)
        var photosListEx: ArrayList<String> = ArrayList()
        finalOutputPhotosArrayList.mapTo(photosListEx) { it.path }

        Glide.with(this).load("file://" + photosListEx[0]).into(squareImageView_main_photo)

        var mRecyclerViewAdapter = MainAdapter(photosListEx)
        var manager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false)
        event_detail_photos_rv.layoutManager = manager
        mRecyclerViewAdapter.setOnItemClickListener { pos ->
            Glide.with(this).load("file://" + photosListEx[pos]).into(squareImageView_main_photo)
        }
        event_detail_photos_rv.adapter = mRecyclerViewAdapter

        var eventInfo = SugarRecord.find(
                EVENTINFO::class.java,
                "TITLE = ? and TIMESTART = ? and TIMEEND = ? and ADDRESS = ? and PHOTOS = ?",
                currentTitle, currentStartTime, currentEndTime, currentAddress, currentPhotosList)
        deleteId = eventInfo[0].id.toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_event_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_edit -> {
                var intent1 = Intent(this@EventDetailActivity, EventActivity::class.java)
                intent1.putExtra("fix", 1)
                intent1.putExtra("id", deleteId)
                startActivityForResult(intent1, 3)
                return true
            }
            R.id.action_delete -> {
                alert("确定要彻底删除该活动吗？", "删除活动") {
                    positiveButton("确定") {
                        SugarRecord.findById(EVENTINFO::class.java, deleteId).delete()
                        this@EventDetailActivity.setResult(Activity.RESULT_OK)
                        this@EventDetailActivity.finish()
                    }
                    negativeButton("取消") {
                    }
                }.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            findData(currentPosition)
        }
    }
}
