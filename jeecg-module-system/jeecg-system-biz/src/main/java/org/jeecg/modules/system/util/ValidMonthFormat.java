package org.jeecg.modules.system.util;

    import javax.validation.Constraint;
    import javax.validation.Payload;
    import java.lang.annotation.ElementType;
    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;
    import java.lang.annotation.Target;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = MonthFormatValidator.class)
    public @interface ValidMonthFormat {
        String message() default "无效的月份格式，举例，例如填报2024年11月数据，则填写成2024-11.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
    