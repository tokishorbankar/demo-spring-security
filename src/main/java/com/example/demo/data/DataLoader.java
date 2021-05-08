package com.example.demo.data;

import com.example.demo.entities.StudentEntity;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDataLoader(@Autowired StudentRepository studentRepository) {

        return args -> {

            List<StudentEntity> studentEntities = Stream.of(
                    StudentEntity.builder().name("Bilbo".trim()).build(),
                    StudentEntity.builder().name("Baggins".trim()).build(),
                    StudentEntity.builder().name("Frodo".trim()).build(),
                    StudentEntity.builder().name("thief".trim()).build()
            ).collect(Collectors.toList());

            studentRepository.saveAll(studentEntities);
        };
    }
}
