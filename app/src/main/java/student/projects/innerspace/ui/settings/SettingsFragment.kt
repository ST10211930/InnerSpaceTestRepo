package student.projects.innerspace.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import student.projects.innerspace.databinding.FragmentSettingsBinding
import student.projects.innerspace.util.LocaleHelper
import com.google.firebase.messaging.FirebaseMessaging

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val languages = listOf("English", "Afrikaans", "Zulu")
    private val codes = listOf("en", "af", "zu")
    private var isSpinnerInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Theme toggle
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val theme = if (isChecked) "Dark" else "Light"
            Toast.makeText(requireContext(), "Theme set to $theme", Toast.LENGTH_SHORT).show()
            // TODO: Save to SharedPreferences or apply theme logic here
        }

        // Font size slider
        binding.fontSizeSlider.addOnChangeListener { _, value, _ ->
            Toast.makeText(requireContext(), "Font size: ${value.toInt()}", Toast.LENGTH_SHORT).show()
            // TODO: Save font size preference
        }

        // Language dropdown
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        // Preselect saved language
        val currentLang = LocaleHelper.getPersistedLanguage(requireContext())
        val selectedIndex = codes.indexOf(currentLang)
        if (selectedIndex >= 0) {
            binding.languageSpinner.setSelection(selectedIndex)
        }

        // Handle language selection
        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true
                    return
                }

                val selectedLangCode = codes[position]
                val currentLangCode = LocaleHelper.getPersistedLanguage(requireContext())

                if (selectedLangCode != currentLangCode) {
                    LocaleHelper.persistLanguage(requireContext(), selectedLangCode)

                    Toast.makeText(requireContext(), "Language set to ${languages[position]}", Toast.LENGTH_SHORT).show()

                    // Force full restart to apply locale globally
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Notification toggle
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(requireContext(), "Notifications $status", Toast.LENGTH_SHORT).show()
            // TODO: Save notification preference
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("daily_updates")
                Toast.makeText(requireContext(), "Notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("daily_updates")
                Toast.makeText(requireContext(), "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }
}
