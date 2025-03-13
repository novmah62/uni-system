package kma.ktlt.post.domain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumListValidator implements ConstraintValidator<ValidEnumList, List<String>> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnumList annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null) return true;
        boolean isValid =  values.stream().allMatch(acceptedValues::contains);

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            String message = String.format("phải là một trong các giá trị được chấp nhận %s", String.join(", ", acceptedValues));

            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
