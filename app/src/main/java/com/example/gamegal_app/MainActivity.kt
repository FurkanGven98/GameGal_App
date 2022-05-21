package com.example.gamegal_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Log.e
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gamegal_app.Fragments.*
import com.example.gamegal_app.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
       private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_search -> {
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_message -> {
                moveToFragment(ChatFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_notifications -> {
                moveToFragment(NotificationsFragment())

                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_profile -> {
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener  true
            }

        }

        false
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        moveToFragment(HomeFragment())
    }








    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans=supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container,fragment)
        fragmentTrans.commit()
    }
}