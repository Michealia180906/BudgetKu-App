package com.michealia0091.budgetku.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("mainScreen")

    data object About : Screen("aboutScreen")

    data object FormUbah : Screen("formUbah/{id}") {
        fun withId(id: Long): String {
            return "formUbah/$id"
        }
    }
}