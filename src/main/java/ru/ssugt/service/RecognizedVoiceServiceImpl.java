package ru.ssugt.service;

import lombok.AllArgsConstructor;
import ru.ssugt.model.RecognizedText;
import ru.ssugt.model.RecognizedVoice;
import ru.ssugt.repository.RecognizedTextRepository;
import ru.ssugt.repository.RecognizedVoiceRepository;

@AllArgsConstructor
public class RecognizedVoiceServiceImpl implements RecognizedVoiceService{
    private final RecognizedVoiceRepository recognizedVoiceRepository;
    @Override
    public void saveText(String yandexText, byte[] wavFileBytes, String correctAnswer) {
        RecognizedVoice recognizedVoice = new RecognizedVoice();
        recognizedVoice.setYandex_stt(yandexText);
        recognizedVoice.setWav_file_bytes(wavFileBytes);
        recognizedVoice.setCorrect_answer(correctAnswer);
        recognizedVoiceRepository.save(recognizedVoice);
    }
}
