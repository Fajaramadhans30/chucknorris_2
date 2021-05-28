package com.chucknorris.myapplication.network

import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.model.SearchFreeResponse
import io.reactivex.Observable

class JokesRepository (private val service: Service) {

    fun getCategories(): Observable<ArrayList<String>> {
        return service.getCategories()
    }

    fun getRandomCategory(id:String): Observable<RandomCategory> {
        return service.getRandomCategory(id)
    }

    fun getRandom(): Observable<RandomCategory> {
        return service.getRandom()
    }

    fun getSearch(query:String): Observable<SearchFreeResponse> {
        return service.getSearch(query)
    }

}