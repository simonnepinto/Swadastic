package com.simonne.swadastic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SubMealCategoryAdapter(private val listener: SubMealCategoryClicked): RecyclerView.Adapter<SubMealCategoryViewHolder>() {

    private val items: ArrayList<SubMealCategories> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubMealCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_sub_categories, parent, false)
        val viewHolder = SubMealCategoryViewHolder(view)
        view.setOnClickListener {
            listener.onSubMealCategoryClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SubMealCategoryViewHolder, position: Int) {
        val currentItem = items[position]
        Glide.with(holder.itemView.context).load(currentItem.imageURL).into(holder.subCategoryImgDish)
        holder.subCategoryTitleDish.text = currentItem.dishName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(updatedList: ArrayList<SubMealCategories>){
        items.clear()
        items.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class SubMealCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val subCategoryImgDish: ImageView = itemView.findViewById(R.id.sub_category_img_dish)
    val subCategoryTitleDish: TextView = itemView.findViewById(R.id.sub_category_title_dish)
}

interface SubMealCategoryClicked{
    fun onSubMealCategoryClicked(items: SubMealCategories)
}