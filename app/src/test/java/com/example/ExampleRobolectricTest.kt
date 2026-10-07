package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.auth.AuthManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Tables Master", appName)
    }

    @Test
    fun verifyPinHashing() {
        val pin = "1234"
        val hash = AuthManager.hashPin(pin)
        assertTrue(AuthManager.verifyPin("1234", hash))
        org.junit.Assert.assertFalse(AuthManager.verifyPin("9999", hash))
    }
}
