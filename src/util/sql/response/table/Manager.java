package util.sql.response.table;

import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Manager {
    private int id;
    private String username;
    private String email;
    private ImageIcon img;

    public Manager(int id, String username, String email){
        this(id, username, email, (ImageIcon) null);
    }

    public Manager(int id, String usernma, String email, InputStream is){
        this(id, usernma, email, ImageUtil.readImage(is));
    }

    public Manager(int id, String username, String email, ImageIcon img){
        this.id = id;
        this.username = username;
        this.email = email;
        this.img = img;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public ImageIcon getImg() {
        return img;
    }

    public InputStream getImgInputStream(){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage bufferedImage = new BufferedImage(
                    img.getIconWidth(),
                    img.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            bufferedImage.getGraphics().drawImage(img.getImage(), 0, 0, null);
            ImageIO.write(bufferedImage, "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }

    public Manager copy(){
        return new Manager(id, username, email, img);
    }
}
