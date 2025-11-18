package student.projects.innerspace.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import student.projects.innerspace.MainActivity
import student.projects.innerspace.R
import student.projects.innerspace.databinding.ActivityLoginBinding
import student.projects.innerspace.ui.auth.BiometricGateActivity
import student.projects.innerspace.utils.Prefs

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient

    // Launcher for Google Sign-In intent
    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    Log.d("LoginActivity", "Google Sign-In successful")
                    showBiometricPrompt()
                } else {
                    Log.e("LoginActivity", "Google Sign-In failed: ${res.exception?.message}")
                    Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.w("LoginActivity", "Google Sign-In cancelled")
            Toast.makeText(this, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this, gso)

        // Email login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            signInWithEmail(email, pass)
        }

        //  launch RegisterActivity
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        // Google sign-in button
        binding.googleSignInButton.setOnClickListener {
            googleLauncher.launch(googleClient.signInIntent)
        }

        // Biometric button (only visible if enabled)
        if (Prefs.isBiometricEnabled(this)) {
            binding.biometricButton.visibility = android.view.View.VISIBLE
            binding.biometricButton.setOnClickListener {
                startActivity(Intent(this, BiometricGateActivity::class.java))
                finish()
            }
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("LoginActivity", "Email login successful")
                showBiometricPrompt()
            }
            .addOnFailureListener {
                Log.e("LoginActivity", "Email login failed: ${it.message}")
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerWithEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("LoginActivity", "Registration successful")
                showBiometricPrompt()
            }
            .addOnFailureListener {
                Log.e("LoginActivity", "Registration failed: ${it.message}")
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showBiometricPrompt() {
        AlertDialog.Builder(this)
            .setTitle("Enable Biometric Login?")
            .setMessage("Would you like to use fingerprint or face unlock next time?")
            .setPositiveButton("Yes") { _, _ ->
                Prefs.setBiometricEnabled(this, true)
                navigateToMain()
            }
            .setNegativeButton("No") { _, _ ->
                navigateToMain()
            }
            .show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
