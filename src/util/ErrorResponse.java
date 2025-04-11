package util.validation;

import java.util.ArrayList;

public class ValidationResponse{
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
