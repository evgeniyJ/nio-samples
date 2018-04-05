package org.evgeniy.ua.server;

//TODO implement server by using NIO2 features (async channels)
public class Nio2EchoServer implements Server {

    private final int port;

    public Nio2EchoServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {

    }
}
