package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipesAdapter(private val listener: RecipesClicked): RecyclerView.Adapter<RecipesViewHolder>() {

    private val items: ArrayList<Recipes> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipes_ingredients, parent, false)
        val viewHolder = RecipesViewHolder(view)
        view.setOnClickListener {
            listener.onRecipeClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val currentItem = items[position]
        if(currentItem.ingredient != null && currentItem.ingredient != ""){
            Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.ingredientsImage)
            holder.ingredientsTitle.text = currentItem.ingredient
            holder.ingredientsMeasure.text = currentItem.measure
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<Recipes>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ingredientsImage: ImageView = itemView.findViewById(R.id.recipeIngredientsImage)
    val ingredientsTitle: TextView = itemView.findViewById(R.id.recipeIngredients)
    val ingredientsMeasure: TextView = itemView.findViewById(R.id.recipeIngredientsMeasure)
}

interface RecipesClicked{
    fun onRecipeClicked(items: Recipes)
}