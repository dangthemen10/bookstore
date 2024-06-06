package com.phdang97.bookstore.repository;

import com.phdang97.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserById(Integer id);

  Optional<User> findUserByToken(String token);
}
