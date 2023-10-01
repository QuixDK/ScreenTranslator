package ru.ssugt.integration.yandex.translate;

public class YandexTranslateApiConfig {
    private final String apiKey;
    private final String host;


    public YandexTranslateApiConfig(String apiKey, String host) {
        this.apiKey = apiKey;
        this.host = host;
    }


    public String getApiKey() {
        return apiKey;
    }

    public String getHost() {
        return host;
    }
}
