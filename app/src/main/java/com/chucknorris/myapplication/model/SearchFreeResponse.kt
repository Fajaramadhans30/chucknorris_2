package com.chucknorris.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchFreeResponse : Serializable {
    @SerializedName("total")
    @Expose
    public val total: Int? = null

    @SerializedName("result")
    @Expose
    val result: List<RandomCategory>? = null
}