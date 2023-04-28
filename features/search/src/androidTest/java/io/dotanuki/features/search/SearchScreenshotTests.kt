package io.dotanuki.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.features.search.presentation.SearchScreenState
import io.dotanuki.features.search.presentation.SearchScreenState.Failed
import io.dotanuki.features.search.ui.SearchView
import io.dotanuki.features.search.util.SearchScreenshotsHelperActivity
import io.dotanuki.platform.android.testing.screenshots.ScreenshotDriver
import io.dotanuki.platform.android.testing.screenshots.ScreenshotTestRule
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenshotTests {

    private fun driver() = object : ScreenshotDriver<SearchScreenshotsHelperActivity, SearchScreenState> {

        override fun beforeCapturing(target: SearchScreenshotsHelperActivity, state: SearchScreenState) {
            val searchView = target.findViewById<SearchView>(R.id.searchScreenRoot)
            listOf(SearchScreenState.Idle, state).forEach { searchView.updateWith(it) }
        }

        override fun imageName(state: SearchScreenState): String =
            "SearchScreenshotTests-${state.javaClass.simpleName}State"
    }

    @get:Rule val screenshotTestRule = ScreenshotTestRule.create(driver())

    @Test fun errorState() {
        val error = HttpNetworkingError.Restful.Server(500)
        val state = Failed(error)
        screenshotTestRule.checkScreenshot(state)
    }

    @Test fun contentWithHistory() {
        val state = SearchScreenState.Success(
            suggestions = listOf("science", "celebrity", "humor"),
            history = listOf("code")
        )

        screenshotTestRule.checkScreenshot(state)
    }
}
