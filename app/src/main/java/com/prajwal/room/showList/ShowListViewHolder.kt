package com.prajwal.room.showList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.adapter_show_list.view.*

class ShowListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val title: TextView? = itemView.txvName
}