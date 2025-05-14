package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.model.Student;
import org.example.jdbcuniversity.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    public boolean enrollStudent(long studentId, long courseId) {
        String sql = "INSERT INTO enrollment(student_id, course_id) VALUES (?, ?)";

        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            statement.setLong(1, studentId);
            statement.setLong(2, courseId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Вставка в таблицу enrollment не произошла");
        }

    }

    public List<Student> findStudentsByCourseId(long courseId) {
        String sql = "SELECT s.id, s.name, s.email " +
                "FROM student s " +
                "JOIN enrollment e ON s.id = e.student_id " +
                "WHERE e.course_id = ? ";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            statement.setLong(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            List<Student> result = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));

                result.add(student);
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблиц student и enrollment");
        }
    }


    public List<Course> findCoursesByStudentId(long studentId) {
        String sql = "SELECT c.id, c.title, c.teacher " +
                "FROM course c " +
                "JOIN enrollment e ON c.id = e.course_id " +
                "WHERE e.student_id = ?";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            statement.setLong(1, studentId);
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
            throw new RuntimeException("Произошла ошибка при выборке из таблиц student и enrollment");
        }
    }

    public boolean removeStudentFromCourse(long studentId, long courseId) {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            statement.setLong(1, studentId);
            statement.setLong(2, courseId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при удалении из таблицы enrollment");
        }
    }

    public void registerStudentWithEnrollment(Student student, long courseId) {

        String insertStudentSql = "INSERT INTO student (name, email) VALUES (?, ?)";
        String enrollSql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";

        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement studentStmt = connection.prepareStatement(insertStudentSql, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement enrollStmt = connection.prepareStatement(enrollSql)
            ) {
                // Вставка студента
                studentStmt.setString(1, student.getName());
                studentStmt.setString(2, student.getEmail());
                studentStmt.executeUpdate();

                ResultSet keys = studentStmt.getGeneratedKeys();
                if (keys.next()) {
                    long studentId = keys.getLong(1);

                    // Вставка в enrollment
                    enrollStmt.setLong(1, studentId);
                    enrollStmt.setLong(2, courseId);
                    enrollStmt.executeUpdate();

                    connection.commit();
                    System.out.println("Транзакция завершена успешно");
                } else {
                    throw new SQLException("ID студента не получен");
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Откат транзакции: " + e.getMessage());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при регистрации студента", e);
        }
    }

    public boolean isStudentEnrolled(long studentId, long courseId) {
        String sql = "SELECT 1 FROM enrollment WHERE student_id = ? AND course_id = ? LIMIT 1";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            statement.setLong(1, studentId);
            statement.setLong(2, courseId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблицы enrollment");
        }
    }


    public List<Student> findStudentsByNamePart(String namePart) {
        String sql = "SELECT * FROM student WHERE name LIKE ?";
        List<Student> result = new ArrayList<>();
        try (PreparedStatement statement = DatabaseUtil.getConnection()
                .prepareStatement(sql)) {
            statement.setString(1, "%" + namePart + "%");

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setName(resultSet.getString("name"));
                student.setEmail(resultSet.getString("email"));
                result.add(student);
            }

            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблицы student");
        }

        return result;

    }

    public int countStudentsInCourse(long courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id = ?";
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(sql)) {

            statement.setLong(1, courseId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Произошла ошибка при выборке из таблицы enrollment");
        }
    }
}
