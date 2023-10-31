package ru.ssugt.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.ssugt.model.RecognizedText;
import ru.ssugt.model.RecognizedVoice;

import java.util.List;

@AllArgsConstructor

public class RecognizedVoiceRepositoryImpl implements RecognizedVoiceRepository{
    private final SessionFactory sessionFactory;

    @Override
    public RecognizedVoice save(RecognizedVoice text) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(text);
        transaction.commit();
        session.close();
        return text;
    }

    @Override
    public RecognizedVoice findById(Long id) {
        Session session = sessionFactory.openSession();
        RecognizedVoice text = session.get(RecognizedVoice.class, id);
        session.close();
        return text;
    }

    @Override
    public List<RecognizedVoice> findAll() {
        Session session = sessionFactory.openSession();
        String query = "FROM RecognizedVoice SELECT ";
        List<RecognizedVoice> texts = session.createQuery(query, RecognizedVoice.class).list();
        session.close();
        return texts;
    }

    @Override
    public void delete(RecognizedVoice text) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(text);
        transaction.commit();
        session.close();
    }
}
