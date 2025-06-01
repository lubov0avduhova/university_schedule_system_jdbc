# University JDBC System

Консольное Java-приложение для управления университетом: студенты, курсы, записи на курсы.  
Полностью реализовано на **чистом JDBC**, с использованием **Liquibase**, **PostgreSQL**, **SLF4J + Logback** и **Docker Compose**.

---

## Цель проекта

Показать глубокое понимание работы с базой данных **без ORM и Spring**:
- `Connection`, `PreparedStatement`, `ResultSet`
- SQL-запросы (`JOIN`, `LIKE`, `COUNT`, `EXISTS`)
- Управление транзакциями вручную (`commit/rollback`)
- DAO-паттерн, `Optional`, маппинг `ResultSet`
- Liquibase-механизм миграций
- Структурированное логирование операций

---

## Технологии

- Java 17
- PostgreSQL (через Docker Compose)
- Liquibase (XML + rollback, preConditions, context)
- JDBC
- SLF4J + Logback
- Maven
- Docker / Docker Compose
- Adminer (web-интерфейс для базы)

---

## Функционал

- [x] Добавление/получение студентов и курсов
- [x] Many-to-Many: запись студента на курс
- [x] Поиск студентов по части имени (`LIKE`)
- [x] Получение студентов по курсу (`JOIN`)
- [x] Получение курсов по студенту (`JOIN`)
- [x] Подсчёт количества студентов на курсе (`COUNT`)
- [x] Проверка: записан ли студент (`EXISTS`)
- [x] Удаление студента из курса
- [x] Транзакция: добавление студента + запись на курс
- [x] Полноценные миграции через Liquibase
- [x] Откат миграций (rollback)
- [x] Контексты dev/prod
- [x] Логгирование всех операций через SLF4J
- [x] Поддержка Docker Compose + Adminer

---

## Структура проекта

```text
src/main/java/org/example/
├── dao/       → StudentDao, CourseDao, EnrollmentDao
├── model/     → Student, Course
├── util/      → DatabaseUtil
├── LiquibaseInitializer.java
└── Main.java  → Демонстрация всех операций
```

Миграции:
```text
src/main/resources/db/changelog/
├── db.changelog-master.xml
├── student/student_1_create_table.xml
├── course/course_1_create_table.xml
├── enrollment/enrollment_1_create_table.xml
```

Docker:
```text
docker-compose.yml  → PostgreSQL + Adminer
```

---

## Liquibase

- Используется `includeAll` + разбиение по сущностям
- `rollback` прописан для всех changeSet'ов
- Отдельные миграции для `dev`-окружения (через `context`)
- `preConditions` с `onFail="MARK_RAN"` для защиты от ошибок

---

## Как запустить

### 1. Запусти PostgreSQL и Adminer через Docker:
```bash
docker-compose up -d
```

Adminer будет доступен на [http://localhost:8081](http://localhost:8081)

### 2. Применить миграции Liquibase:
```bash
mvn liquibase:update
```

### 3. Собери и запусти проект:
```bash
mvn compile
```

Запусти `Main.java` — в консоли будет демонстрация.

---