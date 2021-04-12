package org.eboalapp.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference


object CustomBottomNavigationUi {

    fun setupWithNavController(
        bottomNavigationView: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            onNavDestinationSelected(
                item,
                navController
            )
        }
        val weakReference = WeakReference(bottomNavigationView)
        navController.addOnDestinationChangedListener(
            object : OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination, arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    val menu = view.menu
                    var counter = 0
                    val size = menu.size()
                    while (counter < size) {
                        val item = menu.getItem(counter)
                        if (matchDestination(destination, item.itemId)) {
                            item.isChecked = true
                        }
                        counter++
                    }
                }
            })
    }

    private fun onNavDestinationSelected(
        item: MenuItem,
        navController: NavController
    ): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            findStartDestination(navController.graph)?.let { startDestination ->
                builder.setPopUpTo(startDestination.id, false)
            }
        }
        val options = builder.build()
        return try {
            //TODO provide proper API instead of using Exceptions as Control-Flow.
            navController.navigate(item.itemId, null, options)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun matchDestination(
        destination: NavDestination,
        @IdRes destId: Int
    ): Boolean {
        var currentDestination: NavDestination? = destination
        while (currentDestination!!.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent
        }
        return currentDestination.id == destId
    }

    private fun findStartDestination(graph: NavGraph): NavDestination? {
        var startDestination: NavDestination? = graph
        while (startDestination is NavGraph) {
            val parent = startDestination
            startDestination = parent.findNode(parent.startDestination)
        }
        return startDestination
    }

}
