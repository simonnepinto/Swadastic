package com.simonne.swadastic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation


class SearchIngredientsActivity : AppCompatActivity(), SearchIngredientsClicked {

    private val url: String = "https://www.themealdb.com/api/json/v1/1/filter.php?i="
    private lateinit var mSearchIngredientsAdapter: SearchIngredientsAdapter
    private lateinit var ingredientNames: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ingredients)

        val searchView = findViewById<SearchView>(R.id.search_bar_ingredients)
        val ingredientsList =  findViewById<ListView>(R.id.ingredientsList)
        val ingredientsMainTitle = findViewById<TextView>(R.id.ingredientsMainTitle)

        ingredientNames = getIngredients()

        val ingredientsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, ingredientNames
        )

        ingredientsList.adapter = ingredientsAdapter

        ingredientsList.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            searchView.setQuery(parent.getItemAtPosition(position).toString(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if(ingredientNames.contains(query)){
                    ingredientsAdapter.filter.filter(query)
                    ingredientsList.visibility = View.GONE

                    ingredientsMainTitle.visibility = View.GONE

                    if (query != null) {
                        searchIngredientsData(query)
                    }
                }
                else{
                    ingredientsMainTitle.visibility = View.VISIBLE
                    ingredientsMainTitle.text = "Sorry!! No results match your search"
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ingredientsMainTitle.visibility = View.GONE

                if(newText.isNullOrEmpty())
                    ingredientsList.visibility = View.GONE
                else
                    ingredientsList.visibility = View.VISIBLE

                ingredientsAdapter.filter.filter(newText)
                return false
            }
        })

        bottomNavigation()
    }

    private fun getIngredients(): ArrayList<String> {
        val ingredientsURL = "https://www.themealdb.com/api/json/v1/1/list.php?i=list"
        val ingredientsNameList = ArrayList<String>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, ingredientsURL, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val ingredientsObject = jsonArray.getJSONObject(i)
                    ingredientsNameList.add(ingredientsObject.getString("strIngredient"))
                }
            },
            Response.ErrorListener {
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        return ingredientsNameList
    }

    private fun searchIngredientsData(query: String) {
        val searchIngredients = findViewById<RecyclerView>(R.id.recipeByIngredients)
        searchIngredients.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        searchIngredientsRecipes(query)
        mSearchIngredientsAdapter = SearchIngredientsAdapter(this)
        searchIngredients.adapter = mSearchIngredientsAdapter
    }

    private fun searchIngredientsRecipes(query: String) {
        var recipeURL = url + query
        val searchIngredientsList = ArrayList<SearchIngredients>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, recipeURL, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val searchIngredientsObject = jsonArray.getJSONObject(i)
                    val recipe = SearchIngredients(searchIngredientsObject.getString("idMeal"),
                        searchIngredientsObject.getString("strMealThumb"),
                        searchIngredientsObject.getString("strMeal"))

                    searchIngredientsList.add(recipe)
                }

                mSearchIngredientsAdapter.updateItems(searchIngredientsList)
            },
            Response.ErrorListener {
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun bottomNavigation() {
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_search))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.india))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {

                }
                2 -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(this, IndianRecipesActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        bottomNavigation.show(1)
    }

    override fun onSearchIngredientsClicked(item: SearchIngredients) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }

    fun backButtonFunction(view: View) {
        finish()
    }
}