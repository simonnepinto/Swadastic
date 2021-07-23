package com.simonne.swadastic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class SearchActivity : AppCompatActivity() {
    lateinit var recipeNames: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.search_bar)
        val recipeList = findViewById<ListView>(R.id.recipeList)
        val failedMessage = findViewById<TextView>(R.id.failedMessage)

        recipeNames = getRecipes()

        val recipeAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, recipeNames
        )

        recipeList.adapter = recipeAdapter

        recipeList.onItemClickListener = AdapterView.OnItemClickListener{
            parent, view, position, id ->
            searchView.setQuery(parent.getItemAtPosition(position).toString(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if(recipeNames.contains(query)){
                    failedMessage.visibility = View.GONE
                    recipeAdapter.filter.filter(query)
                    recipeList.visibility = View.GONE
                    val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
                    callRecipeActivity(url)
                }
                else{
                    failedMessage.visibility = View.VISIBLE
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                failedMessage.visibility = View.GONE
                if(newText.isNullOrEmpty())
                    recipeList.visibility = View.GONE
                else
                    recipeList.visibility = View.VISIBLE


                recipeAdapter.filter.filter(newText)
                if(recipeAdapter.count < 6) {
                    val params = recipeList.layoutParams
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    recipeList.layoutParams = params
                }
                else{
                    val params = recipeList.layoutParams
                    params.height = 1000
                    recipeList.layoutParams = params
                }


                return false
            }
        })
        
        bottomNavigation()
    }

    private fun getRecipes(): ArrayList<String> {
        val recipeNameList = ArrayList<String>()

        val myArray = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",  "r", "s", "t", "v", "w", "y")

        for (character in myArray) {
            val recipeURL = "https://www.themealdb.com/api/json/v1/1/search.php?f=$character"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, recipeURL, null,
                Response.Listener {
                    val jsonArray = it.getJSONArray("meals")
                    for(i in 0 until jsonArray.length()){
                        val recipeObject = jsonArray.getJSONObject(i)
                        recipeNameList.add(recipeObject .getString("strMeal"))
                    }
                },
                Response.ErrorListener {
                }
            )

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
        return recipeNameList
    }

    fun getRandomRecipe(view: View) {
        val url = "https://www.themealdb.com/api/json/v1/1/random.php"
        callRecipeActivity(url)
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
