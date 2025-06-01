package org.example.jdbcuniversity.dao;

import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.model.Student;
import org.example.jdbcuniversity.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentDao.class);

    public void enrollStudent(long studentId, long courseId) {
        String sql = "INSERT INTO enrollment(student_id, course_id) VALUES (?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);
            statement.setLong(2, courseId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при записи студента {} на курс {}", studentId, courseId, e);
        }
    }

    public List<Course> findCoursesByStudentId(long studentId) {
        String sql = "SELECT c.* FROM course c " +
                "JOIN enrollment e ON c.id = e.course_id WHERE e.student_id = ?";
        List<Course> courses = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapCourse(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении курсов для студента {}", studentId, e);
        }
        return courses;
    }

    public List<Student> findStudentsByCourseId(long courseId) {
        String sql = "SELECT s.* FROM student s " +
                "JOIN enrollment e ON s.id = e.student_id WHERE e.course_id = ?";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, courseId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при получении студентов для курса {}", courseId, e);
        }
        return students;
    }

    public List<Student> findStudentsByNamePart(String namePart) {
        String sql = "SELECT * FROM student WHERE name LIKE ?";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + namePart + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске студентов по части имени: {}", namePart, e);
        }
        return students;
    }

    public boolean isStudentEnrolled(long studentId, long courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);
            statement.setLong(2, courseId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при проверке записи студента {} на курс {}", studentId, courseId, e);
        }
        return false;
    }

    public int countStudentsInCourse(long courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, courseId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при подсчёте студентов на курсе {}", courseId, e);
        }
        return 0;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        return student;
    }

    private Course mapCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setTitle(rs.getString("title"));
        course.setTeacher(rs.getString("teacher"));
        return course;
    }
}