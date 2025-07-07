package flashcards.infrastructure.incoming

import flashcards.application.service.FlashcardService
import flashcards.domain.model.Flashcard
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/flashcards")
class FlashcardController(private val service: FlashcardService) {

    @PostMapping
    fun create(@RequestBody flashcard: Flashcard): Mono<Flashcard> =
        service.create(flashcard)

    @GetMapping
    fun getAll(): Flux<Flashcard> =
        service.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Mono<Flashcard> =
        service.findById(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): Mono<Void> =
        service.deleteById(id)
}
