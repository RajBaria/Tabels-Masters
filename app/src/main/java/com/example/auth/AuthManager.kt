package com.example.auth

import java.security.MessageDigest

object AuthManager {

    private const val SALT = "GadiyaMasterSalt2026#"

    fun hashPin(pin: String): String {
        val bytes = (SALT + pin).toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun verifyPin(pin: String, storedHash: String?): Boolean {
        if (storedHash.isNullOrBlank()) return true // No PIN set
        return hashPin(pin) == storedHash
    }

    val avatarOptions = listOf(
        "🦁", "🐯", "🦊", "🐼", "🦄", "🚀", "⭐", "🎓", "🦉", "🏆", "🎯", "⚡"
    )
}
