package com.drewsec.user_central.controller;

import com.drewsec.user_central.definitions.constants.ApiMessageConstants;
import com.drewsec.user_central.dto.UserDto;
import com.drewsec.user_central.dto.response.ApiResponse;
import com.drewsec.user_central.dto.response.UserResponse;
import com.drewsec.user_central.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "user", description = "User Endpoints")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse<String>> getCurrentUser() {
		return ResponseEntity.ok(new ApiResponse<>(200,
						ApiMessageConstants.USER_FOUND,
						userService.getUserAuthenticationName()));
	}

	@GetMapping("/except-self")
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsersExceptSelf() {
		return ResponseEntity.ok(new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.finAllUsersExceptSelf()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable String id) {
		return ResponseEntity.ok(new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.getUserById(id)));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
		return ResponseEntity.ok(new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.getAllUsers()));
	}

}
