package com.soldevcode.composechat.util

data class Languages(
    val languages: String,
    val textLanCode: TextToSpeechCode,
    val recordingLanCode: String

    )

data class TextToSpeechCode(
    val lower: String,
    val upper: String
)

fun getLanguages() =
    listOf(
        Languages("Hungarian", TextToSpeechCode("hu", "HU"), "hu"),
        Languages("United States", TextToSpeechCode("en", "US"), "en"),
        Languages("United Kingdom", TextToSpeechCode("en", "GB"), "en"),
        Languages("Germany", TextToSpeechCode("fr", "Fr"), "fr"),
        Languages("France", TextToSpeechCode("fr", "Fr"), "fr"),
        Languages("Japan", TextToSpeechCode("fr", "Fr"), "fr"),
        Languages("China", TextToSpeechCode("fr", "Fr"), "fr"),
        Languages("Brazil", TextToSpeechCode("fr", "Fr"), "fr"),
        Languages("Poland", TextToSpeechCode("ru", "RU"), "ru"),
        Languages("Portuguese", TextToSpeechCode("ru", "RU"), "ru"),
        Languages("Russian", TextToSpeechCode("ru", "RU"), "ru"),
        Languages("Greece", TextToSpeechCode("ru", "RU"), "ru"),
        Languages("Latvia", TextToSpeechCode("ru", "RU"), "ru")
    )

