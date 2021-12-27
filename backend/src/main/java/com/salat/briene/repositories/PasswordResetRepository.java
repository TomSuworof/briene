package com.salat.briene.repositories;

import com.salat.briene.entities.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, UUID> {

    Optional<PasswordResetRequest> findById(UUID id);

    Optional<PasswordResetRequest> findByUsername(String username);
}