package ru.ssugt.repository;

import ru.ssugt.model.RecognizedVoice;

import java.util.List;

public interface RecognizedVoiceRepository {
    RecognizedVoice save(RecognizedVoice text);
    RecognizedVoice findById(Long id);
    List<RecognizedVoice> findAll();
    void delete(RecognizedVoice text);
}
