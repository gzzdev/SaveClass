package com.gzzdev.saveclass.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gzzdev.saveclass.R
import com.gzzdev.saveclass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        actions()
        stateBottomNav()
    }
    private fun actions() {
        binding.fab.setOnClickListener {
            navController.navigate(R.id.noteFragment)
        }
    }
    private fun setup() {
        binding.bottomNavigationView.background = null
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.container.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun stateBottomNav(){
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.noteFragment -> hideBottomNav()
                R.id.settingsFragment -> hideBottomNav()
                R.id.notesByCategoryFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav(){
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.fab.show()
    }

    private fun hideBottomNav(){
        binding.bottomNavigationView.visibility = View.INVISIBLE
        binding.fab.hide()
    }
}