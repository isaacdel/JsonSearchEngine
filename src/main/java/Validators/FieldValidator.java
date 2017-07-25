package Validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class FieldValidator implements IParameterValidator {
    public void validate(String field, String value) throws ParameterException {
        if (value == null)
            throw new ParameterException("Parameter " + field + " should not be null!");
    }
}
