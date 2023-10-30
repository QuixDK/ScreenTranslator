package ru.ssugt.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.ssugt.model.RecognizedText;

import java.util.List;


public class RecognizedTextRepositoryImpl implements RecognizedTextRepository{
        private final SessionFactory sessionFactory;

        public RecognizedTextRepositoryImpl(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        public RecognizedText save(RecognizedText text) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(text);
            transaction.commit();
            session.close();
            return text;
        }

        public RecognizedText findById(Long id) {
            Session session = sessionFactory.openSession();
            RecognizedText text = session.get(RecognizedText.class, id);
            session.close();
            return text;
        }

        public List<RecognizedText> findAll() {
            Session session = sessionFactory.openSession();
            List<RecognizedText> texts = session.createQuery("FROM RecognizedText", RecognizedText.class).list();
            session.close();
            return texts;
        }

        public void delete(RecognizedText text) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(text);
            transaction.commit();
            session.close();
        }
    }


