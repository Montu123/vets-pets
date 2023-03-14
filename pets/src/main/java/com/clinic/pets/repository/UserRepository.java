package com.clinic.pets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.pets.models.User;

import jakarta.transaction.Transactional;



@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);
//  @Modifying
//  @Query("DELETE FROM USER_ROLES u WHERE u.id =?1")
//  void deleteUserById(@Param("id") Long id);
}
