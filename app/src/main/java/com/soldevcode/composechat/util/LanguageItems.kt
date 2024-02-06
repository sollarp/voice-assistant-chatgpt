package com.soldevcode.composechat.util

data class LanguageItems(
    var languages: String,
    val textLanCode: TextToSpeechCode,
    val recordingLanCode: String

    )

data class TextToSpeechCode(
    val lower: String,
    val upper: String
)

fun getLanguages() =
    listOf(
        LanguageItems("Hungarian", TextToSpeechCode("hu", "HU"), "hu"),
        LanguageItems("United States", TextToSpeechCode("en", "US"), "en"),
        LanguageItems("United Kingdom", TextToSpeechCode("en", "GB"), "en"),
        LanguageItems("Germany", TextToSpeechCode("fr", "Fr"), "fr"),
        LanguageItems("France", TextToSpeechCode("fr", "Fr"), "fr"),
        LanguageItems("Japan", TextToSpeechCode("fr", "Fr"), "fr"),
        LanguageItems("China", TextToSpeechCode("fr", "Fr"), "fr"),
        LanguageItems("Brazil", TextToSpeechCode("fr", "Fr"), "fr"),
        LanguageItems("Poland", TextToSpeechCode("ru", "RU"), "ru"),
        LanguageItems("Portuguese", TextToSpeechCode("ru", "RU"), "ru"),
        LanguageItems("Russian", TextToSpeechCode("ru", "RU"), "ru"),
        LanguageItems("Greece", TextToSpeechCode("ru", "RU"), "ru"),
        LanguageItems("Latvia", TextToSpeechCode("ru", "RU"), "ru")
    )

