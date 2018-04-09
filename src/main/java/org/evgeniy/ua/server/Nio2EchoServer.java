package org.evgeniy.ua.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

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
                asynchronousServerSocketChannel.accept(null, acceptCallback(asynchronousServerSocketChannel));
                // Infinite loop
                for (; ; ) {
                    Thread.sleep(1000);
                }
            }
        }
    }

    private static Callback<AsynchronousSocketChannel, Object> acceptCallback(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
        return new Callback<>(
                (client, attachment1) -> {
                    asynchronousServerSocketChannel.accept(null, acceptCallback(asynchronousServerSocketChannel));
                    try {
                        // As async channels API have own thread pool, byte buffer should be per client connection
                        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                        buffer.put(String.format(WELCOME_MESSAGE_TEMPLATE, client.getRemoteAddress()).getBytes());
                        buffer.flip();
                        client.write(buffer, null, new Callback<>(
                                (bytesWritten, attachment2) -> {
                                    buffer.clear();
                                    doRead(client, buffer);
                                }
                        ));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                (exc, attachment) -> {
                    asynchronousServerSocketChannel.accept(null, acceptCallback(asynchronousServerSocketChannel));
                    throw new UnsupportedOperationException("Cannot accept connections!");
                }
        );
    }

    private static void doRead(AsynchronousSocketChannel client, ByteBuffer buffer) {
        client.read(buffer, null, new Callback<>(
                (bytesRead, attachment1) -> {
                    buffer.flip();
                    String clientMessage = StandardCharsets.UTF_8.decode(buffer).toString();
                    String response = String.format(READ_DATA_MESSAGE_TEMPLATE, clientMessage);
                    ByteBuffer bb = ByteBuffer.wrap(response.getBytes());
                    client.write(bb, null, new Callback<>(
                            (bytesWritten, attachment2) -> {
                                buffer.clear();
                                doRead(client, buffer);
                            }
                    ));
                }
        ));
    }

    private static class Callback<V, A> implements CompletionHandler<V, A> {

        private final BiConsumer<V, A> completedHandler;
        private final BiConsumer<Throwable, A> failedHandler;

        private Callback(BiConsumer<V, A> completedHandler) {
            this(completedHandler, (exc, attachment) -> exc.printStackTrace());
        }

        private Callback(BiConsumer<V, A> completedHandler, BiConsumer<Throwable, A> failedHandler) {
            this.completedHandler = completedHandler;
            this.failedHandler = failedHandler;
        }


        @Override
        public void completed(V result, A attachment) {
            completedHandler.accept(result, attachment);
        }

        @Override
        public void failed(Throwable exc, A attachment) {
            failedHandler.accept(exc, attachment);
        }
    }
}
