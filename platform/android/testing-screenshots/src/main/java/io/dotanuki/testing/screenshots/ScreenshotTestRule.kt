package io.dotanuki.testing.screenshots

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dropbox.differ.SimpleImageComparator
import com.dropbox.dropshots.Dropshots
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ScreenshotTestRule<A : Activity, State>(
    private val targetClass: Class<A>,
    private val driver: ScreenshotDriver<A, State>
) : TestRule {

    private val activityRule by lazy {
        ActivityScenarioRule(targetClass)
    }

    private val dropshots by lazy {
        Dropshots(imageComparator = SimpleImageComparator(maxDistance = 0.007f))
    }

    private val chain by lazy {
        RuleChain.outerRule(activityRule).around(dropshots)
    }

    override fun apply(base: Statement, description: Description): Statement = chain.apply(base, description)

    fun checkScreenshot(state: State) {
        activityRule.scenario.run {

            moveToState(Lifecycle.State.CREATED)

            onActivity {
                driver.beforeCapturing(it, state)
                Thread.sleep(1000L)
            }

            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                dropshots.assertSnapshot(it, driver.imageName(state))
            }
        }
    }

    companion object {
        inline fun <reified A : Activity, State> create(driver: ScreenshotDriver<A, State>) =
            ScreenshotTestRule(A::class.java, driver)
    }
}
