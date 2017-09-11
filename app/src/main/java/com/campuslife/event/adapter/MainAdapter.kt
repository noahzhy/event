package com.campuslife.event.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.campuslife.event.R
import kotlinx.android.synthetic.main.imageview_recyclerview_loader.view.*

/**
 * Created by e-it on 2017-9-5.
 */
class MainAdapter(val items: List<String>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var mListener: ((pos: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: ((pos: Int) -> Unit)) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        // 实例化展示的view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.imageview_recyclerview_loader, parent, false)
        // 实例化viewholder
        return ViewHolder(v)
    }

    /**
     * 事件回调监听
     */
    private var onItemClickListener: MainAdapter.OnItemClickListener? = null

//    fun updateData(data_date: ArrayList<String>, data_week: ArrayList<String>, data_title: ArrayList<String>,
//                   data_time_start: ArrayList<String>, data_time_end: ArrayList<String>, data_add: ArrayList<String>) {
//
//
//        this.mData_date = data_date
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        // 绑定数据
        Glide.with(holder.view.context).load("file://" + items!![position]).into(holder.iv_loader)
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                val pos = holder.layoutPosition
                onItemClickListener!!.onItemClick(holder.itemView, pos)
            }
        }

        with(holder?.itemView!!) {
            setOnClickListener { mListener?.invoke(position) }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        internal var iv_loader: ImageView = itemView.iv_rv_loader as ImageView

    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}