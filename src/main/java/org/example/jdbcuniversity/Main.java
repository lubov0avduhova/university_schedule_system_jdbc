package org.example.jdbcuniversity;

import org.example.jdbcuniversity.dao.CourseDao;
import org.example.jdbcuniversity.dao.EnrollmentDao;
import org.example.jdbcuniversity.dao.StudentDao;
import org.example.jdbcuniversity.model.Course;
import org.example.jdbcuniversity.model.Student;

public class Main {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();
        EnrollmentDao enrollmentDao = new EnrollmentDao();

        // 1. Создаём студентов и курсы
        Student s1 = new Student("Alice", "alice@mail.com");
        Student s2 = new Student("Bob", "bob@mail.com");
        studentDao.save(s1);
        studentDao.save(s2);

        Course c1 = new Course("Math", "Dr. Smith");
        Course c2 = new Course("Physics", "Prof. Johnson");
        courseDao.save(c1);
        courseDao.save(c2);

        // 2. Записываем на курсы
        enrollmentDao.enrollStudent(s1.getId(), c1.getId());
        enrollmentDao.enrollStudent(s2.getId(), c1.getId());
        enrollmentDao.enrollStudent(s2.getId(), c2.getId());

        // 3. Демонстрация
        System.out.println("Все студенты:");
        studentDao.findAll().forEach(System.out::println);

        System.out.println("\nКурсы студента Bob:");
        enrollmentDao.findCoursesByStudentId(s2.getId()).forEach(System.out::println);

        System.out.println("\nСтуденты на курсе Math:");
        enrollmentDao.findStudentsByCourseId(c1.getId()).forEach(System.out::println);

        System.out.println("\nПоиск студентов по имени 'Al':");
        enrollmentDao.findStudentsByNamePart("Al").forEach(System.out::println);

        System.out.println("\nПроверка: Bob записан на Math?");
        System.out.println(enrollmentDao.isStudentEnrolled(s2.getId(), c1.getId()));

        System.out.println("\nСколько студентов на курсе Physics:");
        System.out.println(enrollmentDao.countStudentsInCourse(c2.getId()));
    }
}