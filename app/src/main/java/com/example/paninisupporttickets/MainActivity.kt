package com.example.paninisupporttickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.paninisupporttickets.navigation.AppNavHost
import com.example.paninisupporttickets.ui.theme.PaniniSupportTicketsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaniniSupportTicketsTheme {
                AppNavHost()
            }
        }
    }
}
