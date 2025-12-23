package com.scm.scm.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private String[] allowedTypes;
    private long maxSize;
    private boolean required;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
        this.maxSize = constraintAnnotation.maxSize();
        this.required = constraintAnnotation.required();

    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        // If file is null or empty
        if (file == null || file.isEmpty()) {

            if (required) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Please select an image file")
                        .addConstraintViolation();
                return false;
            }

            return true;

        }

        // Check file size
        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File size must be less than " + (maxSize / (1024 * 1024)) + "MB")
                    .addConstraintViolation();
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(allowedTypes).contains(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only JPEG, PNG, JPG, and GIF images are allowed")
                    .addConstraintViolation();
            return false;
        }

        // Optional: Check file extension
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid file extension")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
