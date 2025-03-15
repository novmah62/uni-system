package kma.ktlt.post.domain.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumListValidator.class)
@Documented
public @interface ValidEnumList {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid enum value : {acceptValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
