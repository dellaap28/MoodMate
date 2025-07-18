package com.example.moodmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import android.content.res.Configuration
import android.widget.Button


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tampilkan alert ketika aplikasi dibuka
        showMoodAlert()

        // Inisialisasi Toolbar dan pasang sebagai ActionBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeButtonEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Konfigurasi ActionBarDrawerToggle (hamburger menu)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        //toggle.syncState()

        // Set listener untuk menu drawer
        navigationView.setNavigationItemSelectedListener(this)

        // (Optional) Set item default yang aktif saat app dibuka
        navigationView.setCheckedItem(R.id.home)

        // (Optional) Ubah nama user secara dinamis di header
        val headerView = navigationView.getHeaderView(0)

        // Load default fragment saat pertama kali
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }


        // Atur Back Handler Baru (Modern)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Izinkan back jika drawer tertutup
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

    }

    // Tambahan: sinkronkan toggle setelah create
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    // Tambahan: tangani rotasi atau konfigurasi ulang
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> replaceFragment(HomeFragment())
            R.id.moods -> replaceFragment(MoodFragment())
            R.id.about -> replaceFragment(AboutFragment())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showMoodAlert() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_mood, null)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Buat tombol OK menutup dialog
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

     //override fun onBackPressed() {
        //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //drawerLayout.closeDrawer(GravityCompat.START)
        //} else {
            //super.onBackPressed()
        //}
    //}
}