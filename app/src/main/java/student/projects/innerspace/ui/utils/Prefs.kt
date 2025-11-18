package student.projects.innerspace.utils

import android.content.Context

object Prefs {
    private const val PREF_NAME = "innerspace_prefs"
    private const val KEY_BIOMETRIC = "biometric_enabled"

    fun setBiometricEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_BIOMETRIC, enabled).apply()
    }

    fun isBiometricEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BIOMETRIC, false)
    }
}
