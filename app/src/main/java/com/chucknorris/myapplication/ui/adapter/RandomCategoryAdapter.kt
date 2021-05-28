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
import com.chucknorris.myapplication.util.Constant
import kotlinx.android.synthetic.main.item_random_category.view.*

class RandomCategoryAdapter(
    private var listRandomCategory: MutableList<RandomCategory>,
    private val context: Context?
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
                    it1
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
            dataCategories: RandomCategory
        ) {
            Log.d("ADAPTER RANDOM CATEGORY ", "bindItem: $dataCategories")
            itemView.tvJokes.text = dataCategories.value
            itemView.tvCreatedAts.text = dataCategories.createdAt
//            itemView.tvJokes.text = dataCategories
//            itemView.tvCreatedAt.text = dataCategories.createdAt

//            val requestOption = RequestOptions()
//            requestOption.placeholder(R.drawable.ic_launcher_background)
//            requestOption.error(R.drawable.ic_launcher_background)
//            Glide.with(itemView.ivPicture).setDefaultRequestOptions(requestOption)
//                .load(dataCategories.iconUrl).into(itemView.ivPicture)

//            itemView.setOnClickListener {
//                val category: String = itemView.tvCategory.text as String
//                val intent = Intent("send-category")
//                intent.putExtra("category", category)
//                LocalBroadcastManager.getInstance(it.context).sendBroadcast(intent)
//                it.context.startActivity(Intent(it.context, MainActivity::class.java))
//            }
        }
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}