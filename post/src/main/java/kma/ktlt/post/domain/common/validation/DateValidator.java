package kma.ktlt.post.domain.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements ConstraintValidator<DateAnnotationCus, String> {

    private String dateFormat;

    @Override
    public void initialize(DateAnnotationCus constraintAnnotation) {
        this.dateFormat = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null) {
            return false;
        }

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return false;
        }

        // Xác thực ngày tháng năm
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            Date parsedDate = sdf.parse(date);
            Date minDate = sdf.parse("1930-01-01");
            Date currentDate = new Date();

            if (parsedDate.before(minDate) || parsedDate.after(currentDate)) {
                return false;
            }

            return true;

        } catch (ParseException e) {
            return false; // Ngày không hợp lệ
        }
    }
}