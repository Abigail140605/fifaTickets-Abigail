package com.example.paninisupporttickets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.paninisupporttickets.ui.screens.login.LoginScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.TICKETS) {
                        popUpTo(AppDestinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppDestinations.TICKETS) {
            androidx.compose.material3.Text("Ticket List - Próximamente")
        }

        composable(
            route = AppDestinations.TICKET_DETAIL,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
            androidx.compose.material3.Text("Ticket Detail: $ticketId - Próximamente")
        }

        composable(route = AppDestinations.CREATE_TICKET) {
            androidx.compose.material3.Text("Create Ticket - Próximamente")
        }
    }
}
