package com.chucknorris.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chucknorris.myapplication.R
import com.chucknorris.myapplication.ViewModel.SearchFreeViewModel
import com.chucknorris.myapplication.ViewModel.ViewModelCategoriesFactorySearchFree
import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.model.SearchFreeResponse
import com.chucknorris.myapplication.network.JokesProvider
import com.chucknorris.myapplication.ui.adapter.RandomCategoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.failed_load_layout.*
import kotlinx.android.synthetic.main.progress_loading.*
import org.jetbrains.anko.support.v4.onRefresh

class SearchFreeActivity : AppCompatActivity(), (String) -> Unit {
    private val TAG = "CategoryActivity"
    private lateinit var categoryAdapter: RandomCategoryAdapter
    private lateinit var categoryViewModel: SearchFreeViewModel
    private var dataListCategories: MutableList<RandomCategory> = mutableListOf()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val compositeDisposable = CompositeDisposable()
    private val repository = JokesProvider.jokesProviderRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        idError.visibility = View.GONE
        idLoading.visibility = View.VISIBLE
        tvTitle.visibility = View.GONE
        linearLayout2.visibility = View.GONE
        ivSearchh.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Search by " +intent.getStringExtra("query")
        }

        categoryAdapter = RandomCategoryAdapter(dataListCategories, applicationContext, this)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        rvCategory.layoutManager = linearLayoutManager
        rvCategory.hasFixedSize()
        rvCategory.adapter = categoryAdapter

        categoryViewModel = SearchFreeViewModel(
            compositeDisposable,
            repository,
            AndroidSchedulers.mainThread(),
            Schedulers.io()
        )
        categoryViewModel = ViewModelProviders.of(
            this,
            ViewModelCategoriesFactorySearchFree(
                compositeDisposable,
                repository,
                AndroidSchedulers.mainThread(),
                Schedulers.io()
            )
        ).get(categoryViewModel::class.java)
        intent.getStringExtra("query")?.let {
            categoryViewModel.setListSearchFree(it)
        }
        categoryViewModel.getListSearchFree().observe(this, getCategories)
        swpCategories.onRefresh {
            swpCategories.isRefreshing = true
            intent.getStringExtra(getString(R.string.randomCategory))?.let {
                categoryViewModel.setListSearchFree(it)
            }
            categoryViewModel.getListSearchFree().observe(this, getCategories)
        }
    }

    private val getCategories = Observer<SearchFreeResponse> { categoriesItems ->
        if (categoriesItems != null) {
            dataListCategories.clear()
            rvCategory.visibility = View.VISIBLE
            idLoading.visibility = View.GONE
            idError.visibility = View.GONE
            Log.d(TAG, "MASUKKK AV: $categoriesItems")
            setUI(categoriesItems)
        } else {
            rvCategory.visibility = View.GONE
            idLoading.visibility = View.GONE
            idError.visibility = View.VISIBLE
        }
        swpCategories.isRefreshing = false
    }

    private fun setUI(categoriesItems: SearchFreeResponse) {
        val datalistCategoriesNew: MutableList<RandomCategory> = mutableListOf()
        categoriesItems.let {
            categoriesItems.result?.let { it1 -> datalistCategoriesNew.addAll(it1) }
        }
        categoryAdapter.addData(categoriesItems.result)

    }

    override fun invoke(p1: String) {
        Log.d(TAG, "invokesssss: $p1")
        val intentDetail = Intent(applicationContext, CategoryDetail::class.java)
        intentDetail.putExtra(getString(R.string.randomCategory), p1)
        startActivity(intentDetail)
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