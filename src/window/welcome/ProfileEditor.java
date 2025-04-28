package window.welcome;

import data.Data;
import org.mindrot.jbcrypt.BCrypt;
import util.ErrorResponse;
import util.ImageUtil;
import util.sql.Connect;
import util.sql.response.row.Manager;
import util.validation.Validator;
import window.Window;
import window.WindowManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ProfileEditor extends Window {
    private static final ImageIcon noImageIcon = ImageUtil.getScaledResource("/images/noProfilePicture.png", 128, 128);

    private JPanel panel;
    private JLabel imageLabel;
    private JButton removeImageButton;
    private JButton chooseNewImageButton;
    private JTextField usernameField;
    private JTextField emailField;
    private JButton changePasswordButton;
    private JButton updateButton;
    private JButton backButton;

    private Manager editableManager;

    public ProfileEditor() {
        super();

        setContentPane(panel);

        SwingUtilities.invokeLater(this::setupData);

        backButton.addActionListener(e -> {
            WindowManager.startWindow(Welcome.class);

            dispose();
        });

        removeImageButton.addActionListener(e -> {
            imageLabel.setIcon(noImageIcon);

            editableManager.setImg(null);
        });

        chooseNewImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            int result = chooser.showOpenDialog(null);

            if(result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();

                try {
                    Image image = ImageIO.read(selectedFile);

                    if(image == null){
                        JOptionPane.showMessageDialog(null, "No image selected");
                        return;
                    }

                    ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(imageIcon);
                    editableManager.setImg(imageIcon);
                } catch (IOException ex){
                    JOptionPane.showMessageDialog(null, "Error getting image: " + ex.getMessage());
                }
            }
        });

        updateButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();

            ErrorResponse usernameResponse = Validator.isUsernameValid(username);
            ErrorResponse emailResponse = Validator.isEmailValid(email);

            if(!usernameResponse.isValid()){
                JOptionPane.showMessageDialog(null, usernameResponse.getErrorMessagesAsString());
                return;
            }

            if(!emailResponse.isValid()){
                JOptionPane.showMessageDialog(null, emailResponse.getErrorMessagesAsString());
                return;
            }

            editableManager.setUsername(usernameField.getText());
            editableManager.setEmail(emailField.getText());

            ErrorResponse response = Connect.updateManagerInfo(editableManager);

            if(!response.isValid()){
                JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
                return;
            }

            JOptionPane.showMessageDialog(null, "Successfully updated profile!");
            Data.setManager(editableManager.copy());
        });

        changePasswordButton.addActionListener(e -> {
            JPasswordField p1 = new JPasswordField();
            JPasswordField p2 = new JPasswordField();
            int isOk = JOptionPane.showConfirmDialog(null, p1, "Change Password", JOptionPane.OK_CANCEL_OPTION);
            if(isOk != JOptionPane.OK_OPTION) return;
            isOk = JOptionPane.showConfirmDialog(null, p2, "Change Password", JOptionPane.OK_CANCEL_OPTION);
            if(isOk != JOptionPane.OK_OPTION) return;
            String pass1 = new String(p1.getPassword());
            String pass2 = new String(p2.getPassword());
            if(pass1.equals(pass2)){
                ErrorResponse passwordResponse = Validator.isPasswordValid(pass1);

                if(!passwordResponse.isValid()){
                    JOptionPane.showMessageDialog(null, passwordResponse.getErrorMessagesAsString());
                    return;
                }

                String hashedPassword = BCrypt.hashpw(pass1, BCrypt.gensalt());
                ErrorResponse response = Connect.updateManagerPassword(editableManager, hashedPassword);

                if(!response.isValid()){
                    JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
                    return;
                }

                JOptionPane.showMessageDialog(null, "New password set successfully!");
            } else{
                JOptionPane.showMessageDialog(null, "Passwords do not match!");
                return;
            }
        });
    }

    private void setupData(){
        editableManager = Data.getManager().copy();

        if(editableManager.getImg() == null) {
            imageLabel.setIcon(noImageIcon);
        } else{
            imageLabel.setIcon(editableManager.getImg());
        }

        usernameField.setText(editableManager.getUsername());
        emailField.setText(editableManager.getEmail());
    }
}
