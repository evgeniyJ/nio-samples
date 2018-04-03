package org.evgeniy.ua.server;

@FunctionalInterface
public interface Server {

    String DEFAULT_SERVER = "nio";
    int DEFAULT_PORT = 1234;

    String WELCOME_MESSAGE_TEMPLATE = "Hello %1s client\r\n";
    String READ_DATA_MESSAGE_TEMPLATE = "Server reads : %1s\r\n";

    void start() throws Exception;

    static void run(String[] args) throws Exception {
        String server = DEFAULT_SERVER;
        int port = DEFAULT_PORT;
        if (args.length > 1) {
            server = args[0];
            port = Integer.parseInt(args[1]);
        }
        System.out.println(String.format("Starting server : %1s on port : %2s", server, port));
        Server.provide(server, port).start();
    }

    static Server provide(String name, int port) {
        switch (name) {
            case "netty":
                return new NettyEchoServer(port);
            case "nio":
            default:
                return new NioEchoServer(port);
        }
    }
}
