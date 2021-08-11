package com.codereggs.superHeroeTeam.services.impl;

import com.codereggs.superHeroeTeam.models.User;
import com.codereggs.superHeroeTeam.repositories.UserRepository;
import com.codereggs.superHeroeTeam.services.UserService;
import com.codereggs.superHeroeTeam.services.exceptions.NotValidEmailFormat;
import com.codereggs.superHeroeTeam.services.exceptions.UserAlreadyExistsException;
import com.codereggs.superHeroeTeam.services.exceptions.UserNotFoundException;
import com.codereggs.superHeroeTeam.util.EmailChecker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    // <---- Error Messages ---->

    private final static String NOT_VALID_EMAIL_FORMAT = "The email doesn't a valid email format.";

    //Uses String.format.
    private final static String USER_WITH_EMAIL_ALREADY_EXISTS = "User with email %s already exist.";

    private final static String USER_WITH_ID_NOT_FOUND = "User with id: %s not found.";

    private final static String USER_WITH_EMAIL_NOT_FOUND = "User with email: %s not found.";

    // <---- Create Method ---->

    @Override
    public User createUser(User user) throws UserAlreadyExistsException, NotValidEmailFormat{
        if(!isValidEmail(user.getEmail())){
            throw new NotValidEmailFormat(NOT_VALID_EMAIL_FORMAT);
        }
        if(userExistsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(String.format(USER_WITH_EMAIL_ALREADY_EXISTS,user.getEmail()));
        }
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setCreatedAt(System.currentTimeMillis() / 1000);
        user.setUpdatedAt(user.getCreatedAt());
        User result = userRepository.insert(user);
        return result;
    }

    // <---- Read Methods ---->

    @Override
    public User findById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_ID_NOT_FOUND,id)));
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND,email)));
    }

    @Override
    public ArrayList<User> findAll() {
        return (ArrayList<User>) userRepository.findAll();
    }

    // <---- Update Method ---->

    @SneakyThrows
    public User updateUser(String id, User user) {
        var userToUpdate = findById(id);
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userToUpdate.setPassword(bcryptEncoder.encode(user.getPassword()));
        }
        if (user.getPhoto() != null && !user.getPhoto().isBlank()) {
            userToUpdate.setPhoto(user.getPhoto());
        }
        if (user.getRole() != null) {
            userToUpdate.setRole(user.getRole());
        }
        // Se agrega la ultima vez que fue actualizado.
        userToUpdate.setUpdatedAt(System.currentTimeMillis() / 1000);
        return userRepository.save(userToUpdate);
    }

    // <---- Delete Method ---->

    @Override
    public Boolean deleteUser(String id) {
        try {
            var userToDelete = findById(id);
            userRepository.deleteById(userToDelete.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // <---- Aux Methods. ---->

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isValidEmail(String email){
        return EmailChecker.isValid(email);
    }

}
