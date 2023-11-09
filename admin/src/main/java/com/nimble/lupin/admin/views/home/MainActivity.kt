package com.nimble.lupin.admin.views.home

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.ActivityMainBinding
import com.nimble.lupin.admin.utils.NetworkChangeListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var networkBroadcaster :NetworkChangeListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       val controller = findNavController(R.id.nav_host_fragment_activity_main)
        networkBroadcaster =  NetworkChangeListener(this@MainActivity)

             binding.bottomBarView.setupWithNavController(controller)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawerLayout: DrawerLayout = binding.adminDrawerLayout
        val navigationController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_report, R.id.navigation_schedule), drawerLayout)
        val navigationView : NavigationView = binding.adminNavigationView
        navigationView.setupWithNavController(navigationController)
         actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout,binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {}
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
       return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideBottomView() {
        binding.bottomBarView.animate().translationY(binding.bottomBarView.height.toFloat()).setDuration(800).withEndAction {
            binding.bottomBarView.visibility = View.GONE
        }.start()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false

    }
   fun showBottomView(){
       binding.bottomBarView.visibility = View.VISIBLE
       binding.bottomBarView.animate().translationY(0f).setDuration(800).start()
       actionBarDrawerToggle.isDrawerIndicatorEnabled = true

   }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        applicationContext.registerReceiver(networkBroadcaster,filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkBroadcaster)
    }
}