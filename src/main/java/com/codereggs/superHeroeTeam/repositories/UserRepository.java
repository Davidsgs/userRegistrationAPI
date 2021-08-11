package com.codereggs.superHeroeTeam.repositories;

import com.codereggs.superHeroeTeam.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
