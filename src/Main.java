import window.login.Login;
import window.WindowManager;

public class Main {
    public static void main(String[] args) {
        WindowManager.startWindow(Login.class);

        while (WindowManager.hasActiveWindows()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Main Thread Interrupted!");
                break;
            }
        }
    }
}