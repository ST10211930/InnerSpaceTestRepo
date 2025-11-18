package student.projects.innerspace.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import student.projects.innerspace.data.AppDatabase
import student.projects.innerspace.data.Note
import student.projects.innerspace.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import student.projects.innerspace.ui.home.HomeQuoteViewModel
import student.projects.innerspace.ui.home.QuoteUiState
import student.projects.innerspace.R





class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val quoteViewModel: HomeQuoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val dao = db.noteDao()

        val newNote = Note(
            title = "Calm Day",
            content = "I felt calm today."
        )

        lifecycleScope.launch {
            dao.insert(newNote)
            Log.d("RoomDB", "Note saved locally")
        }

        binding.recentEntriesRecycler.layoutManager = LinearLayoutManager(requireContext())
        dao.getAllNotes().observe(viewLifecycleOwner, Observer { notes ->
            binding.recentEntriesRecycler.adapter = NoteAdapter(notes)
        })

        quoteViewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuoteUiState.Loading -> {
                    binding.quoteText.text = getString(R.string.home_loading_quote) // or "Loading..."
                }
                is QuoteUiState.Success -> {
                    binding.quoteText.text = state.quoteText
                }
                is QuoteUiState.Error -> {
                    binding.quoteText.text = "Stay positive. Keep going."
                }
            }
        }

        quoteViewModel.loadQuote()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
