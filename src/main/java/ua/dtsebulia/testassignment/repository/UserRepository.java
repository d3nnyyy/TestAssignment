package ua.dtsebulia.testassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.dtsebulia.testassignment.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.dateOfBirth " +
            "BETWEEN :fromDate AND :toDate")
    List<User> findUsersByBirthdayRange(
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );
}
