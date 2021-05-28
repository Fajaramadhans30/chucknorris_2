package com.chucknorris.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chucknorris.myapplication.R
import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.ui.SearchFreeActivity
import com.chucknorris.myapplication.util.Constant
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_random_category.view.*

class RandomCategoryAdapter(
    private var listRandomCategory: MutableList<RandomCategory>,
    private val context: Context?,
    private val listener: SearchFreeActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val v =
                LayoutInflater.from(context).inflate(R.layout.item_random_category, parent, false)
            Item(v)
        } else {
            val v =
                LayoutInflater.from(context).inflate(R.layout.progress_loading, parent, false)
            LoadingHolder(v)
        }
    }

    fun addData(listDataCategories: List<RandomCategory>?) {
        listDataCategories.let {
            it?.let { it1 -> this.listRandomCategory.addAll(it1) }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        context?.let {
            listRandomCategory[position].let { it1 ->
                (holder as Item).bindItem(
                    it1,
                    listener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listRandomCategory.size
    }

    class Item(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @SuppressLint("CheckResult", "LongLogTag")
        fun bindItem(
            dataCategories: RandomCategory,
            listener: (String) -> Unit
        ) {
            Log.d("ADAPTER RANDOM CATEGORY ", "bindItem: $dataCategories")
            itemView.tvJokes.text = dataCategories.value
            itemView.tvCreatedAts.text = dataCategories.createdAt
            val category: String = dataCategories.categories.toString()
            itemView.setOnClickListener {
                listener(category)
            }
        }
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}