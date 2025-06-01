package org.example.jdbcuniversity.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course {
    @NotNull
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String teacher;

    public Course(String title, String teacher) {
        this.title = title;
        this.teacher = teacher;
    }
}
