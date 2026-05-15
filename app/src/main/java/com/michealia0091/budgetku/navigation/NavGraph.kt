package com.michealia0091.budgetku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            DetailScreen(navController = navController, id = 0L)
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            DetailScreen(
                navController = navController,
                id = id
            )
        }
    }
}