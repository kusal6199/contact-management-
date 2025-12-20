package com.scm.scm.forms;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.scm.scm.entities.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @Size(min = 3, message = "minimum 3 character is require")
    private String name;
    @Email(message = "invalid email address")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^(97|98)[0-9]{8}$", message = "invalid phone number")
    private String phoneNumber;

    @Size(min = 3, message = "address must be atleast 3 character")
    private String address;

    private String description;
    @URL(message = "invalid URL formate ")
    private String websiteLink;
    @URL(message = "invalid URL formate ")
    private String linkedInLink;

    private boolean favorite;

    // @ValidFile(message = "invalid file")

    private MultipartFile contactImage;



}
