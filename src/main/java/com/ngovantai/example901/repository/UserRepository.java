package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // ← THÊM import này

import java.util.Optional;

@Repository // ← THÊM annotation này
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}