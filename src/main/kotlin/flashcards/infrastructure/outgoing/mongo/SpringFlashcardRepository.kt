package flashcards.infrastructure.outgoing.mongo

import flashcards.domain.model.Flashcard
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringFlashcardRepository : CoroutineCrudRepository<Flashcard, String>
