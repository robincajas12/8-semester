package com.mobiledev.firstapp.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mobiledev.firstapp.R
import com.mobiledev.firstapp.databinding.ActivityMain2Binding
import java.time.LocalDate
import java.time.LocalTime

class Login : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.button4.setOnClickListener {
            checkLogin(binding.username.text.toString(), binding.password.text.toString())
        }

    }

    fun checkLogin(username:String, password:String)
    {
        if(password == "admin" && username == "admin")
        {
            binding.txt.setText("Siuuuuu");
            Snackbar.make(binding.root, "Text label", Snackbar.LENGTH_LONG)
                .show()
        }else{
            binding.txt.setText("Noouuuuuu");
        }
    }
}