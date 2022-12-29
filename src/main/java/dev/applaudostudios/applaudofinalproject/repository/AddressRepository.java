package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.entity.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT u FROM Address u WHERE u.isDefault = true AND u.user.sid = ?1")
    Optional<Address> findAddress(String sid);

    List<Address> findAllByUserSid(String sid);

    Optional<Address> findByIdAndUserSid(Long id, String sid);

    List<Address> findAllByUserSid(String sid, Pageable pageable);
}
