package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySid(String uuid);

    Optional<User> findByUsername(String username);
}
