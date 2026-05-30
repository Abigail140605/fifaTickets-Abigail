package com.example.paninisupporttickets.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.ui.screens.login.LoginScreen
import com.example.paninisupporttickets.ui.screens.ticketdetail.TicketDetailScreen
import com.example.paninisupporttickets.ui.screens.ticketlist.TicketListScreen

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
            TicketListScreen(
                onTicketClick = { ticketId ->
                    navController.navigate(AppDestinations.ticketDetailRoute(ticketId))
                },
                onCreateTicketClick = {
                    navController.navigate(AppDestinations.CREATE_TICKET)
                }
            )
        }

        composable(
            route = AppDestinations.TICKET_DETAIL,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
            TicketDetailScreen(
                ticketId = ticketId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppDestinations.CREATE_TICKET) {
            androidx.compose.material3.Text(UserMessages.Placeholder.CREATE_TICKET)
        }
    }
}
