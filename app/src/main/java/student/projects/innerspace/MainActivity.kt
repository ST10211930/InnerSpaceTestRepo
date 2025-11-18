package student.projects.innerspace

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import student.projects.innerspace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request notification permission
        requestNotificationPermission()

        // Set up navigation controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Connect bottom nav to nav controller
        binding.bottomNav.setupWithNavController(navController)

        // ✅ Handle FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                android.util.Log.d("FCM", "Device token: $token")

                // Optional: Save token to Firestore under user's UID
                val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val tokenMap = mapOf("fcmToken" to token)
                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .set(tokenMap, com.google.firebase.firestore.SetOptions.merge())
                }
            } else {
                android.util.Log.w("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 1001)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val langCode = student.projects.innerspace.util.LocaleHelper.getPersistedLanguage(newBase)
        val context = student.projects.innerspace.util.LocaleHelper.setLocale(newBase, langCode)
        super.attachBaseContext(context)
    }
}

//bez was here, 2nd attempt

