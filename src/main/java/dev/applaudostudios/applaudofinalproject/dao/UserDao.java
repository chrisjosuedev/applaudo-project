package dev.applaudostudios.applaudofinalproject.dao;

import dev.applaudostudios.applaudofinalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findBySid(String uuid);
}
