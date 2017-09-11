package com.campuslife.event

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_event.*
import java.util.*
import android.view.View
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter.createFromResource
import android.widget.*
import java.io.File
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.campuslife.event.sqliteDatabase.EVENTINFO
import com.orm.SugarContext
import java.text.SimpleDateFormat
import android.widget.Toast
import com.yanzhenjie.album.AlbumFile
import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.campuslife.event.adapter.MainAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orm.SugarRecord
import com.yanzhenjie.album.AlbumListener
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.api.widget.Widget
import org.jetbrains.anko.toast
import kotlin.collections.ArrayList


class EventActivity : AppCompatActivity() {

    val calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    var year2 = calendar.get(Calendar.YEAR)
    var month2 = calendar.get(Calendar.MONTH)
    var day2 = calendar.get(Calendar.DAY_OF_MONTH)
    var hour2 = calendar.get(Calendar.HOUR_OF_DAY)
    var minute2 = calendar.get(Calendar.MINUTE)
    //    var spinnerSelectedPosition = 0
    var switchSelectedStatus: Boolean = false

    //    private val PHOTO_REQUEST_TAKEPHOTO = 1// 拍照
    //    private val PHOTO_REQUEST_GALLERY = 2// 从相册中选择
    //    private val PHOTO_REQUEST_CUT = 3// 结果
    //    private val tempFile = File(Environment.getExternalStorageDirectory(), getPhotoFileName())

    var mAlbumFiles: ArrayList<AlbumFile> = ArrayList()
    var photosListEx: ArrayList<String> = ArrayList()
    lateinit var photosList: List<String>
    var albumFilesString: String = ""

    var fix: Int = 0
    var id: Int = 999999

    lateinit var mRecyclerViewAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { view -> this.finish() }
        title = ""
        SugarContext.init(this)

        var intent2: Intent = intent
        try {
            fix = intent2.extras.getInt("fix", 0)
            id = intent2.extras.getInt("id", 0)
        } catch (e: Exception) {
        }

        if (fix == 1) {//edit mode
            SugarContext.init(this)
            var loadEventInfo = SugarRecord.findById(EVENTINFO::class.java, id.toInt())
            Log.w(">>>>>>>>>>>>>>>>", id.toString())
            var timeStart: List<String> = loadEventInfo.TIMESTART.split(":")
            var timeEnd: List<String> = loadEventInfo.TIMEEND.split(":")
            year = timeStart[0].toInt()
            month = timeStart[1].toInt()
            day = timeStart[2].toInt()
            year2 = timeEnd[0].toInt()
            month2 = timeEnd[1].toInt()
            day2 = timeEnd[2].toInt()
            event_add_title.setText(loadEventInfo.TITLE)
            event_add_add.setText(loadEventInfo.ADDRESS)

            val gson = Gson()
            val type = object : TypeToken<ArrayList<AlbumFile>>() {}.type
            val finalOutputPhotosArrayList: ArrayList<AlbumFile> = gson.fromJson(loadEventInfo.PHOTOS, type)

            mAlbumFiles = finalOutputPhotosArrayList

            loadCheckedPhotosWithRecyclerView()


        } else {

        }


