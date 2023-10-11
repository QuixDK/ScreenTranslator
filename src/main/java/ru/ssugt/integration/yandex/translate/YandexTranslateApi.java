package ru.ssugt.integration.yandex.translate;

public interface YandexTranslateApi {
    String getTranslatedText(String sourceText, String sourceLang, String targetLang);
}
