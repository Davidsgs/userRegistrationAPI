package com.codereggs.superHeroeTeam.services;

import com.codereggs.superHeroeTeam.models.User;
import com.codereggs.superHeroeTeam.services.exceptions.NotValidEmailFormat;
import com.codereggs.superHeroeTeam.services.exceptions.UserAlreadyExistsException;
import com.codereggs.superHeroeTeam.services.exceptions.UserNotFoundException;

import java.util.ArrayList;

public interface UserService {

    public User createUser(User user) throws UserAlreadyExistsException, NotValidEmailFormat;

    public User findById(String id) throws UserNotFoundException;

    public ArrayList<User> findAll();

    public User findByEmail(String email) throws UserNotFoundException;

    public User updateUser(String id, User user);

    public Boolean deleteUser(String id);

    public boolean userExistsByEmail(String email);

}
