package com.gzzdev.saveclass.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav(){
        binding.bottomAppBar.performShow()
        binding.fab.show()
    }

    private fun hideBottomNav(){
        binding.bottomAppBar.performHide()
        binding.fab.hide()
    }
}