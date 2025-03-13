package kma.ktlt.post.domain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private List<String> acceptedValues;
    private String name;


    @Override
    public void initialize(EnumValue enumValue) {
        acceptedValues = Stream.of(enumValue.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
        name = enumValue.name();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        System.out.println(value);
        boolean isValid = acceptedValues.contains(value.toString().toUpperCase());

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            String message = String.format("%s phải là một trong các giá trị được chấp nhận %s", name, String.join(", ", acceptedValues));

            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
