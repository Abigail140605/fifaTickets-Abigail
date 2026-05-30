package com.example.paninisupporttickets.data.remote

import com.example.paninisupporttickets.data.remote.model.CreateTicketRequestDto
import com.example.paninisupporttickets.data.remote.model.CreateTicketResponseDto
import com.example.paninisupporttickets.data.remote.model.LoginRequestDto
import com.example.paninisupporttickets.data.remote.model.LoginResponseDto
import com.example.paninisupporttickets.data.remote.model.TicketResponseDto
import com.example.paninisupporttickets.data.remote.model.UpdateTicketPriorityRequestDto
import com.example.paninisupporttickets.data.remote.model.UpdateTicketStatusRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @GET("tickets")
    suspend fun getTickets(): List<TicketResponseDto>

    @GET("tickets/{ticketId}")
    suspend fun getTicket(@Path("ticketId") ticketId: String): TicketResponseDto

    @POST("tickets")
    suspend fun createTicket(@Body request: CreateTicketRequestDto): CreateTicketResponseDto

    @PATCH("tickets/{ticketId}/status")
    suspend fun updateTicketStatus(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketStatusRequestDto
    ): TicketResponseDto

    @PATCH("tickets/{ticketId}/priority")
    suspend fun updateTicketPriority(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketPriorityRequestDto
    ): TicketResponseDto
}
