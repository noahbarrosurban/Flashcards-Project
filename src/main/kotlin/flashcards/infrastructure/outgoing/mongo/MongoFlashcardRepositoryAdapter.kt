package flashcards.infrastructure.outgoing.mongo

import flashcards.domain.model.Flashcard
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class MongoFlashcardRepositoryAdapter(
    private val springRepository: SpringFlashcardRepository
) {

    fun save(flashcard: Flashcard): Mono<Flashcard> =
        springRepository.save(flashcard)

    fun findAll(): Flux<Flashcard> =
        springRepository.findAll()

    fun findById(id: String): Mono<Flashcard> =
        springRepository.findById(id)

    fun deleteById(id: String): Mono<Void> =
        springRepository.deleteById(id)
}