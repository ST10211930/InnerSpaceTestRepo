package student.projects.innerspace.ui.entry

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import student.projects.innerspace.data.AppDatabase
import student.projects.innerspace.data.Note
import student.projects.innerspace.databinding.FragmentEntryBinding
import kotlinx.coroutines.launch
import java.io.InputStream
import android.net.Uri
import student.projects.innerspace.R


class EntryFragment : Fragment() {

    private lateinit var binding: FragmentEntryBinding
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)

        // Save journal entry
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(title = title, content = content)
            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).noteDao().insert(note)
                Toast.makeText(requireContext(), "Entry saved", Toast.LENGTH_SHORT).show()
                binding.titleEditText.setText("")
                binding.contentEditText.setText("")
                binding.imagePreview.setImageDrawable(null)
            }
        }

        // Attach image from gallery
        binding.attachImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Font customization
        binding.fontStyleButton.setOnClickListener {
            val sizes = arrayOf("Small", "Medium", "Large")
            AlertDialog.Builder(requireContext())
                .setTitle("Choose Font Size")
                .setItems(sizes) { _, which ->
                    when (which) {
                        0 -> binding.contentEditText.textSize = 14f
                        1 -> binding.contentEditText.textSize = 18f
                        2 -> binding.contentEditText.textSize = 22f
                    }
                }
                .show()

            val styles = arrayOf("Normal", "Italic", "Bold")
            AlertDialog.Builder(requireContext())
                .setTitle("Choose Font Style")
                .setItems(styles) { _, which ->
                    when (which) {
                        0 -> binding.contentEditText.setTypeface(null, Typeface.NORMAL)
                        1 -> binding.contentEditText.setTypeface(null, Typeface.ITALIC)
                        2 -> binding.contentEditText.setTypeface(null, Typeface.BOLD)
                    }
                }
                .show()
        }

        // Predefined wallpapers
        binding.wallpaperButton.setOnClickListener {
            val wallpapers = arrayOf("Soft Gradient", "Calm Blue", "Warm Sunset")
            AlertDialog.Builder(requireContext())
                .setTitle("Choose Wallpaper")
                .setItems(wallpapers) { _, which ->
                    when (which) {
                        0 -> binding.entryScroll.setBackgroundResource(R.drawable.wallpaper_gradient)
                        1 -> binding.entryScroll.setBackgroundResource(R.drawable.wallpaper_blue)
                        2 -> binding.entryScroll.setBackgroundResource(R.drawable.wallpaper_sunset)
                    }
                }
                .show()
        }

        return binding.root
    }

    // Handle image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val inputStream: InputStream? = selectedImageUri?.let {
                requireContext().contentResolver.openInputStream(it)
            }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
        }
    }
}
