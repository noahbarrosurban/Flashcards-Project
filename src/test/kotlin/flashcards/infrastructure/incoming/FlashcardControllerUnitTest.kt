package flashcards.infrastructure.incoming

import com.ninjasquad.springmockk.MockkBean
import flashcards.application.service.FlashcardService
import flashcards.domain.model.Flashcard
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import kotlin.test.assertEquals

@WebFluxTest(FlashcardController::class)
class FlashcardControllerUnitTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var flashcardService: FlashcardService

    @Test
    fun `GIVEN valid flashcard, WHEN creating flashcard, THEN returns new flashcard with generated id`() = runTest {
        //GIVEN
        val input = Flashcard(question = "What is the capital of Switzerland?", answer = "Bern.")
        val output = input.copy(id="123")

        coEvery { flashcardService.create(input) } returns output

        //WHEN
        val result = webTestClient.post()
            .uri("/flashcards")
            .bodyValue(input)
            .exchange()
            .expectStatus().isOk
            .expectBody<Flashcard>()
            .returnResult()
            .responseBody

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardService.create(input) }
    }

    @Test
    fun `GIVEN flashcards exist, WHEN getting all flashcards, THEN returns complete list`() = runTest {
        //GIVEN
        val flashcards = listOf(
            Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Bern."),
            Flashcard(id = "456", question = "What is the capital of England?", answer = "London.")
        )

        coEvery { flashcardService.findAll() } returns flashcards

        //WHEN
        val result = webTestClient.get()
            .uri("/flashcards")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Flashcard>()
            .returnResult()
            .responseBody

        //THEN
        assertNotNull(result)
        assertEquals("123", result[0].id)
        assertEquals("What is the capital of Switzerland?", result[0].question)
        assertEquals("Bern.", result[0].answer)
        assertEquals("456", result[1].id)
        assertEquals("What is the capital of England?", result[1].question)
        assertEquals("London.", result[1].answer)
        coVerify { flashcardService.findAll() }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN getting flashcard by id, THEN returns flashcard`() = runTest {
        //GIVEN
        val output = listOf(
            Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Bern."),
            Flashcard(id = "456", question = "What is the capital of England?", answer = "London.")
        )

        coEvery { flashcardService.findById("123") } returns output[0]

        //WHEN
        val result = webTestClient.get()
            .uri("/flashcards/123")
            .exchange()
            .expectStatus().isOk
            .expectBody<Flashcard>()
            .returnResult()
            .responseBody

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardService.findById("123") }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN updating flashcard, THEN returns updated flashcard`() = runTest {
        //GIVEN
        val input = Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Zurich.")
        val output = input.copy(answer = "Bern.")

        coEvery { flashcardService.update(input) } returns output

        //WHEN
        val result = webTestClient.patch()
            .uri("/flashcards/123")
            .bodyValue(input)
            .exchange()
            .expectStatus().isOk
            .expectBody<Flashcard>()
            .returnResult()
            .responseBody

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardService.update(input) }
    }

    @Test
    fun `GIVEN flashcard exists, WHEN deleting flashcard, THEN returns no content`() = runTest {
        //GIVEN
        val id = "123"

        coEvery { flashcardService.deleteById(id) } returns Unit

        //WHEN
        val result = webTestClient.delete()
            .uri("/flashcards/$id")
            .exchange()
            .expectStatus().isOk
            .expectBody().isEmpty

        //THEN
        kotlin.test.assertNotNull(result)
        coVerify { flashcardService.deleteById(id) }
    }

    @Test
    fun `GIVEN valid topic, WHEN creating AI flashcard, THEN returns new flashcard with generated id`() = runTest {
        //GIVEN
        val input = "What is the capital of Switzerland?"
        val output = Flashcard(id = "123", question = "What is the capital of Switzerland?", answer = "Bern.")

        coEvery { flashcardService.generate(input) } returns output

        //WHEN
        val result = webTestClient.post()
            .uri("/flashcards/generate?topic=$input")
            .exchange()
            .expectStatus().isOk
            .expectBody<Flashcard>()
            .returnResult()
            .responseBody

        //THEN
        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("What is the capital of Switzerland?", result.question)
        assertEquals("Bern.", result.answer)
        coVerify { flashcardService.generate(input) }
    }
}
