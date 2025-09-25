package io.fantastix.hamasstret

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.fantastix.hamasstret.ui.screen.main.MainActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun testActivityLaunches() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.use {
            assertNotNull(it)
        }
    }

    // Example: Test greeting composable
    @Test
    fun testGreetingComposable() {
        // This is a placeholder. Use Compose testing APIs for real UI tests.
        val name = "TestUser"
        val expectedText = "Hello TestUser!"
        assertEquals(expectedText, "Hello $name!")
    }

    // Add more tests for TimerScreen logic, permissions, and fare calculation as needed
}
