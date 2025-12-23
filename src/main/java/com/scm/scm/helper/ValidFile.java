package com.scm.scm.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Documented
@Repeatable(ValidFile.List.class)
public @interface ValidFile {

    String message() default "Invalid file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Optional: Specify allowed file types
    String[] allowedTypes() default { "image/jpeg", "image/png", "image/jpg", "image/gif" };

    // Optional: Max file size in bytes (5MB default)
    long maxSize() default 5 * 1024 * 1024;

    boolean required() default true;

    @Target({ ElementType.FIELD, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidFile[] value();
    }

}
