package com.example.demo.modules;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentModel extends RepresentationModel<StudentModel> {


    private Long id;

    @NotEmpty(message = "name is required")
    @NotBlank
    @NotNull
    private String name;
}
