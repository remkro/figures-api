package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<Role> getRolesByUsername(String username);
}
