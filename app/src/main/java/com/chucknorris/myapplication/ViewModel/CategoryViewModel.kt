package com.chucknorris.myapplication.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chucknorris.myapplication.network.JokesRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class CategoryViewModel (
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {
//    private var listCategories = MutableLiveData<MutableList<Categories>?>()
    private var listCategories = MutableLiveData<MutableList<String>>()


    @SuppressLint("NullSafeMutableLiveData")
    fun setListCategories() {
        compositeDisposable.add(
            repository.getCategories()
                .observeOn(backgroundScheduler)
                .subscribeOn(mainScheduler)
                .subscribe({ CategoryViewModel ->
                    listCategories.postValue(CategoryViewModel as MutableList<String>?)
                }, { error ->
                    println("error message " + error.message)
                    listCategories.postValue(null)
                })
        )
    }

    fun getListCategories(): LiveData<MutableList<String>> {
        return listCategories
    }
}

class ViewModelCategoriesFactory(
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModelProvider.NewInstanceFactory() {
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(
            compositeDisposable,
            repository,
            backgroundScheduler,
            mainScheduler
        ) as T
    }

}