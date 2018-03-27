package com.prajwal.room.main

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.prajwal.room.R
import com.prajwal.room.database.AppDatabase
import com.prajwal.room.database.User
import com.prajwal.room.showList.ShowListActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListeners()
    }

    override fun onClick(view: View?) {
        when (view) {
            txvShowList -> navigateToShowList()
            btnAdd -> saveToDatabase()
        }
    }

    private fun initListeners() {
        txvShowList.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
    }

    private fun navigateToShowList() {
        startActivity(Intent(this, ShowListActivity::class.java))
    }

    private fun saveToDatabase() {
        when {
            edtFirstName?.text.toString().isEmpty() -> showError(R.string.error_first_name)
            edtLastName?.text.toString().isEmpty() -> showError(R.string.error_last_name)
            else -> {
                val user = User(Random().nextInt(), edtFirstName?.text.toString().trim(), edtLastName?.text.toString().trim())

                compositeDisposable = CompositeDisposable()
                compositeDisposable.add(insertToDatabase(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe({ e ->
                            showSnackbar(e)
                        }, { e ->
                            showSnackbar(e.localizedMessage)
                        }))
            }
        }
    }

    private fun showSnackbar(error: String) {
        Snackbar.make(cnLView, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showError(error: Int) {
        Snackbar.make(cnLView, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun insertToDatabase(user: User) =
            Single.create<String> { e ->
                val appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "User").build()
                val userDao = appDatabase.userDao()
                //insert user to Room
                try {
                    userDao.insertUser(user)
                    e.onSuccess("Successful")
                } catch (exception: IllegalStateException) {
                    e.onError(Throwable("Failure"))
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
