package com.example.paninisupporttickets.data.remote

import com.example.paninisupporttickets.core.AppConstants
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
    @POST(AppConstants.Api.Paths.AUTH_LOGIN)
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @GET(AppConstants.Api.Paths.TICKETS)
    suspend fun getTickets(): List<TicketResponseDto>

    @GET(AppConstants.Api.Paths.TICKET_DETAIL)
    suspend fun getTicket(@Path("ticketId") ticketId: String): TicketResponseDto

    @POST(AppConstants.Api.Paths.TICKETS)
    suspend fun createTicket(@Body request: CreateTicketRequestDto): CreateTicketResponseDto

    @PATCH(AppConstants.Api.Paths.TICKET_STATUS)
    suspend fun updateTicketStatus(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketStatusRequestDto
    ): TicketResponseDto

    @PATCH(AppConstants.Api.Paths.TICKET_PRIORITY)
    suspend fun updateTicketPriority(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketPriorityRequestDto
    ): TicketResponseDto
}
