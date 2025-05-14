package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public boolean save(Course course) {
        String sql = "INSERT INTO course (title, teacher)" +
                "VALUES (?, ?)";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getTitle());
            statement.setString(2, course.getTeacher());

            int rows = statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (rows > 0) {
                course.setId(resultSet.getLong(1));
                return true;
            } else return false;

        } catch (SQLException e) {
            throw new RuntimeException("Вставка в таблицу course не произошла");
        }
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM course";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            List<Course> result = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("id"));
                course.setTitle(resultSet.getString("title"));
                course.setTeacher(resultSet.getString("teacher"));

                result.add(course);
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблицы course");
        }

    }

}
