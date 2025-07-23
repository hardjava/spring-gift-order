package gift.validation;

import gift.dto.CreateProductRequestDto;
import gift.dto.OptionRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductRequestValidator implements ConstraintValidator<ValidProductRequest, CreateProductRequestDto> {

    private static final String VALID_NAME_PATTERN = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&/_]*$";

    @Override
    public boolean isValid(CreateProductRequestDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (!isValidProductName(dto.name(), context)) {
            isValid = false;
        }

        if (!isValidPrice(dto.price(), context)) {
            isValid = false;
        }

        if (!isValidOptions(dto.options(), context)) {
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidProductName(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank() || name.length() > 15 || !name.matches(VALID_NAME_PATTERN)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("상품 이름이 유효하지 않습니다.")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isValidPrice(Long price, ConstraintValidatorContext context) {
        if (price == null || price < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("가격은 0 이상이어야 합니다.")
                    .addPropertyNode("price")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isValidOptions(List<OptionRequestDto> options, ConstraintValidatorContext context) {
        if (options == null || options.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("옵션은 하나 이상 포함되어야 합니다.")
                    .addPropertyNode("options")
                    .addConstraintViolation();

            return false;
        }

        Set<String> names = new HashSet<>();
        boolean isValid = true;

        for (int i = 0; i < options.size(); i++) {
            OptionRequestDto option = options.get(i);
            String optionName = option.name();
            int optionQuantity = option.quantity();

            if (optionName == null || optionName.isBlank() || optionName.length() > 50 ||
                !optionName.matches(VALID_NAME_PATTERN)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("옵션 이름이 유효하지 않습니다: " + optionName)
                        .addPropertyNode("options")
                        .addConstraintViolation();

                isValid = false;
            }

            if (!names.add(optionName)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("옵션 이름이 중복됩니다: " + optionName)
                        .addPropertyNode("options")
                        .addConstraintViolation();

                isValid = false;
            }

            if (optionQuantity < 1 || optionQuantity > 100000000) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("옵션 수량은 최소 1개 이상 1억 개 미만입니다.: " + optionQuantity)
                        .addPropertyNode("options")
                        .addConstraintViolation();

                isValid = false;
            }
        }

        return isValid;
    }
}
