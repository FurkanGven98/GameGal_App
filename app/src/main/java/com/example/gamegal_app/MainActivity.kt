package com.example.gamegal_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.gamegal_app.Fragments.*
import com.example.gamegal_app.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val splashScreen = 10000
    lateinit var binding: ActivityMainBinding


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
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animasyon1 = AnimationUtils.loadAnimation(this,R.anim.animasyon1)
        val animasyon2 = AnimationUtils.loadAnimation(this,R.anim.animasyon2)

        val imageView=binding.imageView3
        val imageView2=binding.imageView4
        imageView3.animation = animasyon1
        imageView4.animation = animasyon2

        //SplashScreen
        Handler().postDelayed({
        finish()
        },splashScreen.toLong())

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