package student.projects.innerspace.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import student.projects.innerspace.MainActivity
import student.projects.innerspace.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Register button
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            val pass = binding.etRegisterPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerWithEmail(email, pass)
        }

        // Go back to login
        binding.txtGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Registration successful")
                navigateToMain()
            }
            .addOnFailureListener {
                Log.e("RegisterActivity", "Registration failed: ${it.message}")
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
