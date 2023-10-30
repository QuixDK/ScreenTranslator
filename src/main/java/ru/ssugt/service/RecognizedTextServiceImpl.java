package ru.ssugt.service;

import lombok.AllArgsConstructor;
import ru.ssugt.model.RecognizedText;
import ru.ssugt.repository.RecognizedTextRepository;

@AllArgsConstructor
public class RecognizedTextServiceImpl implements RecognizedTextService {
    private final RecognizedTextRepository recognizedTextRepository;
    @Override
    public void saveText(String yandexText, String tesseractText, String easyText, byte[] base64Picture, String correctAnswer) {
        RecognizedText recognizedText = new RecognizedText();
        recognizedText.setYandex_ocr(yandexText);
        recognizedText.setTesseract_ocr(tesseractText);
        recognizedText.setEasy_ocr(easyText);
        recognizedText.setBase64_picture(base64Picture);
        recognizedText.setCorrect_answer(correctAnswer);
        recognizedTextRepository.save(recognizedText);
    }
}
