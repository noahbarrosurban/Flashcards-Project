package flashcards.application.service

import flashcards.domain.model.Flashcard
import flashcards.infrastructure.outgoing.mongo.MongoFlashcardRepositoryAdapter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class FlashcardService(private val repository: MongoFlashcardRepositoryAdapter) {

    fun create(flashcard: Flashcard): Mono<Flashcard> =
        repository.save(flashcard)

    fun findAll(): Flux<Flashcard> =
        repository.findAll()

    fun findById(id: String): Mono<Flashcard> =
        repository.findById(id)

    fun deleteById(id: String): Mono<Void> =
        repository.deleteById(id)
}