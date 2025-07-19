package flashcards.application.service

import flashcards.domain.model.Flashcard
import flashcards.infrastructure.outgoing.ai.GeminiClient
import flashcards.infrastructure.outgoing.mongo.MongoFlashcardRepositoryAdapter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
class FlashcardServiceUnitTest {

    @MockK
    private lateinit var flashcardRepository: MongoFlashcardRepositoryAdapter

    @MockK
    private lateinit var geminiClient: GeminiClient

    @InjectMockKs
    private lateinit var flashcardService: FlashcardService

    @Test
    fun `GIVEN valid flashcard, WHEN creating flashcard, THEN returns new flashcard with generated id`() = runTest {
        //GIVEN
        val input = Flashcard(question = "What is the capital of Switzerland?", answer = "Bern.")
        val output = input.copy(id = "123")

        coEvery { flashcardRepository.save(input) } returns output

        //WHEN
        val result = flashcardService.create(input)

        //THEN
        assertNotNull(result)
        assertEquals(output, result)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardRepository.save(input) }
    }

    @Test
    fun `GIVEN flashcards exist, WHEN getting all flashcards, THEN returns complete list`() = runTest {
        //GIVEN
        val output = listOf(
            Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Bern."),
            Flashcard(id = "456", question = "What is the capital of England?", answer = "London.")
        )

        coEvery { flashcardRepository.findAll() } returns output

        //WHEN
        val result = flashcardService.findAll()

        //THEN
        assertNotNull(result)
        assertEquals("123", result[0].id)
        assertEquals("What is the capital of Switzerland?", result[0].question)
        assertEquals("Bern.", result[0].answer)
        assertEquals("456", result[1].id)
        assertEquals("What is the capital of England?", result[1].question)
        assertEquals("London.", result[1].answer)
        coVerify { flashcardRepository.findAll() }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN getting flashcard by id, THEN returns flashcard`() = runTest {
        //GIVEN
        val output = listOf(
            Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Bern."),
            Flashcard(id = "456", question = "What is the capital of England?", answer = "London.")
        )

        coEvery { flashcardRepository.findById("123") } returns output[0]

        //WHEN
        val result = flashcardService.findById("123")

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardRepository.findById("123") }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN updating flashcard, THEN returns updated flashcard`() = runTest {
        //GIVEN
        val input = Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Zurich.")
        val output = input.copy(answer = "Bern.")

        coEvery { flashcardRepository.update(input) } returns output

        //WHEN
        val result = flashcardService.update(input)

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardRepository.update(input) }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN deleting flashcard, THEN returns no content`() = runTest {
        //GIVEN
        val id = "123"

        coEvery { flashcardRepository.deleteById(id) } returns Unit

        //WHEN
        val result = flashcardService.deleteById(id)

        //THEN
        assertNotNull(result)
        coVerify { flashcardRepository.deleteById(id) }
    }

    @Test
    fun `GIVEN valid topic, WHEN creating AI flashcard, THEN returns new flashcard with generated id`() = runTest {
        //GIVEN
        val geminiInput = "What is the capital of Switzerland?"
        val geminiOutput = "Bern."

        coEvery { geminiClient.generate(geminiInput) } returns geminiOutput

        val input = Flashcard(question = geminiInput, answer = geminiOutput)
        val output = input.copy(id = "123")

        coEvery { flashcardRepository.save(input) } returns output

        //WHEN
        val result = flashcardService.generate(geminiInput)

        //THEN
        assertNotNull(result)
        assertEquals(output, result)
        assertEquals("123", result.id)
        assertEquals(geminiInput, result.question)
        assertEquals(geminiOutput, result.answer)
        coVerify { geminiClient.generate(geminiInput) }
        coVerify { flashcardRepository.save(input) }
    }
}
