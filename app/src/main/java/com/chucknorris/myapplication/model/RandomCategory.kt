package com.chucknorris.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RandomCategory : Serializable {
    @SerializedName("categories")
    @Expose
    var categories: List<String>? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("icon_url")
    @Expose
    var iconUrl: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
}