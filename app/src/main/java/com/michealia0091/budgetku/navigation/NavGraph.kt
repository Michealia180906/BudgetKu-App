package com.michealia0091.budgetku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.michealia0091.budgetku.ui.screen.DetailScreen
import com.michealia0091.budgetku.ui.screen.MainScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.About.route) {
            DetailScreen(navController)
        }
    }
}