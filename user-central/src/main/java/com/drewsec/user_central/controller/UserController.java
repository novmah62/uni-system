package com.drewsec.user_central.controller;

import com.drewsec.user_central.definitions.constants.ApiMessageConstants;
import com.drewsec.user_central.dto.UserDto;
import com.drewsec.user_central.dto.response.ApiResponse;
import com.drewsec.user_central.dto.response.UserResponse;
import com.drewsec.user_central.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "user", description = "User Endpoints")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<String> getCurrentUser() {
		return new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.getUserAuthenticationName());
	}

	@GetMapping("/except-self")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<UserResponse>> getAllUsersExceptSelf() {
		return new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.finAllUsersExceptSelf());
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<UserDto> getUserById(@PathVariable String id) {
		return new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.getUserById(id));
	}

	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<UserDto>> getAllUsers() {
		return new ApiResponse<>(200,
				ApiMessageConstants.USER_FOUND,
				userService.getAllUsers());
	}

}
