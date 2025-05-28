package ru.ssugt.forms;

import org.hibernate.SessionFactory;
import ru.ssugt.config.HibernateUtil;
import ru.ssugt.model.RecognizedText;
import ru.ssugt.model.RecognizedVoice;
import ru.ssugt.repository.RecognizedTextRepository;
import ru.ssugt.repository.RecognizedTextRepositoryImpl;
import ru.ssugt.repository.RecognizedVoiceRepository;
import ru.ssugt.repository.RecognizedVoiceRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VoiceTableForm implements Runnable{
    private JPanel panel1;
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final RecognizedVoiceRepository recognizedVoiceRepository = new RecognizedVoiceRepositoryImpl(sessionFactory);

    @Override
    public void run() {
        List<RecognizedVoice> recognizedVoiceList = recognizedVoiceRepository.findAll();
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("id");
        tableModel.addColumn("wav_file_bytes");
        tableModel.addColumn("yandex_stt");
        tableModel.addColumn("correct_answer");

        for (RecognizedVoice recognizedVoice : recognizedVoiceList) {
            Object[] row = new Object[] { recognizedVoice.getId(), recognizedVoice.getWav_file_bytes(),
            recognizedVoice.getYandex_stt(), recognizedVoice.getCorrect_answer()};

            tableModel.addRow(row);
        }
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JFrame frame = new JFrame("Recognized Voice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
}
