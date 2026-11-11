package com.example.recyclerview.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.R
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.ui.fragments.Feed
import com.example.recyclerview.ui.fragments.Profiles

class MainActivity : AppCompatActivity() {
    private lateinit var biding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        biding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(biding.root)
        initMenu()
    }


    private fun initMenu() {

        biding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        Feed(), null).commit()
                    true
                }
                R.id.profiles -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        Profiles(), null).commit()
                    true
                }
                else -> false
            }
        }
    }
}
