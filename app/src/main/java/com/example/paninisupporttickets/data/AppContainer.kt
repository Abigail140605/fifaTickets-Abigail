package com.example.paninisupporttickets.data

import com.example.paninisupporttickets.data.remote.RetrofitClient
import com.example.paninisupporttickets.data.repository.TicketRepository

object AppContainer {
    private val apiService = RetrofitClient.apiService

    val ticketRepository: TicketRepository by lazy {
        TicketRepository(apiService = apiService)
    }
}
