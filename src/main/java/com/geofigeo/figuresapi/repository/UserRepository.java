package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    Optional<User> findByUsernameFetchRoles(String username);
    Optional<User> findByUsername(String username);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByUsername(String username);
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    boolean existsWithLockingByEmail(String email);
}
