package lk.ijse.userservice.repo;

import lk.ijse.userservice.entity.User;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

    int deleteByEmail(String userName);

    @Query("SELECT u from User  u where u.username=:username")
    Optional<User> findByUsername(@Param("username") String username);
}