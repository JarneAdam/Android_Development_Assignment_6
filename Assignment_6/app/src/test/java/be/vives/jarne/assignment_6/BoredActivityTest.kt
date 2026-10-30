package be.vives.jarne.assignment_6

import be.vives.jarne.assignment_6.data.BoredActivityDto
import be.vives.jarne.assignment_6.data.BoredRepository
import be.vives.jarne.assignment_6.data.toDescription
import be.vives.jarne.assignment_6.ui.AddEditToDoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoredActivityTest {

    private val testDispatcher = StandardTestDispatcher()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testJsonDeserialization_appbreweryFormat() {
        // Sample JSON response from https://bored-api.appbrewery.com/random
        val apiJson = """
            {
                "activity": "Surprise your significant other with something considerate",
                "availability": 0.0,
                "type": "social",
                "participants": 1,
                "price": 0.0,
                "accessibility": "Few to no challenges",
                "duration": "minutes",
                "kidFriendly": true,
                "link": "",
                "key": "6204657"
            }
        """.trimIndent()

        val parsed = json.decodeFromString<BoredActivityDto>(apiJson)

        assertEquals("Surprise your significant other with something considerate", parsed.activity)
        assertEquals("social", parsed.type)
        assertEquals(1, parsed.participants)
        assertEquals(0.0, parsed.price, 0.001)
        assertEquals("Few to no challenges", parsed.accessibility)
        assertEquals(0.0, parsed.availability ?: 0.0, 0.001)
        assertEquals("6204657", parsed.key)
    }

    @Test
    fun testToDescription_formatting() {
        val dto = BoredActivityDto(
            activity = "Learn a new song",
            type = "music",
            participants = 1,
            price = 0.25,
            accessibility = "Few to no challenges",
        )

        val description = dto.toDescription()
        val expected = """
            Type: music
            Participants: 1
            Price: 0.25
            Accessibility: Few to no challenges
        """.trimIndent()

        assertEquals(expected, description)
    }

    @Test
    fun testAddEditViewModel_onSurpriseMe_updatesState() = runTest(testDispatcher) {
        val fakeRepository = object : BoredRepository {
            override suspend fun getRandomActivity(): Result<BoredActivityDto> {
                return Result.success(
                    BoredActivityDto(
                        activity = "Host a game night",
                        type = "social",
                        participants = 4,
                        price = 0.1,
                        accessibility = "Few to no challenges",
                    )
                )
            }
        }

        val viewModel = AddEditToDoViewModel(toDoId = null, boredRepository = fakeRepository)

        assertTrue(viewModel.uiState.value.isNewToDo)
        assertEquals("", viewModel.uiState.value.title)
        assertEquals("", viewModel.uiState.value.description)

        viewModel.onSurpriseMe()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Host a game night", state.title)
        val expectedDescription = """
            Type: social
            Participants: 4
            Price: 0.1
            Accessibility: Few to no challenges
        """.trimIndent()
        assertEquals(expectedDescription, state.description)
    }

    @Test
    fun testAddEditViewModel_onSurpriseMe_handlesFailure() = runTest(testDispatcher) {
        val fakeRepository = object : BoredRepository {
            override suspend fun getRandomActivity(): Result<BoredActivityDto> {
                return Result.failure(Exception("Network unreachable"))
            }
        }

        val viewModel = AddEditToDoViewModel(toDoId = null, boredRepository = fakeRepository)

        viewModel.onSurpriseMe()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network unreachable", state.errorMessage)
    }
}
