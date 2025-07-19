package flashcards.application.service

import flashcards.domain.model.Flashcard
import flashcards.infrastructure.outgoing.ai.GeminiClient
import flashcards.infrastructure.outgoing.mongo.MongoFlashcardRepositoryAdapter
import org.springframework.stereotype.Service

@Service
class FlashcardService(
    private val flashcardRepository: MongoFlashcardRepositoryAdapter,
    private val geminiClient: GeminiClient
) {

    suspend fun create(flashcard: Flashcard): Flashcard =
        flashcardRepository.save(flashcard)

    suspend fun findAll(): List<Flashcard> =
        flashcardRepository.findAll()

    suspend fun findById(id: String): Flashcard? =
        flashcardRepository.findById(id)

    suspend fun update(flashcard: Flashcard): Flashcard =
        flashcardRepository.update(flashcard)

    suspend fun deleteById(id: String) =
        flashcardRepository.deleteById(id)

    suspend fun generate(topic: String): Flashcard {
        val response = geminiClient.generate(topic)

        return create(
            Flashcard(
                question = topic,
                answer = response
            )
        )
    }
}
