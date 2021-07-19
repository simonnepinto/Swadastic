package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchIngredientsAdapter(private val listener: SearchIngredientsClicked): RecyclerView.Adapter<SearchIngredientsViewHolder>() {

    private val items: ArrayList<SearchIngredients> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchIngredientsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients_recipes, parent, false)
        val viewHolder = SearchIngredientsViewHolder(view)
        view.setOnClickListener {
            listener.onSearchIngredientsClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchIngredientsViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.searchIngredientsImage)
        holder.searchIngredientsTitle.text = currentItem.recipeName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<SearchIngredients>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class SearchIngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val searchIngredientsImage: ImageView = itemView.findViewById(R.id.ingredientsImage)
    val searchIngredientsTitle: TextView = itemView.findViewById(R.id.ingredientsHeading)
}

interface SearchIngredientsClicked{
    fun onSearchIngredientsClicked(item: SearchIngredients)
}