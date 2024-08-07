package com.minthanhtike.navigationcomponentwithviewsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class PlantDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(nav_arguments.plant_id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = view.findViewById<TextView>(R.id.data_passing_text)
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        text.text = param1
        val navHostFragment = childFragmentManager.findFragmentById(R.id.home_plan_nav_host)
                as? NavHostFragment
        navHostFragment?.findNavController()?.let { navController ->
            navController.graph = navController.createGraph(
                startDestination = nav_routes.home_plant
            ) {
                fragment<PlantHomtFragment>(nav_routes.home_plant) {
                    label = "Home Plant"
                }
                fragment<FavPlanFragment>(nav_routes.fav_plant) {
                    label = "Fav Plant"
                }

                fragment<ShopPlantFragment>(nav_routes.shop_plant) {
                    label = "Shop Plant"
                }
                fragment<SettingPlantFragment>(nav_routes.setting_plant) {
                    label = "Setting Plant"
                }

            }
        }

        navHostFragment?.findNavController()?.let { navController ->
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.route) {
                    nav_routes.home_plant -> {
                        bottomNav.menu[0].isChecked = true
                    }

                    nav_routes.fav_plant -> {
                        bottomNav.menu[1].isChecked = true
                    }

                    nav_routes.shop_plant -> {
                        bottomNav.menu[2].isChecked = true
                    }

                    nav_routes.setting_plant -> {
                        bottomNav.menu[3].isChecked = true
                    }
                }
            }
            bottomNav.setOnItemSelectedListener { item ->
                onNavSelected(item, navController)
            }
        }
    }

    private fun onNavSelected(item: MenuItem, navController: NavController): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(
                navController.graph.findStartDestination().id,
                inclusive = false
            )
        }
        val options = builder.build()
        val route = when (item.itemId) {
            R.id.home_plant -> {
                navController.navigate(nav_routes.home_plant)
                nav_routes.home_plant
            }

            R.id.fav_plant -> {
                navController.navigate(nav_routes.fav_plant)
                nav_routes.fav_plant
            }

            R.id.shop_plant -> {
                navController.navigate(nav_routes.shop_plant)
                nav_routes.shop_plant
            }

            R.id.setting_plant -> {
                navController.navigate(nav_routes.setting_plant)
                nav_routes.setting_plant
            }

            else -> nav_routes.home_plant
        }
        // Return true only if the destination we've navigated to matches the MenuItem
        Log.d("", "${item.itemId}")
        return try {
            navController.currentDestination?.matchDestination(route) == true
        } catch (e: Exception) {
            false
        }
    }

    private fun NavDestination.matchDestination(route: String): Boolean =
        hierarchy.any { it.route == route }

}