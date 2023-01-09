package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import java.sql.Connection;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final Connection connection = Util.getConnection();//соединение с бд
    private final SessionFactory sessionFactory = Util.getSessionFactory();// создание сессии
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {//создание таблицы
        Transaction transaction = null;//создание транзакции
        try (Session session = Util.getSessionFactory().openSession()) {//создание сессии,открытие совсем другую сессии, которая не связана с транзакцией.
            String create = "CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), lastname VARCHAR(255), age INT)";//создание таблицы,приведет кисключению, если такая таблица уже существует
            session.createNativeQuery(create).executeUpdate();//создаем объект Query для выполнения SQL запроса.,возвращаем количесство столбцов в таблице, на которое повлиял наш SQL – запрос
            transaction.commit();//фиксируем изменения транзакции

        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();//откатываем назад изменения транзакции
            }
        }

    }

    @Override
    public void dropUsersTable() {//Удаление таблицы
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {//создание сессии,открытие совсем другую сессии, которая не связана с транзакцией.
            session.createNativeQuery("DROP TABLE IF EXISTS user").executeUpdate();//создаем объект Query для выполнения SQL запроса, удаление таблицы из бд, не приведет к исключению,
            // если такая таблица уже существует, возвращаем количесство столбцов в таблице, на которое повлиял наш SQL – запрос
            transaction.commit();//фиксируем изменения транзакции
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();//откатываем назад изменения транзакции
            }
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {//добавление user в таблицу
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) { //создание сессии,открытие совсем другую сессии, которая не связана с транзакцией
            transaction = session.beginTransaction(); //начало транзакции
            session.save(new User(name, lastName, age));//добавляет новую запись в таблицу
            transaction.commit();//фиксируем изменения транзакции
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();//откатываем назад изменения транзакции
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {//удаление из таблицы по id
        Transaction transaction = null;
        String delete = "DELETE FROM user WHERE id = ?";//удаление строки где id = ?
            try (Session session = Util.getSessionFactory().openSession()) {//создание сессии,открытие совсем другую сессии, которая не связана с транзакцией
            session.createNativeQuery(delete).executeUpdate();////создаем объект Query для выполнения SQL запроса.,возвращаем количесство столбцов в таблице, на которое повлиял наш SQL – запрос
            transaction.commit();//фиксируем изменения транзакции
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();//откатываем назад изменения транзакции
            }
        }
    }

    @Override
    public List<User> getAllUsers() {//получение всех user ов из таблицы
        try (Session session = Util.getSessionFactory().openSession()) {//создание сессии,открытие совсем другую сессии, которая не связана с транзакцией
            return session.createQuery("from User", User.class).list();//создание объекта Query для выполнения JPQL запрос, выполнение запроса
        }
    }

    @Override
    public void cleanUsersTable() {//очистка таблицы
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {//создание сессии,открытие совсем другую сессии, которая не связана с транзакцией
            session.createNativeQuery("TRUNCATE TABLE user").executeUpdate();//создаем объект Query для выполнения SQL запроса, удаляет данные внутри таблицы, но не саму таблицу.,
                    // если такая таблица уже существует, возвращаем количесство столбцов в таблице, на которое повлиял наш SQL – запрос
            transaction.commit();//фиксируем изменения транзакции
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();//откатываем назад изменения транзакции
            }
        }
    }
}
