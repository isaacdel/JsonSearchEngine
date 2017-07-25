package Validators;


import Constants.ApplicationConstants;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;


public class FileValidator implements IParameterValidator {
    public static ApplicationConstants applicationConstants = new ApplicationConstants();

    public void validate(String name, String value) throws ParameterException {
        if (!value.equals(applicationConstants.USERS) && !value.equals(applicationConstants.TICKETS) &&
                !value.equals(applicationConstants.ORGANIZATIONS)) {
            throw new ParameterException("Parameter " + name + " should be one of the supported files!");
        }
    }
}
