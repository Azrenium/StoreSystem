package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static ImageIcon readImage(InputStream is) {
        try{
            BufferedImage image = ImageIO.read(is);
            return new ImageIcon(image);
        } catch (IOException | NullPointerException | IllegalArgumentException e){
            System.out.println("Error while loading image: " + e.getMessage());
            return null;
        }
    }
}
