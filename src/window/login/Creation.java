package window.login;

import window.Window;
import window.WindowManager;

import javax.swing.*;

public class Creation extends Window {
    private JButton backToLoginButton;
    private JPanel panel;

    public Creation() {
        super();

        setContentPane(panel);

        backToLoginButton.addActionListener(e -> {
            WindowManager.startWindow(Login.class);

            dispose();
        });
    }
}
