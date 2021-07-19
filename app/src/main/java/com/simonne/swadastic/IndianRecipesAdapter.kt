package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class IndianRecipesAdapter(private val listener: IndianRecipesClicked): RecyclerView.Adapter<IndianRecipesViewHolder>() {

    private val items: ArrayList<IndianRecipes> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndianRecipesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_indian_recipes, parent, false)
        val viewHolder = IndianRecipesViewHolder(view)
        view.setOnClickListener {
            listener.onIndianRecipeClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: IndianRecipesViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.indianRecipesImage)
        holder.indianRecipesTitle.text = currentItem.recipeName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<IndianRecipes>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class IndianRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val indianRecipesImage: ImageView = itemView.findViewById(R.id.indianDishImage)
    val indianRecipesTitle: TextView = itemView.findViewById(R.id.indianDishName)
}

interface IndianRecipesClicked{
    fun onIndianRecipeClicked(item: IndianRecipes)
}