package org.jeecg.modules.system.util;

    import javax.validation.ConstraintValidator;
    import javax.validation.ConstraintValidatorContext;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;

    public class MonthFormatValidator implements ConstraintValidator<ValidMonthFormat, String> {

        private SimpleDateFormat dateFormat;

        @Override
        public void initialize(ValidMonthFormat constraintAnnotation) {
            this.dateFormat = new SimpleDateFormat("yyyy-MM");
            this.dateFormat.setLenient(false);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isEmpty()) {
                return false; // 可以根据需要调整是否允许为空
            }
            try {
                dateFormat.parse(value);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }
    