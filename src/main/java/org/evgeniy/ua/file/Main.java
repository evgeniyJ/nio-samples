package org.evgeniy.ua.file;

public class Main {

    private static final String DEFAULT_DIRECTORY = "D:\\test";

    public static void main(String[] args) {
        String path = DEFAULT_DIRECTORY;
        if (args.length > 0) {
            path = args[0];
        }
        new DirectoryWatchService(path).monitor();
    }
}
