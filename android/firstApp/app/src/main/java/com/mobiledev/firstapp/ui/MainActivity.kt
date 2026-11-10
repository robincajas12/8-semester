package com.mobiledev.firstapp.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.mobiledev.firstapp.R
import com.mobiledev.firstapp.ui.activity.Fragment1
import com.mobiledev.firstapp.ui.activity.Fragment2
import com.mobiledev.firstapp.databinding.ActivityMainBinding
import com.mobiledev.firstapp.databinding.UserListLayoutBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this) {
            if (binding.bottomNavigation.selectedItemId != R.id.f1) {
                binding.bottomNavigation.selectedItemId = R.id.f1
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.f1 -> {
                    changeFragment(Fragment1())
                    true
                }
                R.id.f2 -> {
                    changeFragment(Fragment2())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            changeFragment(Fragment1())
        }
    }

    fun  changeFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
