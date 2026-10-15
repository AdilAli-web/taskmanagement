package com.example.taskmanagement.SecurityConfig;

import com.example.taskmanagement.Entity.AppUser;
import com.example.taskmanagement.Repo.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsFetch implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public UserDetailsFetch(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user=appUserRepository.findByEmailIgnoreCase(email.toLowerCase())
                .orElseThrow(()-> new UsernameNotFoundException(email));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();



    }
}
