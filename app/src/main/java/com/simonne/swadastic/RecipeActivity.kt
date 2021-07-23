package com.simonne.swadastic

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.util.regex.Matcher
import java.util.regex.Pattern


class RecipeActivity : YouTubeBaseActivity(), RecipesClicked {

    private lateinit var mRecipesAdapter: RecipesAdapter
    val API_KEY = "AIzaSyAmLhOufpDy1TeiIEG68ZW6-ZIFm1Y2f3A"
    lateinit var recipe_name: String; lateinit var youtube_link: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        var id = intent.getStringExtra("id")

        if (id != null) {
            recipesData(id)
        }

    }

    private fun getVideoId(videoUrl: String): String {
        var videoId = ""
        val regex =
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
        val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(videoUrl)
        if (matcher.find()) {
            videoId = matcher.group(1)
        }
        return videoId
    }

    private fun playVideo(videoURL: String){
        val youtubePlayer = findViewById<YouTubePlayerView>(R.id.youtubeVideo)

        val videoID = getVideoId(videoURL)

        youtubePlayer.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo(videoID)
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ){
                //Toast.makeText(this@RecipeActivity , "Video player Failed" , Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recipesData(id: String) {
        val recipes = findViewById<RecyclerView>(R.id.recipeIngredients)
        recipes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        fetchRecipes(id)
        mRecipesAdapter = RecipesAdapter(this)
        recipes.adapter = mRecipesAdapter
    }

    private fun fetchRecipes(id: String){
        val recipeBody = findViewById<NestedScrollView>(R.id.recipeBody)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val recipeName = findViewById<TextView>(R.id.recipeCategoryName)
        val category= findViewById<TextView>(R.id.recipeCuisine)
        val area= findViewById<TextView>(R.id.recipeArea)
        val imageURL= findViewById<ImageView>(R.id.recipeImage)
        val instructions= findViewById<TextView>(R.id.recipeInstructions)

        val url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id"
        val recipesList = ArrayList<Recipes>()

        progressBar.visibility = View.VISIBLE
        recipeBody.visibility = View.GONE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val jsonArray = it.getJSONArray("meals")
                val recipesObject = jsonArray.getJSONObject(0)
                for(i in 1 until 20){
                    val ingredientsURL = "https://www.themealdb.com/images/ingredients/" + recipesObject.getString("strIngredient$i") +".png"
                    val recipe = Recipes(ingredientsURL,
                        recipesObject.getString("strIngredient$i"),
                        recipesObject.getString("strMeasure$i"))

                    recipesList.add(recipe)
                }

                recipeName.text = recipesObject.getString("strMeal")
                category.text = recipesObject.getString("strCategory")
                area.text = recipesObject.getString("strArea")
                instructions.text = recipesObject.getString("strInstructions")
                playVideo(recipesObject.getString("strYoutube"))

                recipeBody.visibility = View.VISIBLE

                Glide.with(this).load(recipesObject.getString("strMealThumb")).listener(object: RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        recipeBody.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        recipeBody.visibility = View.VISIBLE
                        return false
                    }

                }).into(imageURL)

                recipe_name =  recipesObject.getString("strMeal")
                youtube_link = recipesObject.getString("strYoutube")

                mRecipesAdapter.updateItems(recipesList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun backButtonFunction(view: View) {
        finish()
    }

    fun shareButtonFunction(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val shareBody = "Check out this recipe for $recipe_name. Here is the YouTube Link: $youtube_link"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(intent, "Share using"))
    }

    override fun onRecipeClicked(item: Recipes) {

    }
}