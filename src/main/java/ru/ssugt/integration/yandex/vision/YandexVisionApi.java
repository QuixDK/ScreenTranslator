package ru.ssugt.integration.yandex.vision;

import java.util.List;

public interface YandexVisionApi {
    public String recognizeText(String mimeType, List<String> languagesCodes, String model, byte[] content);
}
