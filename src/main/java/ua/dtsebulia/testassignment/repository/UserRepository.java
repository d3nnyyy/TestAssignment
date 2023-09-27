package ua.dtsebulia.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.dtsebulia.testassignment.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
