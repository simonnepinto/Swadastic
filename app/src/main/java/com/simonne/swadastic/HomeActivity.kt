package com.simonne.swadastic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_search))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.india))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                }
                2 -> {
                }
                3 -> {
                }
            }
        }
        bottomNavigation.show(2)
    }

}