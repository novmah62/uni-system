package com.drewsec.user_central.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 6, max = 40, message = "Invalid password: Must be of 6 - 40 characters")
    private String password;

    @Size(max = 50, message = "Invalid email: Must be max 50 characters")
    @Email(message = "Invalid email")
    private String email;

}