package com.drewsec.user_central.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "Invalid Username: Empty username")
	private String username;

	@NotBlank(message = "Invalid Password: Empty password")
	private String password;

}