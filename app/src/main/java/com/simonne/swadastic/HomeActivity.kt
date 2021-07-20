package com.simonne.swadastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), FeaturedRecipesClicked, MealCategoryClicked, SubMealCategoryClicked {

    private lateinit var mFeaturedRecipesAdapter: FeaturedRecipesAdapter
    private lateinit var mMealCategoryAdapter: MealCategoryAdapter
    private lateinit var mSubMealCategoryAdapter: SubMealCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        featuredRecipesData()

        categoryData()

        subCategoryData("Chicken")

        bottomNavigation()
    }

    private fun featuredRecipesData() {
        val featuredRecipes = findViewById<RecyclerView>(R.id.featuredRecipes)
        featuredRecipes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        fetchFeaturedRecipes()
        mFeaturedRecipesAdapter = FeaturedRecipesAdapter(this)
        featuredRecipes.adapter = mFeaturedRecipesAdapter
    }

    private fun categoryData() {
        val mealCategory = findViewById<RecyclerView>(R.id.meal_category)
        mealCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        fetchMealCategories()
        mMealCategoryAdapter = MealCategoryAdapter(this)
        mealCategory.adapter = mMealCategoryAdapter
    }

    private fun subCategoryData(category: String) {
        val subMealCategory = findViewById<RecyclerView>(R.id.sub_meals)
        val subCategoryMainHeading = findViewById<TextView>(R.id.subCategoryMainHeading)

        subCategoryMainHeading.text = "$category Dishes"
        subMealCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        fetchSubCategory(category)
        mSubMealCategoryAdapter = SubMealCategoryAdapter(this)
        subMealCategory.adapter = mSubMealCategoryAdapter
    }

    private fun fetchFeaturedRecipes(){
        val url = "https://www.themealdb.com/api/json/v1/1/filter.php?i=tomato"
        val featuredRecipesList = ArrayList<FeaturedRecipes>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until 7){
                    val featuredRecipesObject = jsonArray.getJSONObject(i)
                    val recipe = FeaturedRecipes(featuredRecipesObject.getString("idMeal"),
                        featuredRecipesObject.getString("strMealThumb"),
                        featuredRecipesObject.getString("strMeal"))

                    featuredRecipesList.add(recipe)
                }
                featuredRecipesList.reverse()

                mFeaturedRecipesAdapter.updateItems(featuredRecipesList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun fetchMealCategories(){
        val url = "https://www.themealdb.com/api/json/v1/1/categories.php"
        val mealCategoriesList = ArrayList<MealCategories>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("categories")
                for(i in 0 until jsonArray.length()){
                    val mealCategoriesObject = jsonArray.getJSONObject(i)
                    val category = MealCategories(mealCategoriesObject.getString("strCategory"),
                        mealCategoriesObject.getString("strCategoryThumb"))

                    mealCategoriesList.add(category)
                }

                mealCategoriesList.removeAt(0)
                mealCategoriesList.removeAt(mealCategoriesList.size-1)

                mMealCategoryAdapter.updateItems(mealCategoriesList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun fetchSubCategory(category: String){
        val url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=$category"
        val subMealCategoriesList = ArrayList<SubMealCategories>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val subMealCategoriesObject = jsonArray.getJSONObject(i)
                    val subCategory = SubMealCategories(subMealCategoriesObject.getString("idMeal"),
                        subMealCategoriesObject.getString("strMealThumb"),
                        subMealCategoriesObject.getString("strMeal"))

                    subMealCategoriesList.add(subCategory)
                }

                subMealCategoriesList.removeAt(0)
                mSubMealCategoryAdapter.updateItems(subMealCategoriesList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onFeaturedRecipeClicked(items: FeaturedRecipes) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("id", items.id)
        startActivity(intent)
    }

    override fun onMealCategoryClicked(items: MealCategories) {
        subCategoryData(items.category)
    }

    override fun onSubMealCategoryClicked(items: SubMealCategories) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("id", items.id)
        startActivity(intent)
    }

    private fun bottomNavigation() {
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_search))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.india))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }
                2 -> {

                }
                3 -> {
                    val intent = Intent(this, IndianRecipesActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        bottomNavigation.show(2)
    }

}