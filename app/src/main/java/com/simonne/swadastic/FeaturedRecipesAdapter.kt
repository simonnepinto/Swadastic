package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class FeaturedRecipesAdapter(private val listener: FeaturedRecipesClicked): RecyclerView.Adapter<FeaturedRecipesViewHolder>() {

    private val items: ArrayList<FeaturedRecipes> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedRecipesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_featured_recipes, parent, false)
        val viewHolder = FeaturedRecipesViewHolder(view)
        view.setOnClickListener {
            listener.onFeaturedRecipeClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FeaturedRecipesViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.featuredRecipesImage)
        holder.featuredRecipesTitle.text = currentItem.recipeName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<FeaturedRecipes>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class FeaturedRecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val featuredRecipesImage: ImageView = itemView.findViewById(R.id.featuredRecipesImage)
    val featuredRecipesTitle: TextView = itemView.findViewById(R.id.featuredRecipesTitle)
}

interface FeaturedRecipesClicked{
    fun onFeaturedRecipeClicked(item: FeaturedRecipes)
}