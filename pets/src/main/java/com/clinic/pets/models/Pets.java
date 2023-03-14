package com.clinic.pets.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pets {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String petname;

	@NotBlank
	@Size(max = 50)
	private String petType;

	@NotBlank
	@Size(max = 120)
	private String moreInfo;

	@NotBlank
	@Size(max = 50)
	private String username;

	public Pets(String petname, String petType, String moreInfo, String username) {
		this.petname = petname;
		this.petType = petType;
		this.moreInfo = moreInfo;
		this.username = username;
	}

}
