package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDao {

    private static final Logger log = LoggerFactory.getLogger(CourseDao.class);

    public boolean save(Course course) {
        String sql = "INSERT INTO course(title, teacher) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getTitle());
            statement.setString(2, course.getTeacher());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        course.setId(keys.getLong(1));
                    }
                }
                return true;
            } else {
                log.warn("Вставка курса не повлияла ни на одну строку");
                return false;
            }
        } catch (SQLException e) {
            log.error("Ошибка при сохранении курса: {}", course, e);
            return false;
        }
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                courses.add(mapCourse(rs));
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении списка курсов", e);
        }
        return courses;
    }

    public Optional<Course> findByTitle(String title) {
        String sql = "SELECT * FROM course WHERE title = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCourse(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске курса по названию: {}", title, e);
        }
        return Optional.empty();
    }

    private Course mapCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setTitle(rs.getString("title"));
        course.setTeacher(rs.getString("teacher"));
        return course;
    }
}