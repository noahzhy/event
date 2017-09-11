package com.campuslife.event.sqliteDatabase

import com.orm.SugarRecord
import com.orm.dsl.Unique


/**
 * Created by e-it on 2017/8/29.
 */
class EVENTINFO : SugarRecord {
    var TITLE: String = ""
    var TIMESTART: String = ""
    var TIMEEND: String = ""
    var ADDRESS: String = ""
    var PHOTOS: String= ""

   constructor() {}

    constructor(title: String, timeStart: String, timeEnd: String, address: String,photos:String) {
        this.TITLE = title
        this.TIMESTART = timeStart
        this.TIMEEND = timeEnd
        this.ADDRESS = address
        this.PHOTOS = photos
    }

}

//
//
//        DataSupport() {
//    var id: Int = 0
//    //transient紧在gson生成对象时忽略，litePal不会忽略还是会创建表
//    @Transient private val title: String? = null
//    @Transient private val timeStart: String? = null
//    @Transient private val timeEnd: String? = null
//    @Transient private val address: String? = null
////
////    constructor(){}
////
////    constructor(id: Int, date: String, title: String, timeStart: String, timeEnd: String, add: String) {
////        this.id = id
////        this.date = date
////        this.title = title
////        this.timeStart = timeStart
////        this.timeEnd = timeEnd
////        this.add = add
////    }
////
////
////    override fun describeContents(): Int {
////        return 0
////    }
////
////    override fun writeToParcel(p0: Parcel, p1: Int) {
////        p0.writeInt(this.id)
////        p0.writeString(this.date)
////        p0.writeString(this.title)
////        p0.writeString(this.timeStart)
////        p0.writeString(this.timeEnd)
////        p0.writeString(this.add)
////    }
////
////    override fun toString(): String {
////        return "EVENTINFO(id=$id,date='$date',title='$title',timeStart='$timeStart',timeEnd='$timeEnd',add='$add')"
////    }
////
////    protected constructor(`in`:Parcel){
////        this.id = `in`.readInt()
////        this.date = `in`.readString()
////        this.title = `in`.readString()
////        this.timeStart = `in`.readString()
////        this.timeEnd = `in`.readString()
////        this.add = `in`.readString()
////    }
////
////    companion object {
////        @JvmField val CREATOR : Parcelable.Creator<EVENTINFO> = object :Parcelable.Creator<EVENTINFO>{
////            override fun createFromParcel(p0: Parcel): EVENTINFO {
////                return EVENTINFO(p0)
////            }
////
////            override fun newArray(p0: Int): Array<EVENTINFO?> {
////                return arrayOfNulls(p0)
////            }
////        }
////    }
//}