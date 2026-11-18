package com.example.recyclerview.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.R
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.ui.fragments.Chat
import com.example.recyclerview.ui.fragments.Feed
import com.example.recyclerview.ui.fragments.Profiles

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMenu()
    }


    private fun initMenu() {

        binding.bottomNavigation.setOnItemSelectedListener { item ->
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
                R.id.chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        Chat(), null).commit()
                    true
                }
                else -> false
            }
        }
    }
}
