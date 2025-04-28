package util.validation;

import util.ErrorResponse;

import java.util.regex.Pattern;

public class Validator {
    private static Pattern emailRegexPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static ErrorResponse isUsernameValid(String username) {
        ErrorResponse response = new ErrorResponse();

        for(char c : username.toCharArray()) {
            if(!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                response.addErrorMessage("Username must be alphanumeric!");
                return response;
            }
        }

        if(username.length() < 4 || username.length() > 20) response.addErrorMessage("Username must be between 4 and 20 characters!");

        return response;
    }

    public static ErrorResponse isEmailValid(String email) {
        ErrorResponse response = new ErrorResponse();

        if(!emailRegexPattern.matcher(email).matches()) response.addErrorMessage("Invalid email!");

        return response;
    }

    public static ErrorResponse isPasswordValid(String password) {
        ErrorResponse response = new ErrorResponse();

        if(password.length() < 7) response.addErrorMessage("Password must be at least 8 characters!");

        boolean hasSpecial = false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        for(char c : password.toCharArray()) {
            if(Character.isDigit(c)) hasNumber = true;
            else if(!Character.isAlphabetic(c)) hasSpecial = true;
            else if(Character.isUpperCase(c)) hasCapital = true;
        }

        if(!hasSpecial) response.addErrorMessage("Password must have a special character!");
        if(!hasCapital) response.addErrorMessage("Password must have a capital character!");
        if(!hasNumber) response.addErrorMessage("Password must have a number!");

        return response;
    }
}
