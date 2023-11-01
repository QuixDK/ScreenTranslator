package ru.ssugt.forms;

import org.hibernate.SessionFactory;
import ru.ssugt.config.HibernateUtil;
import ru.ssugt.model.RecognizedText;
import ru.ssugt.repository.RecognizedTextRepository;
import ru.ssugt.repository.RecognizedTextRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TextTableForm implements Runnable{
    private JPanel panel1;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final RecognizedTextRepository recognizedTextRepository = new RecognizedTextRepositoryImpl(sessionFactory);

    @Override
    public void run() {
        List<RecognizedText> recognizedTextList = recognizedTextRepository.findAll();
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("id");
        tableModel.addColumn("base64_picture");
        tableModel.addColumn("easy_ocr");
        tableModel.addColumn("tesseract_ocr");
        tableModel.addColumn("yandex_ocr");
        tableModel.addColumn("correct_answer");

        for (RecognizedText recognizedText : recognizedTextList) {
            Object[] row = new Object[] { recognizedText.getId(), recognizedText.getBase64_picture(),
            recognizedText.getEasy_ocr(), recognizedText.getTesseract_ocr(), recognizedText.getYandex_ocr(),
            recognizedText.getCorrect_answer()};

            tableModel.addRow(row);
        }
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Recognized Text");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
}
