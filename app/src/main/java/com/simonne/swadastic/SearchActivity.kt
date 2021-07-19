package com.simonne.swadastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(this)


        
        bottomNavigation()
    }

    fun getRandomRecipe(view: View) {
        val url = "https://www.themealdb.com/api/json/v1/1/random.php"
        callRecipeActivity(url)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        val query = "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
        callRecipeActivity(query)
        return false
    }

    private fun callRecipeActivity(url: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                val randomRecipesObject = jsonArray.getJSONObject(0)
                val mealID = randomRecipesObject.getString("idMeal")
                val intent = Intent(this, RecipeActivity::class.java)
                intent.putExtra("id", mealID)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
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

    fun getRecipeByIngredients(view: View) {
        val intent = Intent(this, SearchIngredientsActivity::class.java)
        startActivity(intent)
    }

    fun getRecipeByArea(view: View) {
        val intent = Intent(this, SearchAreaActivity::class.java)
        startActivity(intent)
    }
}
