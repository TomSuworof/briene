package com.salat.briene.repositories;

import com.salat.briene.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(@NotNull Long UUID);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
