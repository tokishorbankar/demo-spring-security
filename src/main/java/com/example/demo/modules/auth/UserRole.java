package com.example.demo.modules.auth;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.modules.auth.UserPermission.*;

@Getter
public enum UserRole {


    ADMIN(Stream.of(
            STUDENT_READ,
            STUDENT_WRITE,
            COURSE_READ,
            COURSE_WRITE)
            .collect(Collectors.toCollection(HashSet::new))),

    ADMINTRAINEE(Stream.of(
            STUDENT_READ,
            COURSE_READ)
            .collect(Collectors.toCollection(HashSet::new))),

    STUDENT(Collections.emptySet());


    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        Set<SimpleGrantedAuthority> collectPermissions = getSimpleGrantedAuthorities();

        collectPermissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return collectPermissions;

    }

    private Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        Set<SimpleGrantedAuthority> collectPermissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toCollection(HashSet::new));
        return collectPermissions;
    }
}
