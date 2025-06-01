package org.example.jdbcuniversity.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    @NotNull(message = "Id can not be null")
    private Long id;

    @NotNull(message = "Name can not be null")
    @NotBlank
    private String name;

    @NotNull(message = "Email can not be null")
    @NotBlank
    private String email;

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
