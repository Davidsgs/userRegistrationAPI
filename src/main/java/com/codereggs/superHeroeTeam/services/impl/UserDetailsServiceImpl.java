package com.codereggs.superHeroeTeam.services.impl;

import com.codereggs.superHeroeTeam.models.User;
import com.codereggs.superHeroeTeam.models.impl.UserDetailsImpl;
import com.codereggs.superHeroeTeam.repositories.UserRepository;
import com.codereggs.superHeroeTeam.services.UserService;
import com.codereggs.superHeroeTeam.services.exceptions.NotValidEmailFormat;
import com.codereggs.superHeroeTeam.services.exceptions.UserAlreadyExistsException;
import com.codereggs.superHeroeTeam.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return new UserDetailsImpl(userService.findByEmail(email));
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public User registerUser(User user) throws UserAlreadyExistsException, NotValidEmailFormat {
        return userService.createUser(user);
    }
}
