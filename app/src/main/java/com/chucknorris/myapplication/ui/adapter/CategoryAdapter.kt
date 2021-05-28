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
//        val item = listCategory
//        context?.let {
//            holder.itemView.tvCategory.text = listCategory?.get(position)
//            holder.itemView.setOnClickListener {
//                item?.get(position)?.let { it1 -> mListener(it1) }
//            }
//
//        }
    }

    override fun getItemCount(): Int {
        return listCategory?.size!!
    }

    class Item(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bindItem(
            dataCategories: String,
            listener: (String) -> Unit
        ) {
//            for (i in 0 until dataCategories!!) {
            //Append all the values to a string
            itemView.tvCategory.text = dataCategories
//            }
            val category: String = itemView.tvCategory.text as String
//            itemView.setOnClickListener {
//                val intent = Intent("send-category")
//                intent.putExtra("category", category)
////                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
////                LocalBroadcastManager.getInstance(it.context).sendBroadcast(intent)
//                it.context.startActivity(Intent(it.context, RandomCategoryActivity::class.java))
//            }

            itemView.setOnClickListener {
                listener(category)
            }
        }
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}