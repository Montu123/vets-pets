package com.clinic.pets.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class PetsRequest {
	private Long id;
	@NotBlank
	private String username;
	@NotBlank
	private String petname;
	@NotBlank
	private String petType;
	
	private String moreInfo;
	
}
