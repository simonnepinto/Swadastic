package com.simonne.swadastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class AreaRecipeActivity : AppCompatActivity(), AreaRecipeClicked  {

    private lateinit var mAreaRecipeAdapter: AreaRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_recipe)

        var area = intent.getStringExtra("area")
        if (area != null) {
            areaRecipeData(area)
        }

        bottomNavigation()
    }

    private fun areaRecipeData(area: String) {
        val areaRecipe = findViewById<RecyclerView>(R.id.getRecipeByArea)
        areaRecipe.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        areaRecipeRecipes(area)
        mAreaRecipeAdapter = AreaRecipeAdapter(this)
        areaRecipe.adapter = mAreaRecipeAdapter
    }

    private fun areaRecipeRecipes(area:String) {
        var url = "https://www.themealdb.com/api/json/v1/1/filter.php?a=$area"
        val areaRecipeList = ArrayList<AreaRecipe>()
        val heading = findViewById<TextView>(R.id.heading)

        heading.text = "A peek into $area Cuisine"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val areaRecipeObject = jsonArray.getJSONObject(i)
                    val area = AreaRecipe(areaRecipeObject.getString("idMeal"),
                        areaRecipeObject.getString("strMealThumb"),
                        areaRecipeObject.getString("strMeal"))

                    areaRecipeList.add(area)
                }
                mAreaRecipeAdapter.updateItems(areaRecipeList)
            },
            Response.ErrorListener {
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun backButtonFunction(view: View) {
        finish()
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

    override fun onAreaRecipeClicked(item: AreaRecipe) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }
}