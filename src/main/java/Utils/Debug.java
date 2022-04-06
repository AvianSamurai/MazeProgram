package Utils;

public class Debug {

    /**
     * Prints str to the console formatted as following
     * nameOfCallingClass] str
     * Adds a new line to the end of the print statement
     *
     * @param str string to log
     */
    public static void LogLn(String str) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println(className + "] " + str);
    }

    /**
     * Prints str to the console formatted as following
     * nameOfCallingClass] str
     *
     * @param str string to log
     */
    public static void Log(String str) {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.print(className + "] " + str);
    }
}
