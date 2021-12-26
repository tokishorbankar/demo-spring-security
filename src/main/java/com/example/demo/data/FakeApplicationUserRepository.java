package com.example.demo.data;

import com.example.demo.entities.ApplicationUserEntity;
import com.example.demo.repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.modules.auth.UserRole.*;

@Repository
public class FakeApplicationUserRepository implements ApplicationUserRepository {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUserEntity> findByUsername(String username) {
        return getApplicationUserEntities()
                .stream()
                .filter(applicationUserEntity -> username.equalsIgnoreCase(applicationUserEntity.getUsername()))
                .findFirst();
    }

    private List<ApplicationUserEntity> getApplicationUserEntities() {
        return Stream.of(
                ApplicationUserEntity.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("kishorbankar")
                        .authorities(ADMIN.getGrantedAuthorities())
                        .accountNonExpired(Boolean.TRUE)
                        .accountNonLocked(Boolean.TRUE)
                        .credentialsNonExpired(Boolean.TRUE)
                        .enabled(Boolean.TRUE)
                        .build(),
                ApplicationUserEntity.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("kishor")
                        .authorities(ADMINTRAINEE.getGrantedAuthorities())
                        .accountNonExpired(Boolean.TRUE)
                        .accountNonLocked(Boolean.TRUE)
                        .credentialsNonExpired(Boolean.TRUE)
                        .enabled(Boolean.TRUE)
                        .build(),
                ApplicationUserEntity.builder()
                        .password(passwordEncoder.encode("password"))
                        .username("bankar")
                        .authorities(STUDENT.getGrantedAuthorities())
                        .accountNonExpired(Boolean.TRUE)
                        .accountNonLocked(Boolean.TRUE)
                        .credentialsNonExpired(Boolean.TRUE)
                        .enabled(Boolean.TRUE)
                        .build()

        ).collect(Collectors.toCollection(ArrayList::new));
    }
}
