package utils;

public class TestStatusManager {

    private static ThreadLocal<String> status = new ThreadLocal<>();

    public static void setPass() {
        status.set("PASS");
    }

    public static void setFail() {
        status.set("FAIL");
    }
    
    public static void setSkip() {
        status.set("SKIP");
    }
    
    public static String getStatus() {
        return status.get() == null ? "PASS" : status.get();
    }

    public static void clear() {
        status.remove();
    }
}