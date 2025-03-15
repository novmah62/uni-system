package com.drewsec.user_central.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

	private Long id;

	private String username;

	private String email;

	private String type = "Bearer";

	private String token;


}