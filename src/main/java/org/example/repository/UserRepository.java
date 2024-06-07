package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//в качестве аргумента принимает информацию о нашем id и конкретном пользователе
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userEmail);

}
