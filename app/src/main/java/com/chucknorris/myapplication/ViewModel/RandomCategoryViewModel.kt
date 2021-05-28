package com.chucknorris.myapplication.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.network.JokesRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class RandomCategoryViewModel (
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {
    private var listCategories = MutableLiveData<RandomCategory>()


    @SuppressLint("NullSafeMutableLiveData")
    fun setListCategories(category: String) {
        compositeDisposable.add(
            repository.getRandomCategory(category)
                .observeOn(backgroundScheduler)
                .subscribeOn(mainScheduler)
                .subscribe({ RandomCategoryViewModel ->
                    listCategories.postValue(RandomCategoryViewModel as RandomCategory)
                }, { error ->
                    println("error message " + error.message)
                    listCategories.postValue(null)
                })
        )
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun setRandom() {
        compositeDisposable.add(
            repository.getRandom()
                .observeOn(backgroundScheduler)
                .subscribeOn(mainScheduler)
                .subscribe({ RandomCategoryViewModel ->
                    listCategories.postValue(RandomCategoryViewModel as RandomCategory)
                }, { error ->
                    println("error message " + error.message)
                    listCategories.postValue(null)
                })
        )
    }

    fun getListRandomCategories(): LiveData<RandomCategory> {
        return listCategories
    }

    fun getRandom(): LiveData<RandomCategory> {
        return listCategories
    }
}

class ViewModelCategoriesFactorys(
    private val compositeDisposable: CompositeDisposable,
    private val repository: JokesRepository,
    private val backgroundScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModelProvider.NewInstanceFactory() {
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RandomCategoryViewModel(
            compositeDisposable,
            repository,
            backgroundScheduler,
            mainScheduler
        ) as T
    }
}