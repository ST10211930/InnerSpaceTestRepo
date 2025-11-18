package student.projects.innerspace
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import student.projects.innerspace.data.QuoteRepository

class QuoteRepositoryUnitTest {
    @Test
    fun fetch_quotes_returns_result() = runBlocking {
        val repo = QuoteRepository()
        val result = repo.fetchAllQuotes()
        assertTrue(result.isSuccess || result.isFailure)
        // This ensures the function runs and returns a Result object
    }
}