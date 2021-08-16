package com.salat.briene.repositories;

import com.salat.briene.entities.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, String> {

    Optional<PasswordResetRequest> findById(String id);
}