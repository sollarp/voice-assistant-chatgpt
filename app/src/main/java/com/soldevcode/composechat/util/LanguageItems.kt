package com.soldevcode.composechat.util

data class LanguageItems(
    var languages: String,
    val textLanCode: TextToSpeechItems,
    val recordingLanCode: String
)

data class TextToSpeechItems(
    val lower: String,
    val upper: String
)

fun getLanguages() =
    listOf(
        LanguageItems("Hungarian", TextToSpeechItems("hu", "HU"), "hu"),
        LanguageItems("United States", TextToSpeechItems("en", "US"), "en"),
        LanguageItems("United Kingdom", TextToSpeechItems("en", "GB"), "en"),
        LanguageItems("Germany", TextToSpeechItems("fr", "Fr"), "fr"),
        LanguageItems("France", TextToSpeechItems("fr", "Fr"), "fr"),
        LanguageItems("Japan", TextToSpeechItems("fr", "Fr"), "fr"),
        LanguageItems("China", TextToSpeechItems("fr", "Fr"), "fr"),
        LanguageItems("Brazil", TextToSpeechItems("fr", "Fr"), "fr"),
        LanguageItems("Poland", TextToSpeechItems("ru", "RU"), "ru"),
        LanguageItems("Portuguese", TextToSpeechItems("ru", "RU"), "ru"),
        LanguageItems("Russian", TextToSpeechItems("ru", "RU"), "ru"),
        LanguageItems("Greece", TextToSpeechItems("ru", "RU"), "ru"),
        LanguageItems("Latvia", TextToSpeechItems("ru", "RU"), "ru")
    )

