package window.welcome;

import data.Data;
import util.sql.response.table.Manager;
import window.Window;
import window.WindowManager;
import window.login.Login;

import javax.swing.*;

public class Welcome extends Window {
    private JPanel panel;
    private JLabel welcomeLabel;
    private JButton editButton;
    private JButton logOutButton;
    private JButton viewStoresButton;
    private JButton viewEmployeesButton;
    private JButton viewProductsButton;

    public Welcome() {
        super();

        setContentPane(panel);

        if(Data.getManager() == null) {
            System.out.println("No manager has logged in!");
            dispose();
        }

        logOutButton.addActionListener(e -> {
            Data.setManager(null);

            WindowManager.startWindow(Login.class);

            dispose();
        });

        editButton.addActionListener(e -> {
            WindowManager.startWindow(ProfileEditor.class);

            dispose();
        });
    }
}
