package com.example.demo.assemblers;

import com.example.demo.entities.StudentEntity;
import com.example.demo.modules.StudentModel;
import com.example.demo.controller.StudentManagementRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler<T> extends RepresentationModelAssemblerSupport<StudentEntity, StudentModel> {


    public StudentModelAssembler() {

        super(StudentManagementRestController.class, StudentModel.class);

    }


    /**
     * Converts the given entity into a {@code D}, which extends {@link RepresentationModel}.
     *
     * @param entity
     * @return
     */
    @Override
    public StudentModel toModel(StudentEntity entity) {
        StudentModel model = instantiateModel(entity);

        model.add(linkTo(methodOn(StudentManagementRestController.class).one(entity.getId())).withSelfRel());
        model.setId(entity.getId());
        model.setName(entity.getName());

        return model;
    }


    @Override
    public CollectionModel<StudentModel> toCollectionModel(Iterable<? extends StudentEntity> entities) {
        CollectionModel<StudentModel> models = super.toCollectionModel(entities);

        models.add(linkTo(methodOn(StudentManagementRestController.class).all(null)).withSelfRel());

        return models;
    }
}
