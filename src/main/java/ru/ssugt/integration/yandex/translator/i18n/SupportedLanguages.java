package ru.ssugt.integration.yandex.translator.i18n;

public enum SupportedLanguages {
    English("en"),
    Russian("ru"),
    French("fr");

    public final String code;
    SupportedLanguages(String code) {
        this.code = code;
    }
}