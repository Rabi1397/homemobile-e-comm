package com.example.thehighlife1.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.thehighlife1.R
import com.example.thehighlife1.presentation.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, HomeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Any = when (item.itemId) {
            R.id.homeMenu -> HomeFragment()
            R.id.shopMenu -> ShopFragment()
            R.id.bagMenu -> BagFragment()
            R.id.favMenu -> FavFragment()
            R.id.profileMenu -> ProfileFragment()
            else -> HomeFragment() // Default to HomeFragment() if no match is found
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_fragment, fragment as Fragment)
            .commit()

        return true
    }

}
