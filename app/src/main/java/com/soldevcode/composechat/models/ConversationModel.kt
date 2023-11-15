package com.soldevcode.composechat.models

data class ConversationModel(
    var question: String = "",
    var answer: String ="",
    var chatId: String = "test",
    var chatOwner: String = "",
)

sealed interface Message {
    val text: String
    val timestamp: Long

    data class Question(
        override val text: String,
        override val timestamp: Long,
    ): Message

    data class Answer(
        override val text: String,
        override val timestamp: Long,
    ): Message
}

