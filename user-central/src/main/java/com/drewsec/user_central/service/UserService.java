package com.drewsec.user_central.service;

import com.drewsec.user_central.payload.request.LoginRequest;
import com.drewsec.user_central.payload.request.SignupRequest;
import com.drewsec.user_central.payload.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signUpRequest);

    ResponseEntity<?> deleteUser(Long userId);

    ResponseEntity<?> updateUser(Long userId, UpdateUserRequest updateUserRequest);

}
