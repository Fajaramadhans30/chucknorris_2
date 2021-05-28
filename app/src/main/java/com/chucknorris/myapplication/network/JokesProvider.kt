package com.chucknorris.myapplication.network

object JokesProvider {
    fun jokesProviderRepository(): JokesRepository {
        return  JokesRepository(Service.create())
    }
}