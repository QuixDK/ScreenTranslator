package ru.ssugt.repository;

import ru.ssugt.model.RecognizedText;

import java.util.List;


public interface RecognizedTextRepository {
    RecognizedText save(RecognizedText text);
    RecognizedText findById(Long id);
    List<RecognizedText> findAll();
    void delete(RecognizedText text);
}
