package com.scm.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {

    @Size(min = 3, message = "minimum 3 character is require")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email address")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "password should be minimum 6 character")
    private String password;

    @Size(min = 3, max = 100)
    private String about;

    @Size(min = 10, max = 10, message = "invalid phone number")
    private String phoneNumber;
}
