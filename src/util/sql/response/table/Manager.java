package util.sql.response.table;

import util.ImageUtil;

import javax.swing.*;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ImageIcon getImg() {
        return img;
    }
}
