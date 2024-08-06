package com.minthanhtike.navigationcomponentwithviewsystem

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navController = findNavController(R.id.nav_host_fragment)
        navController.graph = navController.createGraph(
            startDestination = nav_routes.home
        ) {
            fragment<HomeFragment>(nav_routes.home) {
                label = "home"
            }

            fragment<PlantDetailFragment>("${nav_routes.plant_detail}/{${nav_arguments.plant_id}}") {
                label = "plant_details"
                argument(nav_arguments.plant_id) {
                    type = NavType.StringType
                }
            }
        }
    }
}

object nav_routes {
    const val home = "home"
    const val plant_detail = "plant_details"
}

object nav_arguments {
    const val plant_id = "plant_id"
    const val plant_name = "plant_name"
}