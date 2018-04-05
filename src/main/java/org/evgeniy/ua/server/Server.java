package org.evgeniy.ua.server;

import org.evgeniy.ua.util.ProgramArgumentsUtil;

@FunctionalInterface
public interface Server {

    String DEFAULT_SERVER = "nio";
    int DEFAULT_PORT = 1234;

    String WELCOME_MESSAGE_TEMPLATE = "Hello %1s client\r\n";
    String READ_DATA_MESSAGE_TEMPLATE = "Server reads : %1s\r\n";

    void start() throws Exception;

    static void run(String[] args) {
        String server = ProgramArgumentsUtil.extract(args, 1, DEFAULT_SERVER);
        int port = ProgramArgumentsUtil.extract(args, 2, Integer::valueOf, DEFAULT_PORT);
        System.out.println(String.format("Starting server : %1s on port : %2s", server, port));
        try {
            Server.provide(server, port).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Server provide(String name, int port) {
        switch (name) {
            case "netty":
                return new NettyEchoServer(port);
            case "nio2":
                return new Nio2EchoServer(port);
            case "nio":
            default:
                return new NioEchoServer(port);
        }
    }
}
