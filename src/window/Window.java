package window;

import javax.swing.*;
import java.awt.*;

public abstract class Window extends JFrame {
    public Window() {
        setSize(700, 700);
        setVisible(true);

        WindowManager.increaseActiveWindows();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void dispose() {
        WindowManager.decreaseActiveWindows();

        super.dispose();
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);

        revalidate();
    }
}
