/**
 * 
 */
package com.clinic.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.pets.models.Pets;
import com.clinic.pets.models.User;
import com.clinic.pets.payload.request.PetsRequest;
import com.clinic.pets.payload.response.MessageResponse;
import com.clinic.pets.repository.PetsRepository;
import com.clinic.pets.repository.RoleRepository;
import com.clinic.pets.repository.UserRepository;
import com.clinic.pets.security.jwt.JwtUtils;

import jakarta.validation.Valid;

/**
 * @author sandeepsingh
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	  @Autowired
	  AuthenticationManager authenticationManager;

	  @Autowired
	  UserRepository userRepository;

	  @Autowired
	  RoleRepository roleRepository;

	  @Autowired
	  PasswordEncoder encoder;
	  @Autowired
	  private PetsRepository petsRepository;

	  @Autowired
	  JwtUtils jwtUtils;
	 @GetMapping("/all/{role}")
	  public ResponseEntity<?> getAllUsers(@PathVariable("role") String role){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  if(role.equalsIgnoreCase("ADMIN")) {
		List<User> allUsers=userRepository.findAll();
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUsers);
		  }
		  return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
			        .body(new MessageResponse("You are not an Admin!"));
		  
	  }
	  
	  @PostMapping("/add-pets")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody List<PetsRequest> petPayloads) {
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  for(PetsRequest petsPay:petPayloads) {
	 	   Pets pets=new Pets(petsPay.getPetname(),petsPay.getPetType(),petsPay.getMoreInfo(), petsPay.getUsername());
	 	    pets.setUsername(petsPay.getUsername());
	       petsRepository.save(pets);
	   }
	    return ResponseEntity.ok(new MessageResponse("Pets info saved successfully!"));
	  }
	  @GetMapping("/all-pets/{username}")
	  public ResponseEntity<?> getAllUsersPets(@PathVariable String username){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		 List<Pets> allUserspets=petsRepository.findByUsername(username);
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUserspets);
		  }
	  @GetMapping("/pet-delete")
	  public ResponseEntity<?> deleteUserPet(@RequestBody Pets pet){
		  petsRepository.delete(pet);
		   return ResponseEntity.ok(new MessageResponse("Pets info updated!"));
		  }

	  @PutMapping("/edit-pet")
	  public ResponseEntity<String> updateData(@RequestBody PetsRequest petsPay) {
		  Pets pets=new Pets(petsPay.getPetname(),petsPay.getPetType(),petsPay.getMoreInfo(), petsPay.getUsername());
		  pets.setId(petsPay.getId());
		  petsRepository.save(pets);
	      return ResponseEntity.ok("Data updated successfully");
	  }
	  
	  @GetMapping("/all-pets-admin/{userrole}")
	  public ResponseEntity<?> getAllsPets(@PathVariable String userrole){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  if(userrole.equalsIgnoreCase("ADMIN")) {
		 List<Pets> allUserspets=petsRepository.findAll();
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUserspets);
		  }
		  return ResponseEntity.ok(new MessageResponse("you are not an admin"));
		  }
}
