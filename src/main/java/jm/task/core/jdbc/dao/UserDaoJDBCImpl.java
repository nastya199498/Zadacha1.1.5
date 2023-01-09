package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() { // Создание таблицы
        String str = "CREATE TABLE user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), lastName VARCHAR(255), age INT)"; //создаем таблицу user с 4 столбцами
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(str);// создание таблицы
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() { //Удаление таблицы
        String str = "DROP TABLE IF EXISTS user";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement preparedStatement = null;
        String insert = """
                INSERT INTO user
                (name, lastname, age)
                VALUES
                (?,?,?);
                """;
        try {
            preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeUserById(long id) { //Удаление из таблицы по id
        PreparedStatement preparedStatement = null;
        String str = "DELETE FROM User WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        String select = "SELECT * FROM  USER;";
        List<User> userList = new ArrayList<>();
        try {ResultSet resultSet = connection.createStatement().executeQuery(select);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }


    /*
    public List<User> getAllUsers() { //Получение всех User из базы
        List<User> userList = new ArrayList<>();
        String str = "SELECT id, name, lastName, age FROM User";
        SELECT * FROM  USER
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(str);
            while (resultSet.next()) {
                User user = new User();//(resultSet.getString("name"), resultSet.getString("lastname"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    } */

    public void cleanUsersTable() { //Очистка таблицы
        String str = "TRUNCATE TABLE User";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
