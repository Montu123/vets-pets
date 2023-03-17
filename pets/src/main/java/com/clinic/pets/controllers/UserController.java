/**
 * 
 */
package com.clinic.pets.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
			Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
			return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}

/***********Admin can fetch all the users **********/	  
	 @GetMapping("/all/{role}")
	  public ResponseEntity<?> getAllUsers(@PathVariable String role){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  if(role.equalsIgnoreCase("ADMIN")) {
		List<User> allUsers=userRepository.findAll();
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUsers);
		  }
		  return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
			        .body(new MessageResponse("You are not an Admin!"));
		  
	  }
	 /***********User can add pets **********/	
	  @PostMapping("/add-pet")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody PetsRequest petPayloads) {
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//		  for(PetsRequest petsPay:petPayloads) {
		  Pets pets;
	 	  if(petPayloads.getId()==0||petPayloads.getId()==null) {
	 		 pets=new Pets(petPayloads.getPetname(),petPayloads.getPetType(),petPayloads.getMoreInfo(), petPayloads.getUsername());    
	 	  }
	 	  else {
	 		  Optional<Pets> updatPet=petsRepository.findById(petPayloads.getId());
	 		  pets=updatPet.get();
	 		 System.out.println(petPayloads.getId()+" Pets "+pets.toString());
	 		  pets.setId(petPayloads.getId());
	 		  pets.setPetname(petPayloads.getPetname());
	 		  pets.setPetType(petPayloads.getPetType());
	 		  pets.setMoreInfo(petPayloads.getMoreInfo());
	 	 //  pets=new Pets(petPayloads.getId(), petPayloads.getPetname(),petPayloads.getPetType(),petPayloads.getMoreInfo(), petPayloads.getUsername());  
	 	  }
	 	   pets.setUsername(petPayloads.getUsername());
	 	   
	       petsRepository.save(pets);
	  // }
	    return ResponseEntity.ok(new MessageResponse("Pets info saved successfully!"));
	  }
	  /**********users can fetch all their pets **********/	
	  @GetMapping("/all-pets/{username}")
	  public ResponseEntity<?> getAllUsersPets(@PathVariable String username){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		 List<Pets> allUserspets=petsRepository.findByUsername(username);
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUserspets);
		  }
	  /***********user can delete their pets **********/	
	  @GetMapping("/pet-delete")
	  public ResponseEntity<?> deleteUserPet(@RequestBody Pets pet){
		  petsRepository.delete(pet);
		   return ResponseEntity.ok(new MessageResponse("Pets info updated!"));
		  }

	  /***********Admin fetching all users **********/	  
	  @GetMapping("/all-pets-admin/{userrole}")
	  public ResponseEntity<?> getAllsPets(@PathVariable String userrole){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  if(userrole.equalsIgnoreCase("ADMIN")) {
		 List<Pets> allUserspets=petsRepository.findAll();
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(allUserspets);
		  }
		  return ResponseEntity.ok(new MessageResponse("you are not an admin"));
		  }
	  /***********Admin pet category **********/	  
	  @GetMapping("/all-pets-types")
	  public ResponseEntity<?> getAllsPetsCategory(){
		  ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		  
		 List<Pets> allUserspets=petsRepository.findAll();
			List<Pets> petCategory = allUserspets.stream().filter(distinctByKey(type -> type.getPetType()))
					.collect(Collectors.toList());
		 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(petCategory);
		  }
		  
	  
}
