package flashcards.infrastructure.outgoing.mongo

import flashcards.domain.model.Flashcard
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringFlashcardRepository : ReactiveMongoRepository<Flashcard, String>