package flashcards.infrastructure.outgoing.mongo

import flashcards.domain.model.Flashcard
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class MongoFlashcardRepositoryAdapter(
    private val springRepository: SpringFlashcardRepository
) {

    suspend fun save(flashcard: Flashcard): Flashcard {
        return springRepository.save(flashcard)
    }

    suspend fun findAll(): List<Flashcard> {
        return springRepository.findAll().toList()
    }

    suspend fun findById(id: String): Flashcard? {
        return springRepository.findById(id)
    }

    suspend fun update(flashcard: Flashcard): Flashcard {
        return springRepository.save(flashcard)
    }

    suspend fun deleteById(id: String) {
        return springRepository.deleteById(id)
    }
}
