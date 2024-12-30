package com.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	
	private int id;
	
	@NotEmpty
	@Size(min=4,message="username must be min of 4 characters ")
	private String name;
	
	@Email(message="email must be valid ")
	private String email;
	
	@NotEmpty
	@Size(min=3,max=10,message="password must be min of 3 and max of 10")
	private String password;
	
	@NotEmpty
	private String about;

}
