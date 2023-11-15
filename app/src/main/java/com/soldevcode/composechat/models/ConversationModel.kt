package com.soldevcode.composechat.models

import com.soldevcode.composechat.data.dto.gptRequest.ApiMessage
import com.soldevcode.composechat.models.Message.Answer

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
        override val timestamp: Long = System.currentTimeMillis()
    ): Message

    data class Answer(
        override val text: String,
        override val timestamp: Long = System.currentTimeMillis(),
    ): Message
}

fun Message.toApiMessage(): ApiMessage = ApiMessage(
    content = text,
    role = if(this is Answer) "system" else "user",
)