        //get the date now
        tv_date.text = year.toString() + "年" + (month + 1).toString() + "月" + day.toString() + "日"
        tv_date.setOnClickListener { view ->
            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, choosedyear, monthOfYear, dayOfMonth ->
                        year = choosedyear
                        month = monthOfYear
                        day = dayOfMonth
                        tv_date.text = choosedyear.toString() + "年" + (monthOfYear + 1).toString() + "月" + dayOfMonth.toString() + "日"

                        var a: Int = (year.toString() + month.toString() + day.toString()).toInt()
                        var b: Int = (year2.toString() + month2.toString() + day2.toString()).toInt()
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                            tv_date.setTextColor(resources.getColor(R.color.red))
                            tv_time.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tv_date.setTextColor(resources.getColor(R.color.black))
                            tv_date2.setTextColor(resources.getColor(R.color.black))
                            tv_time.setTextColor(resources.getColor(R.color.black))
                            tv_time2.setTextColor(resources.getColor(R.color.black))
                        }


                    }, year, month, day)
            datePickerDialog.show()

        }
        tv_date2.text = year2.toString() + "年" + (month2 + 1).toString() + "月" + day2.toString() + "日"
        tv_date2.setOnClickListener { view ->
            val datePickerDialog2 = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, choosedyear, monthOfYear, dayOfMonth ->
                        year2 = choosedyear
                        month2 = monthOfYear
                        day2 = dayOfMonth
                        tv_date2.text = choosedyear.toString() + "年" + (monthOfYear + 1).toString() + "月" + dayOfMonth.toString() + "日"

                        var a: Int = (year.toString() + month.toString() + day.toString()).toInt()
                        var b: Int = (year2.toString() + month2.toString() + day2.toString()).toInt()
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                            tv_date2.setTextColor(resources.getColor(R.color.red))
                            tv_time2.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tv_date.setTextColor(resources.getColor(R.color.black))
                            tv_date2.setTextColor(resources.getColor(R.color.black))
                            tv_time.setTextColor(resources.getColor(R.color.black))
                            tv_time2.setTextColor(resources.getColor(R.color.black))

                        }

                    }, year2, month2, day2)
            datePickerDialog2.show()
        }
        tv_time.text = fixTime(hour, minute)
        tv_time2.text = fixTime(hour2, minute2)
        tv_time.setOnClickListener { view ->
            val timePickerDialog = TimePickerDialog(this,
                    TimePickerDialog.OnTimeSetListener { view, choosedhour, choosedminute ->
                        hour = choosedhour
                        minute = choosedminute
                        tv_time.text = fixTime(hour, minute)

                        var a: Int = (year.toString() + month.toString() + day.toString()).toInt()
                        var b: Int = (year2.toString() + month2.toString() + day2.toString()).toInt()
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                            tv_date.setTextColor(resources.getColor(R.color.red))
                            tv_time.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tv_date.setTextColor(resources.getColor(R.color.black))
                            tv_date2.setTextColor(resources.getColor(R.color.black))
                            tv_time.setTextColor(resources.getColor(R.color.black))
                            tv_time2.setTextColor(resources.getColor(R.color.black))
                        }

                    }, hour, minute, true)
            timePickerDialog.show()
        }
        tv_time2.setOnClickListener { view ->
            val timePickerDialog2 = TimePickerDialog(this,
                    TimePickerDialog.OnTimeSetListener { view, choosedhour, choosedminute ->
                        hour2 = choosedhour
                        minute2 = choosedminute
                        tv_time2.text = fixTime(hour2, minute2)

                        var a: Int = (year.toString() + month.toString() + day.toString()).toInt()
                        var b: Int = (year2.toString() + month2.toString() + day2.toString()).toInt()
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                            tv_date2.setTextColor(resources.getColor(R.color.red))
                            tv_time2.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tv_date.setTextColor(resources.getColor(R.color.black))
                            tv_date2.setTextColor(resources.getColor(R.color.black))
                            tv_time.setTextColor(resources.getColor(R.color.black))
                            tv_time2.setTextColor(resources.getColor(R.color.black))
                        }

                    }, hour2, minute2, true)
            timePickerDialog2.show()
        }


        switch_event_all_day.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            switchSelectedStatus = b
            if (switchSelectedStatus) {
                tv_date.visibility = View.VISIBLE
                tv_time.visibility = View.GONE
                tv_date2.visibility = View.VISIBLE
                tv_time2.visibility = View.GONE
            } else {
                tv_date.visibility = View.VISIBLE
                tv_time.visibility = View.VISIBLE
                tv_date2.visibility = View.VISIBLE
                tv_time2.visibility = View.VISIBLE
            }
        })

        tv_add_photo_and_video.setOnClickListener { view ->
            choosePhotos()
        }

        event_rv_loader.setOnClickListener { view ->
            choosePhotos()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_done -> {
                SugarContext.init(this)
                if (event_add_title.text.toString().equals("")) {
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_LONG).show()
                } else {
                    var a: Int = (year.toString() + month.toString() + day.toString()).toInt()
                    var b: Int = (year2.toString() + month2.toString() + day2.toString()).toInt()
                    if (!switchSelectedStatus) {
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                        } else {

                            if (fix == 1) {
                                var eventInfoFix = SugarRecord.findById(EVENTINFO::class.java, id)
                                eventInfoFix.TITLE = event_add_title.text.toString()
                                eventInfoFix.TIMESTART = year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + fixTime(hour, minute)
                                eventInfoFix.TIMEEND = year2.toString() + ":" + month2.toString() + ":" + day2.toString() + ":" + fixTime(hour2, minute2)
                                eventInfoFix.ADDRESS = event_add_add.text.toString()
                                eventInfoFix.PHOTOS = albumFilesString
                                eventInfoFix.save()
                            } else {
                                var eventInfo = EVENTINFO(
                                        event_add_title.text.toString(),
                                        year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + fixTime(hour, minute),
                                        year2.toString() + ":" + month2.toString() + ":" + day2.toString() + ":" + fixTime(hour2, minute2),
                                        event_add_add.text.toString(),
                                        albumFilesString)
                                eventInfo.save()
                            }

                        }
                    } else {
                        if (b < a) {
                            Toast.makeText(this, "结束时间不能早于开始时间", Toast.LENGTH_LONG).show()
                        } else {
                            if (fix == 1) {
                                var eventInfoFix = SugarRecord.findById(EVENTINFO::class.java, id)
                                eventInfoFix.TITLE = event_add_title.text.toString()
                                eventInfoFix.TIMESTART = year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + fixTime(hour, minute)
                                eventInfoFix.TIMEEND = year2.toString() + ":" + month2.toString() + ":" + day2.toString() + ":" + fixTime(hour2, minute2)
                                eventInfoFix.ADDRESS = event_add_add.text.toString()
                                eventInfoFix.PHOTOS = albumFilesString
                                eventInfoFix.save()
                            } else {
                                var eventInfo = EVENTINFO(
                                        event_add_title.text.toString(),
                                        year.toString() + ":" + month.toString() + ":" + day.toString() + ":" + "00:00",
                                        year2.toString() + ":" + month2.toString() + ":" + day2.toString() + ":" + "23:59",
                                        event_add_add.text.toString(), albumFilesString)
                                eventInfo.save()
                            }
                        }
                    }
                    var intent2: Intent = Intent(this@EventActivity, MainActivity::class.java)
                    this@EventActivity.setResult(Activity.RESULT_OK, intent2)
                    this@EventActivity.finish()
                }
                return true


            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun fixTime(hour: Int, minute: Int): String {
        var fix0hour: String = ""
        var fix0minute: String = ""
        if (hour < 10) {
            fix0hour = "0" + hour.toString()
        } else {
            fix0hour = hour.toString()
        }
        if (minute < 10) {
            fix0minute = "0" + minute.toString()
        } else {
            fix0minute = minute.toString()
        }
        return fix0hour + ":" + fix0minute
    }

    fun choosePhotos() {
        Album.album(this) // Image and video mix options.
                .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
                .widget(Widget.newDarkBuilder(this).title("选择照片和视频")
                        .statusBarColor(resources.getColor(R.color.colorPrimaryDark)) // 状态栏颜色
                        .toolBarColor(resources.getColor(R.color.colorPrimary))// Toolbar颜色
                        .build())
                .requestCode(200) // The request code will be returned in the listener.
                .columnCount(3) // The number of columns in the page list.
                .selectCount(99)  // Choose up to a few images.
                .camera(true) // Whether the camera appears in the Item.
                .checkedList(mAlbumFiles) // To reverse the list.
                .listener(object : AlbumListener<ArrayList<AlbumFile>> {
                    override fun onAlbumResult(requestCode: Int, result: ArrayList<AlbumFile>) {
                        mAlbumFiles = result
                        loadCheckedPhotosWithRecyclerView()
                    }

                    override fun onAlbumCancel(requestCode: Int) {
                        // The user canceled the operation.
                    }
                })
                .start()
    }

    fun loadCheckedPhotosWithRecyclerView() {
        photosListEx.clear()
        for (x in mAlbumFiles) {
            photosListEx.add(x.path)
        }
        var gson: Gson = Gson()
        albumFilesString = gson.toJson(mAlbumFiles)
        photosList = photosListEx.toList()

        if (photosList.size == 0) {
            tv_add_photo_and_video.setText("添加照片或视频")
        } else {
            tv_add_photo_and_video.setText(" ")
        }
        mRecyclerViewAdapter = MainAdapter(photosList)

        //recyclerview的布局管理器必须要添加，否则不能显示
        var manager: LinearLayoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        event_rv_loader.layoutManager = manager
        mRecyclerViewAdapter.setOnItemClickListener { pos ->
            choosePhotos()
        }
        event_rv_loader.adapter = mRecyclerViewAdapter
    }

}