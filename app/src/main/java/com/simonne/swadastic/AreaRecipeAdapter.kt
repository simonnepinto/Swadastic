package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AreaRecipeAdapter(private val listener: AreaRecipeClicked): RecyclerView.Adapter<AreaRecipeViewHolder>() {

    private val items: ArrayList<AreaRecipe> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_area_recipes, parent, false)
        val viewHolder = AreaRecipeViewHolder(view)
        view.setOnClickListener {
            listener.onAreaRecipeClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: AreaRecipeViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.areaRecipeImage)
        holder.areaRecipeTitle.text = currentItem.recipeName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<AreaRecipe>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class AreaRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val areaRecipeImage: ImageView = itemView.findViewById(R.id.areaRecipeImage)
    val areaRecipeTitle: TextView = itemView.findViewById(R.id.areaRecipeTitle)
}

interface AreaRecipeClicked {
    fun onAreaRecipeClicked(item: AreaRecipe)
}