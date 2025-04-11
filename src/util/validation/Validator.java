package util;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validator {
    private static Pattern emailRegexPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    public static ValidationResponse isUsernameValid(String username) {
        ValidationResponse response = new ValidationResponse();

        for(char c : username.toCharArray()) {
            if(!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                response.addErrorMessage("Username must be alphanumeric!");
                return response;
            }
        }

        if(username.length() < 4 || username.length() > 20) response.addErrorMessage("Username must be between 4 and 20 characters!");

        return response;
    }

    public static ValidationResponse isEmailValid(String email) {
        ValidationResponse response = new ValidationResponse();

        if(!emailRegexPattern.matcher(email).matches()) response.addErrorMessage("Invalid email!");

        return response;
    }

    public static ValidationResponse isPasswordValid(String password) {
        ValidationResponse response = new ValidationResponse();

        if(password.length() < 8) response.addErrorMessage("Password must be at least 8 characters!");

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

    public static class ValidationResponse{
        private boolean valid = true;
        private final ArrayList<String> messages = new ArrayList<>();

        public boolean isValid() {
            return valid;
        }

        public void invalidate(){
            this.valid = false;
        }

        /**
         * Automatically invalidates the response.
         * @param message   The error message to add to the response.
         */
        public void addErrorMessage(String message){
            this.messages.add(message);
            invalidate();
        }

        public ArrayList<String> getErrorMessages(){
            return this.messages;
        }

        public String getErrorMessagesAsString(){
            StringBuilder sb = new StringBuilder();

            this.messages.forEach(it -> sb.append(it).append("\n"));

            if(!this.messages.isEmpty()) sb.deleteCharAt(sb.length()-1);

            return sb.toString();
        }
    }
}
