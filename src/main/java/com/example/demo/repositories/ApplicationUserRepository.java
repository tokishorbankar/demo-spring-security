package com.example.demo.repositories;

import com.example.demo.entities.ApplicationUserEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ApplicationUserRepository {

    public Optional<ApplicationUserEntity> findByUsername(String username);
}
