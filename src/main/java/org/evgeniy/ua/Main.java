package org.evgeniy.ua;

public class Main {

    public static final int DEFAULT_PORT = 1234;

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        System.out.println(String.format("Starting server : %1s on port : %2s", "nio", port));
        new NioEchoServer(port).start();
    }
}
