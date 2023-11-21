package com.nimble.lupin.user.views.home

import android.content.IntentFilter
import android.content.SharedPreferences
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
import com.nimble.lupin.user.R
import com.nimble.lupin.user.api.ApiService
import com.nimble.lupin.user.databinding.ActivityMainBinding
import com.nimble.lupin.user.utils.Constants
import com.nimble.lupin.user.utils.NetworkChangeListener
import org.koin.java.KoinJavaComponent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomView: BottomNavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var networkChangeListener: NetworkChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
        Constants.userId = sharedPref.getInt(Constants.User_ID,0)
        val controller = findNavController(R.id.nav_host_fragment_activity_main)
        networkChangeListener = NetworkChangeListener(this@MainActivity)
        bottomView = binding.bottomBarView
        bottomView.setupWithNavController(controller)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawerLayout: DrawerLayout = binding.userDrawerLayout
        val navigationController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_report, R.id.navigation_task), drawerLayout)
        val navigationView : NavigationView = binding.userNavigationView
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
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        bottomView.animate().translationY(bottomView.height.toFloat()).setDuration(800).withEndAction {
            bottomView.visibility = View.GONE
        }.start()

    }
    fun showBottomView(){
        bottomView.visibility = View.VISIBLE
        bottomView.animate().translationY(0f).setDuration(800).start()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener,filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkChangeListener)
    }
}