package student.projects.innerspace.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import student.projects.innerspace.data.QuoteRepository
import java.util.*
import android.util.Log
import student.projects.innerspace.api.NetworkModule

sealed class QuoteUiState {
    object Loading : QuoteUiState()
    data class Success(val quoteText: String) : QuoteUiState()
    data class Error(val message: String) : QuoteUiState()
}

class HomeQuoteViewModel : ViewModel() {

    private val repo = QuoteRepository()
    private val _state = MutableLiveData<QuoteUiState>(QuoteUiState.Loading)
    val state: LiveData<QuoteUiState> = _state

    // Simple motivational keywords bias
    private val motivationalKeywords = listOf(
        "success", "motivat", "courage", "believe", "dream", "persever",
        "progress", "growth", "hope", "strength", "determination", "focus",
        "inspire", "achievement", "confidence", "effort"
    )

    fun loadQuote() {
        Log.d("QuotesDebug", "loadQuote() called")
        _state.value = QuoteUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("QuotesDebug", "Starting network fetch")
            try {
                val list = NetworkModule.retrofit
                    .create(student.projects.innerspace.api.QuoteApi::class.java)
                    .getAllQuotes()
                Log.d("QuotesDebug", "Received ${'$'}{list.size} items")
                // ... keep your existing selection logic after this try block ...
            } catch (e: Exception) {
                Log.e("QuotesDebug", "Fetch failed", e)
                _state.postValue(QuoteUiState.Error("Fetch failed: ${'$'}{e.localizedMessage ?: e::class.java.simpleName}"))
            }
        }
    }
}
