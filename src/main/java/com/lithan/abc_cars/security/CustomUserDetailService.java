package com.lithan.abc_cars.security;

import com.lithan.abc_cars.models.Role;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("Login UserName is "+username);

        UserEntity user = userRepository.findFirstByUsername(username);
        if (user != null){
            User authUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
            String[] roleNames= user.getRoles().stream().map(Role::getName).toArray(String[]::new);

            System.out.println("Role Name is "+ Arrays.toString(roleNames));


            return authUser;
        }else{
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
