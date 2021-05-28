package com.chucknorris.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chucknorris.myapplication.R
import com.chucknorris.myapplication.ViewModel.RandomCategoryViewModel
import com.chucknorris.myapplication.ViewModel.ViewModelCategoriesFactorys
import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.network.JokesProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_detail_category.*
import kotlinx.android.synthetic.main.failed_load_layout.*
import kotlinx.android.synthetic.main.item_random_category.ivPicture
import kotlinx.android.synthetic.main.no_data_layout.*
import kotlinx.android.synthetic.main.progress_loading.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CategoryDetail : AppCompatActivity() {
    private val TAG = "RandomCategoryActivity"
    private lateinit var randomCategoryViewModel: RandomCategoryViewModel
    private val compositeDisposable = CompositeDisposable()
    private val repository = JokesProvider.jokesProviderRepository()

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_category)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Detail Category"
        }
        if (isNetworkConnected()) {
            idError.visibility = View.GONE
        } else {
            idError.visibility = View.VISIBLE
            parentConstraint.visibility = View.GONE
        }
        idLoading.visibility = View.VISIBLE
        parentConstraint.visibility = View.GONE

        randomCategoryViewModel = RandomCategoryViewModel(
            compositeDisposable,
            repository,
            AndroidSchedulers.mainThread(),
            Schedulers.io()
        )
        randomCategoryViewModel = ViewModelProviders.of(
            this,
            ViewModelCategoriesFactorys(
                compositeDisposable,
                repository,
                AndroidSchedulers.mainThread(),
                Schedulers.io()
            )
        ).get(randomCategoryViewModel::class.java)
        intent.getStringExtra(getString(R.string.randomCategory))?.let {
            randomCategoryViewModel.setListCategories(it)
        }

        randomCategoryViewModel.getListRandomCategories().observe(this, getCategories)
        }

    private val getCategories = Observer<RandomCategory> { categoriesItems ->
        Log.d(TAG, "CATEGORIES ITEM : $categoriesItems")
        if (categoriesItems != null) {
            idLoading.visibility = View.GONE
            parentConstraint.visibility = View.VISIBLE
            setUI(categoriesItems)
        } else {
            idErrorNoData.visibility = View.VISIBLE
            idLoading.visibility = View.GONE
            parentConstraint.visibility = View.GONE
        }
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    private fun setUI(categoriesItems: RandomCategory) {
        tvValue.text = categoriesItems.value
        tvCreatedAts.text = categoriesItems.createdAt.toString()
        Log.d(TAG, "setUI: " + categoriesItems.categories)

//        val cal = Calendar.getInstance(Locale.ENGLISH)
//        cal.timeInMillis = categoriesItems.createdAt!!.toInt() * 1000L
//        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
//        Log.d(TAG, "setUIIIII: $sdf")

        val requestOption = RequestOptions()
        requestOption.placeholder(R.drawable.ic_launcher_background)
        requestOption.error(R.drawable.ic_launcher_background)
//        Glide.with(ivPicture).setDefaultRequestOptions(requestOption).load(categoriesItems.iconUrl)
//            .into(ivPicture)
        Glide.with(this).load(categoriesItems.iconUrl).apply(requestOption).into(ivPicture);

        btnNext.setOnClickListener {
            callNext()
        }
    }

    private fun callNext() {
        randomCategoryViewModel = ViewModelProviders.of(
            this,
            ViewModelCategoriesFactorys(
                compositeDisposable,
                repository,
                AndroidSchedulers.mainThread(),
                Schedulers.io()
            )
        ).get(randomCategoryViewModel::class.java)
        randomCategoryViewModel.setRandom()

        randomCategoryViewModel.getRandom().observe(this, getCategories)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}