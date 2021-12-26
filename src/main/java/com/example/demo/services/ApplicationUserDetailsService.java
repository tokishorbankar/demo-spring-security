package com.example.demo.services;

import com.example.demo.data.FakeApplicationUserRepository;
import com.example.demo.repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private static final String EXCEPTION_MESSAGE = "Resource not found with User Name : %s";

    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(EXCEPTION_MESSAGE, username)));
    }
}
