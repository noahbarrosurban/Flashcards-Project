package flashcards.infrastructure.incoming

import flashcards.application.service.FlashcardService
import flashcards.domain.model.Flashcard
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/flashcards")
class FlashcardController(
    private val flashcardService: FlashcardService
) {

    @PostMapping
    suspend fun create(@RequestBody flashcard: Flashcard): Flashcard =
        flashcardService.create(flashcard)

    @GetMapping
    suspend fun getAll(): List<Flashcard> =
        flashcardService.findAll()

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String): Flashcard =
        flashcardService.findById(id)
            ?: throw NoSuchElementException("Flashcard not found with id=$id")

    @PatchMapping("/{id}")
    suspend fun update(@RequestBody flashcard: Flashcard): Flashcard =
        flashcardService.update(flashcard)

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) =
        flashcardService.deleteById(id)

    @PostMapping("/generate")
    suspend fun generate(@RequestParam topic : String): Flashcard =
        flashcardService.generate(topic)
}
