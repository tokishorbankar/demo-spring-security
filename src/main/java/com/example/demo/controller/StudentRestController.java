package com.example.demo.controller;

import com.example.demo.assemblers.StudentModelAssembler;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modules.StudentModel;
import com.example.demo.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/students")
@Slf4j
public class StudentRestController {

    private final StudentModelAssembler<StudentRestController> studentModelAssembler;

    private final StudentService studentService;

    @Autowired
    public StudentRestController(StudentModelAssembler<StudentRestController> studentModelAssembler, StudentService studentService) {
        this.studentModelAssembler = studentModelAssembler;
        this.studentService = studentService;
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_ADMIN' OR 'ROLE_STUDENT')")
    public ResponseEntity<StudentModel> one(@PathVariable("id") final Long id) throws ResourceNotFoundException {

        log.info(String.format("StudentRestController - one() invoke by id %s", id));

        return studentService.findById(id).map(entity -> {

            StudentModel model = StudentModel.builder().id(entity.getId()).name(entity.getName()).build();

            model.add(linkTo(methodOn(StudentRestController.class).one(entity.getId())).withSelfRel());
            model.setId(entity.getId());
            model.setName(entity.getName());
            return model;

        }).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

}
