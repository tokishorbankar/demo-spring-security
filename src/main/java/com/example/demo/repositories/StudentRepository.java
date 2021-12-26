package com.example.demo.repositories;

import com.example.demo.entities.StudentEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<StudentEntity, Long> {


}
