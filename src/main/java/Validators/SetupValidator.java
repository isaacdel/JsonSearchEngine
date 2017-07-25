package Validators;

import Constants.ApplicationConstants;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class SetupValidator implements IParameterValidator {
    public static ApplicationConstants applicationConstants = new ApplicationConstants();

    public void validate(String setup, String value) throws ParameterException {
        if (!value.equals(applicationConstants.TRUE))
            throw new ParameterException("Parameter " + setup + " should be true!");
    }
}
