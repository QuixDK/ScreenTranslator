package ru.ssugt.i18n;

public enum SupportedLanguages {
    English("en"),
    Russian("ru"),
    French("fr"),
    Korean("ko"),
    Japanese("ja");

    public final String code;
    SupportedLanguages(String code) {
        this.code = code;
    }
}