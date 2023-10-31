package ru.ssugt.service;

public interface RecognizedVoiceService {
    void saveText(String yandexText, byte[] wavFileBytes, String correctAnswer);
}
