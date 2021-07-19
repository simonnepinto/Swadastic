package com.simonne.swadastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class SearchAreaActivity : AppCompatActivity(), SearchAreaClicked {

    private lateinit var mSearchAreaAdapter: SearchAreaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_area)

        searchAreaData()
    }

    private fun searchAreaData() {
        val searchArea = findViewById<RecyclerView>(R.id.recipeByArea)
        searchArea.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchAreaRecipes()
        mSearchAreaAdapter = SearchAreaAdapter(this)
        searchArea.adapter = mSearchAreaAdapter
    }

    private fun searchAreaRecipes() {
        var url = "https://www.themealdb.com/api/json/v1/1/list.php?a=list"
        val searchAreaList = ArrayList<SearchArea>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                for(i in 0 until jsonArray.length()){
                    val searchAreaObject = jsonArray.getJSONObject(i)
                    val area = SearchArea(searchAreaObject.getString("strArea"))

                    searchAreaList.add(area)
                }
                searchAreaList.removeAt(24)
                mSearchAreaAdapter.updateItems(searchAreaList)
            },
            Response.ErrorListener {
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onSearchAreaClicked(item: SearchArea) {
        val intent = Intent(this, AreaRecipeActivity::class.java)
        intent.putExtra("area", item.area)
        startActivity(intent)
    }

    fun backButtonFunction(view: View) {
        finish()
    }
}