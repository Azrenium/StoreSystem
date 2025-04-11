package login;

import util.sql.Connect;
import util.sql.response.RowResponse;
import util.sql.response.table.Manager;
import window.Window;
import window.WindowManager;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class Login extends Window {
    private JPanel panel;
    private JTextField usernameField;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JButton registerButton;

    public Login() {
        super();

        setContentPane(panel);

        registerButton.addActionListener(e -> {
            WindowManager.startWindow(Register.class);

            dispose();
        });

        loginButton.addActionListener(e -> {
            //Initial Checks
            if(usernameField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username or Password field is empty!");
                return;
            }

            //Attempt Login
            RowResponse<Manager> response = Connect.getManager(usernameField.getText(), new String(passwordField.getPassword()));

            if(!response.isValid()){
                JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
            } else{
                Manager manager = response.getData();
                JOptionPane.showMessageDialog(null, "Successful login as: " + manager.getUsername());
            }
        });
    }
}
