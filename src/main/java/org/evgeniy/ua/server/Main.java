package org.evgeniy.ua.server;

public class Main {

    private static final String DEFAULT_SERVER = "nio";
    private static final int DEFAULT_PORT = 1234;

    public static void main(String[] args) throws Exception {
        String server = DEFAULT_SERVER;
        int port = DEFAULT_PORT;
        if (args.length > 1) {
            server = args[0];
            port = Integer.parseInt(args[1]);
        }
        System.out.println(String.format("Starting server : %1s on port : %2s", server, port));
        Server.provide(server, port).start();
    }
}
