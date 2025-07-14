package flashcards.infrastructure.outgoing.ai

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GeminiClient (
    @Value("\${gemini.api.key}") private val apiKey: String
){

    private val client = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/models")
        .build()

    suspend fun generate(topic: String): String {
        val prompt = """
            Gere uma pergunta e resposta sobre o tema "$topic", sendo extremamente direto.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(
                            text = prompt
                        )
                    )
                )
            )
        )

        val model = "gemini-1.5-flash"

        val response = client.post()
            .uri("/$model:generateContent?key=$apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(GeminiResponse::class.java)
            .awaitSingle()

        val rawText = response
            .candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: throw IllegalStateException("Resposta inválida da API Gemini")

        val answer = Regex("""\*\*Resposta:\*\*\s*(.+)""")
            .find(rawText)
            ?.groupValues
            ?.get(1)
            ?.trim()
            ?: throw IllegalStateException("Não foi possível extrair a resposta")

        return answer
    }
}