package com.prajwal.room.showList

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.prajwal.room.R
import com.prajwal.room.database.AppDatabase
import com.prajwal.room.database.User
import io.reactivex.Single
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
        disposable = getData().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ e ->
                    showListAdapter.notifyDataSetChanged()
                }, { e ->
                    //TODO show error
                })
    }

    private fun getData() =
            Single.create<String> { e ->
                val appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "User").build()
                val userDao = appDatabase.userDao()
                //insert user to Room
                try {
                    userList.addAll(userDao.getAllUser())
                    e.onSuccess("Successful")
                } catch (exception: IllegalStateException) {
                    e.onError(Throwable("Failure"))
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}