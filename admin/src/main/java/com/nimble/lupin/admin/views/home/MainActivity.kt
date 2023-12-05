package com.nimble.lupin.admin.views.home

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.nimble.lupin.admin.R
import com.nimble.lupin.admin.databinding.ActivityMainBinding
import com.nimble.lupin.admin.utils.CircleImageView
import com.nimble.lupin.admin.utils.Constants
import com.nimble.lupin.admin.utils.NetworkChangeListener
import com.nimble.lupin.admin.views.login.LoginActivity
import org.koin.java.KoinJavaComponent
import java.security.Permission

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var networkBroadcaster: NetworkChangeListener
    private val sharedPref: SharedPreferences by KoinJavaComponent.inject(SharedPreferences::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.Admin_ID = sharedPref.getInt(Constants.Admin_ID_Key,0)
        binding.adminNavigationView.getHeaderView(0).findViewById<AppCompatTextView>(R.id.header_userName).text =   sharedPref.getString(Constants.Admin_Username_Key,"")
       val navigationProfileView =  binding.adminNavigationView.getHeaderView(0).findViewById<CircleImageView>(R.id.header_imageView)
        Glide.with(this).load(sharedPref.getString(Constants.Admin_Image_Key,"")).into(navigationProfileView)
        val controller = findNavController(R.id.nav_host_fragment_activity_main)
        networkBroadcaster = NetworkChangeListener(this@MainActivity)
        binding.bottomBarView.setupWithNavController(controller)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawerLayout: DrawerLayout = binding.adminDrawerLayout
        val navigationController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_report,
                R.id.navigation_schedule
            ), drawerLayout
        )
        val navigationView: NavigationView = binding.adminNavigationView
        navigationView.setupWithNavController(navigationController)
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {}
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideBottomView() {
        binding.bottomBarView.animate().translationY(binding.bottomBarView.height.toFloat())
            .setDuration(800).withEndAction {
            binding.bottomBarView.visibility = View.GONE
        }.start()
        binding.toolbar.visibility = View.GONE
    }

    fun showBottomView() {

        binding.toolbar.visibility = View.VISIBLE
        binding.bottomBarView.visibility = View.VISIBLE
        binding.bottomBarView.animate().translationY(0f).setDuration(800).start()

    }



    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        applicationContext.registerReceiver(networkBroadcaster, filter)
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(networkBroadcaster)
        }
        catch (_: Exception){

        }
    }

}