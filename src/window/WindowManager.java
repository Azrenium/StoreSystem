package window;

public class WindowManager {
    private static int activeWindows = 0;

    public static void increaseActiveWindows() {
        synchronized (WindowManager.class) {
            ++activeWindows;
        }
    }

    public static void decreaseActiveWindows() {
        synchronized (WindowManager.class) {
            --activeWindows;
        }
    }

    public static boolean hasActiveWindows() {
        synchronized (WindowManager.class) {
            return activeWindows > 0;
        }
    }

    public static void startWindow(Class<? extends Window> windowClass) {
        new Thread(() -> {
            try {
                windowClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                System.out.println("Exception on window start: " + ex.getMessage());
            }
        }).start();
    }
}
