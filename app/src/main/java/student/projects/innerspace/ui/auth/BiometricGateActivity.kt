package student.projects.innerspace.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import student.projects.innerspace.MainActivity
import student.projects.innerspace.R

class BiometricGateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val capability = BiometricManager.from(this).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )

        if (capability == BiometricManager.BIOMETRIC_SUCCESS) {
            showPrompt()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_cancel))
            .build()

        val prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                startActivity(Intent(this@BiometricGateActivity, MainActivity::class.java))
                finish()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Toast.makeText(this@BiometricGateActivity, errString, Toast.LENGTH_SHORT).show()
            }
            override fun onAuthenticationFailed() {
                Toast.makeText(this@BiometricGateActivity, getString(R.string.biometric_failed), Toast.LENGTH_SHORT).show()
            }
        })
        prompt.authenticate(info)
    }
}
