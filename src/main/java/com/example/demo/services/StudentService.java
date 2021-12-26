package com.example.demo.services;

import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {


    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Optional<StudentEntity> findById(final Long id) {
        return studentRepository.findById(id);
    }

    public Page<StudentEntity> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public StudentEntity saveOrUpdate(StudentEntity studentEntity) {
        return studentRepository.save(studentEntity);
    }
}

