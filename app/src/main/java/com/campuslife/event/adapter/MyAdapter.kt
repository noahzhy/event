package com.campuslife.event.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.campuslife.event.R
import kotlinx.android.synthetic.main.content_item_recyclerview_content_main.view.*
import java.util.ArrayList
import android.text.method.TextKeyListener.clear



/**
 * Created by e-it on 2017/8/26.
 */

class MyAdapter(private var mData_date: ArrayList<String>?,
                private var mData_week: ArrayList<String>?,
                private var mData_title: ArrayList<String>?,
                private var mData_time: ArrayList<String>?,
                private var mData_add: ArrayList<String>?,
                private var mData_cover_photo: ArrayList<String>?) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var mListener: ((pos: Int) -> Unit)? = null

    /**
     * 事件回调监听
     */
    private var onItemClickListener: MyAdapter.OnItemClickListener? = null

    fun updateData(data_date: ArrayList<String>, data_week: ArrayList<String>, data_title: ArrayList<String>,
                   data_time_start: ArrayList<String>,data_add: ArrayList<String>, data_cover_photo: ArrayList<String>) {
        this.mData_date = data_date
        this.mData_week = data_week
        this.mData_title = data_title
        this.mData_time = data_time_start
        this.mData_add = data_add
        this.mData_cover_photo =  data_cover_photo
        notifyDataSetChanged()
    }

    /**
     * 添加新的Item
     */
    fun addNewItem() {
        if (mData_date == null) {
            mData_date = ArrayList()
        }
        mData_date!!.add(0, "new Item")
        notifyItemInserted(0)
    }

    /**
     * 删除Item
     */
    fun deleteItem() {
        if (mData_date == null || mData_date!!.isEmpty()) {
            return
        }
        mData_date!!.removeAt(0)
        notifyItemRemoved(0)
    }


    /**
     * 设置回调监听
     *
     * @param listener
     */
    //    fun setOnItemClickListener(listener: ((pos: Int) -> Unit){
//        this.onItemClickListener = listener
//        mListener = listener
//    }

    fun setOnItemClickListener(listener: ((pos: Int) -> Unit)) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 实例化展示的view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_item_recyclerview_content_main, parent, false)
        // 实例化viewholder
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 绑定数据
        holder.tv_item_date.text = mData_date!![position]
        holder.tv_item_week.text = mData_week!![position]
        holder.tv_item_title.text = mData_title!![position]
        if (mData_time!![position].equals("00:00-23:59")) {
            holder.tv_item_time.visibility = View.GONE
        } else {
            holder.tv_item_time.text = mData_time!![position]
        }
        if (mData_add!![position].equals("")) {
            holder.tv_item_add.visibility = View.GONE
        } else {
            holder.tv_item_add.text = mData_add!![position]
        }
        if (mData_cover_photo!![position].equals("")){
            Glide.with(holder.itemView.context).load("file:///android_asset/welcome.jpg").into(holder.iv_cover_photo)
        }else{
            Glide.with(holder.itemView.context).load("file://" + mData_cover_photo!![position]).into(holder.iv_cover_photo)
        }

        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                val pos = holder.layoutPosition
                onItemClickListener!!.onItemClick(holder.itemView, pos)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (onItemClickListener != null) {
                val pos = holder.layoutPosition
                onItemClickListener!!.onItemLongClick(holder.itemView, pos)
            }
            true
        }


        with(holder?.itemView!!) {
            setOnClickListener { mListener?.invoke(position) }
        }
    }

    override fun getItemCount(): Int {
        return if (mData_date == null) 0 else mData_date!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tv_item_date: TextView = itemView.tv_item_date as TextView
        internal var tv_item_week: TextView = itemView.tv_item_week as TextView
        internal var tv_item_title: TextView = itemView.tv_item_title as TextView
        internal var tv_item_time: TextView = itemView.tv_item_time as TextView
        internal var tv_item_add: TextView = itemView.tv_item_add as TextView
        internal var iv_cover_photo: ImageView = itemView.roundAngleImageView as ImageView

    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }
}