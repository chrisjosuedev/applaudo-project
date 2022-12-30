package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByStatusIsTrue();

    List<User> findAllByStatusIsTrue(Pageable pageable);

    Optional<User> findBySid(String uuid);

    Optional<User> findByUsername(String username);
}
