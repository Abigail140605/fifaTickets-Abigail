package com.example.paninisupporttickets.data.remote.model

import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.TicketCategory
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.TicketStatus
import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("userName") val userName: String
)

data class TicketResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("providerName") val providerName: String,
    @SerializedName("category") val category: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("reportedBy") val reportedBy: String,
    @SerializedName("location") val location: String
) {
    fun toDomain(): Ticket {
        return Ticket(
            id = id,
            title = title,
            description = description,
            providerName = providerName,
            category = TicketCategory.valueOf(category),
            priority = TicketPriority.valueOf(priority),
            status = TicketStatus.valueOf(status),
            createdAt = createdAt,
            reportedBy = reportedBy,
            location = location
        )
    }
}

data class CreateTicketRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("providerName") val providerName: String,
    @SerializedName("category") val category: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("reportedBy") val reportedBy: String,
    @SerializedName("location") val location: String
)

data class CreateTicketResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("message") val message: String
)

data class UpdateTicketStatusRequestDto(
    @SerializedName("status") val status: String
)

data class UpdateTicketPriorityRequestDto(
    @SerializedName("priority") val priority: String
)

data class ErrorResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int?
)
