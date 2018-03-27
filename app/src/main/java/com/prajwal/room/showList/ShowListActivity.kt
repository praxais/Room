package com.prajwal.room.showList

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.prajwal.room.R
import com.prajwal.room.database.AppDatabase
import com.prajwal.room.database.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_list.*

class ShowListActivity : AppCompatActivity() {
    private lateinit var disposable: Disposable
    private lateinit var userList: MutableList<User>
    private lateinit var showListAdapter: ShowListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        initRecyclerView()
        getDataFromDb()
    }

    private fun initRecyclerView() {
        rcvList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        userList = ArrayList()
        showListAdapter = ShowListAdapter(userList)

        rcvList?.adapter = showListAdapter
    }

    private fun getDataFromDb() {
        val appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "User").build()
        val userDao = appDatabase.userDao()

        disposable = userDao.getAllUser().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ userList ->
                    this.userList.addAll(userList)
                    showListAdapter.notifyDataSetChanged()
                }, { error ->
                    //TODO show error
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}