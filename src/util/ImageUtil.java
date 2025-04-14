package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static ImageIcon readImage(InputStream is) {
        try{
            BufferedImage image = ImageIO.read(is);
            return new ImageIcon(image);
        } catch (IOException | NullPointerException | IllegalArgumentException e){
            return null;
        }
    }

    public static ImageIcon getScaledResource(String path, int width, int height) {
        ImageIcon i = new ImageIcon(ImageUtil.class.getResource(path));

        return new ImageIcon(i.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
