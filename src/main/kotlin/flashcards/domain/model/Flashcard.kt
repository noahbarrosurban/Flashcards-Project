package flashcards.domain.model

import org.springframework.data.annotation.Id

data class Flashcard(
    @Id val id: String? = null,
    val question: String,
    val answer: String
)