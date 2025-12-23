package com.scm.scm.helper;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

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

    @Size(min = 3, message = "minimum 3 character is require", groups = CreateContact.class)
    private String name;
    @Email(message = "invalid email address", groups = CreateContact.class)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format", groups = CreateContact.class)
    private String email;

    @NotBlank(message = "phone number is required", groups = CreateContact.class)
    @Pattern(regexp = "^(97|98)[0-9]{8}$", message = "invalid phone number", groups = CreateContact.class)
    private String phoneNumber;

    @Size(min = 3, message = "address must be atleast 3 character", groups = CreateContact.class)
    private String address;

    private String description;
    @URL(message = "invalid URL formate ", groups = CreateContact.class)
    private String websiteLink;
    @URL(message = "invalid URL formate ", groups = CreateContact.class)
    private String linkedInLink;

    private boolean favorite;

    // @ValidFile(message = "invalid file")

    @ValidFile(message = "Invalid image file", allowedTypes = { "image/jpeg", "image/png", "image/jpg",
            "image/gif" }, maxSize = 5 * 1024 * 1024, required = true, groups = CreateContact.class)

    @ValidFile(message = "Invalid image file", allowedTypes = { "image/jpeg", "image/png", "image/jpg",
            "image/gif" }, maxSize = 5 * 1024 * 1024, required = false, groups = UpdateContact.class)
    private MultipartFile contactImage;

}
