package com.chucknorris.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chucknorris.myapplication.R
import com.chucknorris.myapplication.ViewModel.CategoryViewModel
import com.chucknorris.myapplication.ViewModel.ViewModelCategoriesFactory
import com.chucknorris.myapplication.network.JokesProvider
import com.chucknorris.myapplication.ui.adapter.CategoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.failed_load_layout.*
import kotlinx.android.synthetic.main.progress_loading.*
import org.jetbrains.anko.support.v4.onRefresh


class CategoryActivity :AppCompatActivity(), (String) -> Unit {
    private val TAG = "CategoryActivity"
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel
    private var dataListCategories: MutableList<String> = mutableListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val compositeDisposable = CompositeDisposable()
    private val repository = JokesProvider.jokesProviderRepository()

    private lateinit var querys: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        idError.visibility = View.GONE
        idLoading.visibility = View.VISIBLE

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Category"
        }

        ivSearchh.setOnClickListener {
            Log.d(TAG, "onCreate: " + searchFinder.text)
            searchQuery(searchFinder.text)
        }

        categoryAdapter = CategoryAdapter(dataListCategories, applicationContext, this)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        rvCategory.layoutManager = linearLayoutManager
        rvCategory.hasFixedSize()
        rvCategory.adapter = categoryAdapter

        categoryViewModel = CategoryViewModel(compositeDisposable, repository, AndroidSchedulers.mainThread(), Schedulers.io())
        categoryViewModel = ViewModelProviders.of(
            this,
            ViewModelCategoriesFactory(
                compositeDisposable,
                repository,
                AndroidSchedulers.mainThread(),
                Schedulers.io()
            )
        ).get(categoryViewModel::class.java)
        categoryViewModel.setListCategories()
        categoryViewModel.getListCategories().observe(this, getCategories)
        swpCategories.onRefresh {
            swpCategories.isRefreshing = true
            categoryViewModel.setListCategories()
            categoryViewModel.getListCategories().observe(this, getCategories)
        }
    }

    private fun searchQuery(text: Editable?) {
        querys = text.toString()
        if (querys.isNotEmpty()) {
            text?.clear()
        }
        Log.d(TAG, "searchQuery: $querys")
            val intentDetail = Intent(applicationContext, SearchFreeActivity::class.java)
            intentDetail.putExtra(getString(R.string.querySearch), querys)
            startActivity(intentDetail)
    }

    private val getCategories = Observer<MutableList<String>> { categoriesItems ->
        if (categoriesItems != null) {
            dataListCategories.clear()
            rvCategory.visibility = View.VISIBLE
            idLoading.visibility = View.GONE
            idError.visibility = View.GONE
            if (categoriesItems.size > 0) {
                Log.d(TAG, "MASUKKK: " + categoriesItems.size)
                Log.d(TAG, "MASUKKK AV: $categoriesItems")
                val datalistCategoriesNew: MutableList<String> = mutableListOf()
                categoriesItems.let { datalistCategoriesNew.addAll(it) }
                categoryAdapter.addData(datalistCategoriesNew)
            }
        } else {
            rvCategory.visibility = View.GONE
            idLoading.visibility = View.GONE
            idError.visibility = View.VISIBLE
        }
        swpCategories.isRefreshing = false
    }

    override fun invoke(p1: String) {
        Log.d(TAG, "invoke: $p1")
        val intentDetail = Intent(applicationContext, RandomCategoryActivity::class.java)
        intentDetail.putExtra(getString(R.string.randomCategory), p1)
        startActivity(intentDetail)
    }

}