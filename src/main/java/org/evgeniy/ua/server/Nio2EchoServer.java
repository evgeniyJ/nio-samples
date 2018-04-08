package org.evgeniy.ua.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class Nio2EchoServer implements Server {

    private final int port;

    public Nio2EchoServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        // Default group (default thread pool)
        try (AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()) {
            if (asynchronousServerSocketChannel.isOpen()) {
                asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
                asynchronousServerSocketChannel.accept(
                        null,
                        new CompletionHandler<AsynchronousSocketChannel, Void>() {

                            @Override
                            public void completed(AsynchronousSocketChannel client, Void attachment) {
                                asynchronousServerSocketChannel.accept(null, this);
                                accept(client);
                            }

                            @Override
                            public void failed(Throwable exc, Void attachment) {
                                asynchronousServerSocketChannel.accept(null, this);
                                throw new UnsupportedOperationException("Cannot accept connections!");
                            }
                        });
                // Infinite loop
                for (; ; ) {
                    Thread.sleep(1000);
                }
            }
        }
    }

    private static void accept(AsynchronousSocketChannel client) {
        try {
            // As async channels API have own thread pool, byte buffer should be per client connection
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            buffer.put(String.format(WELCOME_MESSAGE_TEMPLATE, client.getRemoteAddress()).getBytes());
            buffer.flip();
            client.write(buffer).get();
            buffer.clear();
            while (client.read(buffer).get() != -1) {
                buffer.flip();
                String clientMessage = StandardCharsets.UTF_8.decode(buffer).toString();
                String response = String.format(READ_DATA_MESSAGE_TEMPLATE, clientMessage);
                ByteBuffer bb = ByteBuffer.wrap(response.getBytes());
                // Send the data; don't assume it goes all at once
                while (bb.hasRemaining()) {
                    client.write(bb).get();
                }
                buffer.clear();
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            System.err.println(ex);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
