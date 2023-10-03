package ru.ssugt.integration.yandex.vision;

import java.util.List;

public interface YandexVisionApi {
    public StringBuilder recognizeText(String mimeType, List<String> languagesCodes, String model, byte[] content);
}
