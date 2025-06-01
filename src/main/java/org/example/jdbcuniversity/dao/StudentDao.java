package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Student;
import org.example.jdbcuniversity.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StudentDao {
    private static final Logger log = LoggerFactory.getLogger(StudentDao.class);

    public boolean save(Student student) {
        String sql = "INSERT INTO student(name, email) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        student.setId(keys.getLong(1));
                    }
                }
                return true;
            } else {
                log.warn("Вставка студента не повлияла ни на одну строку");
                return false;
            }
        } catch (SQLException e) {
            log.error("Ошибка при сохранении студента: {}", student, e);
            return false;
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT * FROM student";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении списка студентов", e);
        }
        return students;
    }

    public Optional<Student> findByName(String name) {
        String sql = "SELECT * FROM student WHERE name = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске студента по имени: {}", name, e);
        }
        return Optional.empty();
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        return student;
    }

}
