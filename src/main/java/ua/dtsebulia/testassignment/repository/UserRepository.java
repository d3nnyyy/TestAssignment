package ua.dtsebulia.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dtsebulia.testassignment.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
