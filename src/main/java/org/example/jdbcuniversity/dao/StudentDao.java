package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Student;
import org.example.jdbcuniversity.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StudentDao {

    public void save(Student student) {
        String sql = "INSERT INTO student(name, email) " +
                "VALUES (?, ?)";
        try (
                PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = statement.getGeneratedKeys()
        ) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());

            statement.executeUpdate();

            if (keys.next()) {
                student.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Вставка в таблицу student не произошла");
        }
    }

    public List<Student> findAll() {
        List<Student> result = new ArrayList<>();
        String sql = "SELECT * FROM student";

        try (PreparedStatement statement = DatabaseUtil.getConnection()
                .prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                result.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблицы student");
        }

        return result;
    }
}
