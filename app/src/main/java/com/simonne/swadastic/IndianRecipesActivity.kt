package com.simonne.swadastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class IndianRecipesActivity : AppCompatActivity(), IndianRecipesClicked {

    private lateinit var mIndianRecipesAdapter: IndianRecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indian_recipes)

        indianRecipesData()
        bottomNavigation()
    }

    private fun indianRecipesData() {
        val indianRecipes = findViewById<RecyclerView>(R.id.indianRecipes)
        indianRecipes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        fetchIndianRecipes()
        mIndianRecipesAdapter = IndianRecipesAdapter(this)
        indianRecipes.adapter = mIndianRecipesAdapter
    }

    private fun fetchIndianRecipes(){
        val url = "https://www.themealdb.com/api/json/v1/1/filter.php?a=indian"
        val indianRecipesList = ArrayList<IndianRecipes>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val indianRecipesObject = jsonArray.getJSONObject(i)
                    val recipe = IndianRecipes(indianRecipesObject.getString("idMeal"),
                        indianRecipesObject.getString("strMealThumb"),
                        indianRecipesObject.getString("strMeal"))

                    indianRecipesList.add(recipe)
                }

                mIndianRecipesAdapter.updateItems(indianRecipesList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
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
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                3 -> {

                }
            }
        }
        bottomNavigation.show(3)
    }

    override fun onIndianRecipeClicked(item: IndianRecipes) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }

}