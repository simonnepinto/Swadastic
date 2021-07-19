package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAreaAdapter(private val listener: SearchAreaClicked): RecyclerView.Adapter<SearchAreaViewHolder>() {

    private val items: ArrayList<SearchArea> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAreaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_area, parent, false)
        val viewHolder = SearchAreaViewHolder(view)
        view.setOnClickListener {
            listener.onSearchAreaClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchAreaViewHolder, position: Int) {
        val currentItem = items[position]
        holder.searchAreaTitle.text = currentItem.area
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<SearchArea>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class SearchAreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val searchAreaTitle: TextView = itemView.findViewById(R.id.searchAreaTitle)
}

interface SearchAreaClicked {
    fun onSearchAreaClicked(item: SearchArea)
}