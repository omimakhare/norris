package io.dotanuki.features.facts.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.facts.di.FactsContext
import io.dotanuki.features.facts.di.FactsViewModelFactory
import io.dotanuki.features.facts.presentation.FactsScreenState.Empty
import io.dotanuki.features.facts.presentation.FactsScreenState.Failed
import io.dotanuki.features.facts.presentation.FactsScreenState.Idle
import io.dotanuki.features.facts.presentation.FactsScreenState.Loading
import io.dotanuki.features.facts.presentation.FactsScreenState.Success
import io.dotanuki.platform.android.testing.persistence.StorageTestHelper
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class FactsViewModelTests {

    private val restHelper = RestTestHelper()
    private val storageHelper = StorageTestHelper()

    private val testContext = FactsContext(restHelper.restClient, storageHelper.storage)
    private val viewModel = with(testContext) { FactsViewModelFactory().create() }

    @Test fun `at first lunch, should start on empty state`() = runBlocking {
        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `given a successful a search, should emit results`() = runBlocking {
        val factId = UUID.randomUUID().toString()
        val divideByZero = "Chuck Norris can divide by zero"

        val restScenario = RestScenario.Facts(factId, divideByZero)

        restHelper.defineScenario(restScenario)

        val previousSearch = "humor"
        storageHelper.storage.registerNewSearch(previousSearch)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)

            val facts = listOf(
                FactDisplayRow(
                    url = "https://api.chucknorris.io/jokes/$factId",
                    fact = divideByZero,
                    displayWithSmallerFontSize = false
                )
            )

            val presentation = FactsPresentation(previousSearch, facts)

            assertThat(awaitItem()).isEqualTo(Success(presentation))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `given an unsuccessful a search, should emit error`() = runBlocking {
        val previousSearch = "humor"
        storageHelper.storage.registerNewSearch(previousSearch)

        val incomingError = HttpNetworkingError.Connectivity.OperationTimeout

        val restScenario = RestScenario.Error(incomingError)
        restHelper.defineScenario(restScenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Failed(incomingError))
            cancelAndIgnoreRemainingEvents()
        }
    }
}
