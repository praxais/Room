package com.prajwal.room.showList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.prajwal.room.R
import com.prajwal.room.database.User

class ShowListAdapter() : RecyclerView.Adapter<ShowListViewHolder>() {
    private lateinit var userList: List<User>

    constructor(userList: List<User>) : this() {
        this.userList = userList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_show_list, parent, false)
        return ShowListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowListViewHolder, position: Int) {
        val user = userList[position]

        holder.title?.text = user.firstName + user.lastName
    }

    override fun getItemCount() = userList.count()
}