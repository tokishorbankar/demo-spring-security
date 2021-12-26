package com.example.demo.controller;

import com.example.demo.assemblers.StudentModelAssembler;
import com.example.demo.entities.StudentEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modules.StudentModel;
import com.example.demo.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/managements/api")
@Slf4j
public class StudentManagementRestController {

    private final StudentModelAssembler<StudentManagementRestController> studentModelAssembler;

    private final StudentService studentService;

    private final PagedResourcesAssembler<StudentEntity> pagedResourcesAssembler;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    public StudentManagementRestController(StudentModelAssembler<StudentManagementRestController> studentModelAssembler, StudentService studentService, PagedResourcesAssembler<StudentEntity> pagedResourcesAssembler) {
        this.studentModelAssembler = studentModelAssembler;
        this.studentService = studentService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id)
            throws BadRequestException, ResourceNotFoundException {

        log.info("StudentManagementRestController - delete() invoke");

        final StudentEntity results = studentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        studentService.deleteById(results.getId());

        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<StudentModel> create(final @Valid @RequestBody StudentModel studentModel)
            throws BadRequestException {

        log.info("StudentManagementRestController - create() invoke");

        final StudentEntity entity = studentService.saveOrUpdate(StudentEntity
                .builder()
                .name(studentModel.getName().trim())
                .id(studentModel.getId())
                .build());

        StudentModel results = StudentModel
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();

        final URI url = MvcUriComponentsBuilder
                .fromController(getClass())
                .path("/{id}")
                .buildAndExpand(results.getId())
                .toUri();

        return ResponseEntity.created(url).eTag(url.getPath()).body(results);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<StudentModel> update(@PathVariable(required = true, value = "id") final Long id,
                                               final @Valid @RequestBody StudentModel studentModel) throws BadRequestException, ResourceNotFoundException {

        log.info("StudentManagementRestController - update() invoke");

        final StudentEntity entity = studentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        entity.setName(studentModel.getName().trim());

        final StudentEntity saveOrUpdateEntity = studentService.saveOrUpdate(entity);

        StudentModel results = StudentModel
                .builder()
                .id(saveOrUpdateEntity.getId())
                .name(saveOrUpdateEntity.getName())
                .build();

        final URI url = MvcUriComponentsBuilder
                .fromController(getClass())
                .path("/{id}")
                .buildAndExpand(saveOrUpdateEntity.getId())
                .toUri();

        return ResponseEntity
                .ok()
                .location(url)
                .eTag(url.getPath())
                .body(results);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public ResponseEntity<StudentModel> one(@PathVariable("id") final Long id)
            throws BadRequestException, ResourceNotFoundException {

        log.info(String.format("StudentManagementRestController - one() invoke by id %s", id));

        return studentService.findById(id)
                .map(studentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<StudentModel>> all(
            @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)}) final Pageable pageable)
            throws BadRequestException {

        log.info("StudentManagementRestController - all() invoke by Pageable");

        final Page<StudentEntity> entities = studentService.findAll(pageable);

        final PagedModel<StudentModel> collModel = pagedResourcesAssembler.toModel(entities, studentModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

}
