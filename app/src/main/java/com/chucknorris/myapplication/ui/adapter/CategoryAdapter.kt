package com.chucknorris.myapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chucknorris.myapplication.R
import com.chucknorris.myapplication.ui.CategoryActivity
import com.chucknorris.myapplication.util.Constant
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter(
    private var listCategory: MutableList<String>?,
    private val context: Context?,
    private val listener: CategoryActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val v =
                LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
            Item(v)
        } else {
            val v =
                LayoutInflater.from(context).inflate(R.layout.progress_loading, parent, false)
            LoadingHolder(v)
        }
    }

    fun addData(listDataCategories: MutableList<String>) {
        listDataCategories.let {
            this.listCategory?.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        context?.let {
            listCategory?.get(position)?.let { it1 ->
                (holder as Item).bindItem(
                    it1,
                    listener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listCategory?.size!!
    }

    class Item(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItem(
            dataCategories: String,
            listener: (String) -> Unit
        ) {
            itemView.tvCategory.text = dataCategories
            val category: String = itemView.tvCategory.text as String
            itemView.setOnClickListener {
                listener(category)
            }
        }
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}