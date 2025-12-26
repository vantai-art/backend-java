package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.PasswordResetToken;
import com.ngovantai.example901.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user); // Xóa token cũ của user
}