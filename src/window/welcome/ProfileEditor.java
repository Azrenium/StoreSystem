package window.welcome;

import util.ImageUtil;
import window.Window;
import window.WindowManager;

import javax.swing.*;

public class ProfileEditor extends Window {
    private JPanel panel;
    private JLabel imageLabel;
    private JButton removeImageButton;
    private JButton chooseNewImageButton;
    private JTextField textField1;
    private JTextField textField2;
    private JButton changePasswordButton;
    private JButton updateButton;
    private JButton backButton;

    public ProfileEditor() {
        super();

        setContentPane(panel);

        imageLabel.setIcon(ImageUtil.getScaledResource("/images/noProfilePicture.png", 128, 128));

        backButton.addActionListener(e -> {
            WindowManager.startWindow(Welcome.class);

            dispose();
        });
    }
}
