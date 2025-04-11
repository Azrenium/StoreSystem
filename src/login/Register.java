package login;

import org.mindrot.jbcrypt.BCrypt;
import util.sql.Connect;
import util.ErrorResponse;
import util.validation.Validator;
import window.Window;
import window.WindowManager;

import javax.swing.*;

public class Register extends Window {
    private JPanel panel;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public Register() {
        super();

        setContentPane(panel);

        registerButton.addActionListener(e -> {
            //Initial Checks
            if(usernameField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter username");
                return;
            } else if(emailField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "No email address entered!");
                return;
            } else if(new String(passwordField.getPassword()).isEmpty()){
                JOptionPane.showMessageDialog(null, "No password entered!");
                return;
            }

            //Validation checks
            ErrorResponse uvr = Validator.isUsernameValid(usernameField.getText());
            if(!uvr.isValid()){
                JOptionPane.showMessageDialog(null, uvr.getErrorMessagesAsString());
                return;
            }

            ErrorResponse evr = Validator.isEmailValid(emailField.getText());
            if(!evr.isValid()){
                JOptionPane.showMessageDialog(null, evr.getErrorMessagesAsString());
                return;
            }

            String password = new String(passwordField.getPassword());
            ErrorResponse pvr = Validator.isPasswordValid(password);
            if(!pvr.isValid()){
                JOptionPane.showMessageDialog(null, pvr.getErrorMessagesAsString());
                return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            ErrorResponse response = Connect.registerManager(usernameField.getText(), emailField.getText(), hashedPassword);
            if(response.isValid()){
                WindowManager.startWindow(Creation.class);

                dispose();
                return;
            } else{
                JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
            }
        });
    }
}
