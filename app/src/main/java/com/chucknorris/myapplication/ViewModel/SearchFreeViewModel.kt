package com.chucknorris.myapplication.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chucknorris.myapplication.model.SearchFreeResponse
import com.chucknorris.myapplication.network.JokesRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class SearchFreeViewModel (
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {
    private var listCategories = MutableLiveData<SearchFreeResponse>()


    @SuppressLint("NullSafeMutableLiveData")
    fun setListSearchFree(query: String) {
        compositeDisposable.add(
            repository.getSearch(query)
                .observeOn(backgroundScheduler)
                .subscribeOn(mainScheduler)
                .subscribe({ SearchFreeViewModel ->
                    listCategories.postValue(SearchFreeViewModel as SearchFreeResponse)
                }, { error ->
                    println("error message " + error.message)
                    listCategories.postValue(null)
                })
        )
    }

    fun getListSearchFree(): LiveData<SearchFreeResponse> {
        return listCategories
    }
}

class ViewModelCategoriesFactorySearchFree(
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModelProvider.NewInstanceFactory() {
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchFreeViewModel(
            compositeDisposable,
            repository,
            backgroundScheduler,
            mainScheduler
        ) as T
    }
}