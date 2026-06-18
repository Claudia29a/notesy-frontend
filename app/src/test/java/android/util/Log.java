package android.util;

/**
 * Minimal stub for android.util.Log to allow plain JVM unit tests to run.
 * Only the methods used by the codebase are provided.
 */
public final class Log {
    private Log() {}

    public static int e(String tag, String msg, Throwable tr) {
        // no-op in unit tests
        return 0;
    }

    public static int e(String tag, String msg) {
        return 0;
    }

    public static int d(String tag, String msg) {
        return 0;
    }
}

