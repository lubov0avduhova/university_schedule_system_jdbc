package org.example.jdbcuniversity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Course {
    private Long id;
    private String title;
    private String teacher;

    public Course(String title, String teacher) {
        this.title = title;
        this.teacher = teacher;
    }
}
