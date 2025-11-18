package student.projects.innerspace.api

import retrofit2.http.GET

data class QuoteDto(
    val text: String?,
    val author: String? // we will ignore author, keep for compatibility
)

interface QuoteApi {
    @GET("api/quotes")
    suspend fun getAllQuotes(): List<QuoteDto>
}
