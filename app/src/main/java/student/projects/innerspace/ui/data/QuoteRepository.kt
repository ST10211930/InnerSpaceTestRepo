package student.projects.innerspace.data

import student.projects.innerspace.api.NetworkModule
import student.projects.innerspace.api.QuoteDto

class QuoteRepository {
    private val api = NetworkModule.quoteApi

    suspend fun fetchAllQuotes(): Result<List<QuoteDto>> {
        return try {
            val list = api.getAllQuotes()
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
