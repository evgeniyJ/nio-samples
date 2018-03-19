package org.evgeniy.ua.server;

@FunctionalInterface
public interface Server {

    String WELCOME_MESSAGE_TEMPLATE = "Hello %1s client\r\n";
    String READ_DATA_MESSAGE_TEMPLATE = "Server reads : %1s\r\n";

    void start() throws Exception;

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
