package org.example.jdbcuniversity;

import org.example.jdbcuniversity.dao.CourseDao;
import org.example.jdbcuniversity.dao.EnrollmentDao;
import org.example.jdbcuniversity.dao.StudentDao;
import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();
        EnrollmentDao enrollmentDao = new EnrollmentDao();

        log.info("Запуск University JDBC System");

        try {
            demo(studentDao, courseDao, enrollmentDao);
        } catch (Exception e) {
            log.error("Ошибка при выполнении программы", e);
        }

        log.info("Программа завершена");
    }


    private static void demo(StudentDao studentDao, CourseDao courseDao, EnrollmentDao enrollmentDao) {
        printSection("Все студенты", studentDao.findAll());

        Student bob = studentDao.findByName("Bob").orElseThrow();
        printSection("Курсы студента Bob", enrollmentDao.findCoursesByStudentId(bob.getId()));

        Course math = courseDao.findByTitle("Math").orElseThrow();
        printSection("Студенты на курсе Math", enrollmentDao.findStudentsByCourseId(math.getId()));

        printSection("Поиск студентов по имени 'Al'", enrollmentDao.findStudentsByNamePart("Al"));

        boolean enrolled = enrollmentDao.isStudentEnrolled(bob.getId(), math.getId());
        log.info("Bob записан на Math? {}", enrolled ? "Да" : "Нет");

        Course physics = courseDao.findByTitle("Physics").orElseThrow();
        long count = enrollmentDao.countStudentsInCourse(physics.getId());
        log.info("Кол-во студентов на Physics: {}", count);
    }

    private static <T> void printSection(String title, List<T> items) {
        log.info("\n {}:", title);
        items.forEach(item -> log.info("   → {}", item));
    }
}
