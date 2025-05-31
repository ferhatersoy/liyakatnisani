package com.example.liyakatnisani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.liyakatnisani.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // AppBarConfiguration'ı top-level destination'larınızla yapılandırın.
        // subjectsFragment, lectureNotesFragment gibi fragment'lar top-level olabilir.
        // Şimdilik sadece SubjectsFragment'ı top-level olarak kabul edelim.
        // Eğer drawer menu vs. olursa, oradaki tüm item'lar buraya eklenebilir.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.subjectsFragment) // subjectsFragment'ın ID'sini nav_graph'tan alın
        )
        // appBarConfiguration = AppBarConfiguration(navController.graph) // Eğer tüm fragmentlar top level ise

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
