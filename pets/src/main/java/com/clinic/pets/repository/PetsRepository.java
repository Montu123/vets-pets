package com.clinic.pets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinic.pets.models.Pets;

import jakarta.transaction.Transactional;
@Repository
@Transactional
public interface PetsRepository extends JpaRepository<Pets, Long>{
	 List<Pets> findByUsername(String username);
	 
	 
	 @Modifying
	    @Query("DELETE FROM Pets u WHERE u.username = :username AND u.petname = :petname")
	    void deleteByUsernameAndPetname(@Param("username") String username, @Param("petname") String petname);
	 
//	    @Query("SELECT FROM  distinct e.petType from pets e")
//	 List<Pets> petCategory();

}
