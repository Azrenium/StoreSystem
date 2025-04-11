import login.Login;
import window.WindowManager;

public class Main {
    public static void main(String[] args) {
        WindowManager.startWindow(Login.class);

        while (WindowManager.hasActiveWindows()){
            //keep going
        }
    }
}