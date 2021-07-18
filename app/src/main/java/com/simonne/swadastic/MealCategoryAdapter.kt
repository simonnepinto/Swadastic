package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealCategoryAdapter(private val listener: MealCategoryClicked): RecyclerView.Adapter<MealCategoryViewHolder>() {

    private val items: ArrayList<MealCategories> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_categories, parent, false)
        val viewHolder = MealCategoryViewHolder(view)
        view.setOnClickListener {
            listener.onMealCategoryClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MealCategoryViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.categoryImgDish)
        holder.categoryTitleDish.text = currentItem.category
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<MealCategories>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class MealCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val categoryImgDish: ImageView = itemView.findViewById(R.id.category_img_dish)
    val categoryTitleDish: TextView = itemView.findViewById(R.id.category_title_dish)
}

interface MealCategoryClicked{
    fun onMealCategoryClicked(items: MealCategories)
}