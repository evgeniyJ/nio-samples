package org.evgeniy.ua.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NioEchoServer implements Server {

    // Use the same byte buffer for all channels. A single thread is
    // servicing all the channels, so no danger of concurrent access.
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    private final int port;

    public NioEchoServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        //create socket channel, selector, bind server on port and register in selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        Selector selector = Selector.open();

        serverSocket.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //consume requests
        while (true) {
            //blocking call
            int readyChannels = selector.select();
            //still need to do check as another thread can invoke Selector#wakeup
            if (readyChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid()) {
                        try {
                            if (key.isAcceptable()) {
                                //safe cast as only ServerSocketChannel support OP_ACCEPT operation
                                //also for this sample channel is our serverSocketChannel reference
                                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                                SocketChannel client = channel.accept();
                                client.configureBlocking(false);
                                client.register(selector, SelectionKey.OP_READ);
                                writeWelcomeMessage(client);
                            }
                            if (key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                writeReadDataMessage(channel);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void writeWelcomeMessage(SocketChannel channel) throws Exception {
        //clear buffer
        buffer.clear();
        //put content
        buffer.put(String.format(WELCOME_MESSAGE_TEMPLATE, channel.getRemoteAddress()).getBytes());
        //make buffer readable
        buffer.flip();
        //write to channel
        channel.write(buffer);
    }

    private void writeReadDataMessage(SocketChannel channel) throws Exception {
        int count;
        buffer.clear();
        // Loop while data is available; channel is nonblocking
        // We can also remove loop and store data in local map
        // than when we read all bytes (for example we read end-line
        // special symbol for our custom protocol), then we write
        // data to client
        while ((count = channel.read(buffer)) > 0) {
            buffer.flip();
            String clientMessage = StandardCharsets.UTF_8.decode(buffer).toString();
            String response = String.format(READ_DATA_MESSAGE_TEMPLATE, clientMessage);
            ByteBuffer bb = ByteBuffer.wrap(response.getBytes());
            while (bb.hasRemaining()) {
                channel.write(bb);
            }
            // WARNING: the above loop is evil. You
            // should put in local map (for example)
            // and continue loop over selection keys,
            // than you will again be here and can
            // write chunk of data to channel,
            // if hasRemaining = false, than you wrote
            // all data to client.
            buffer.clear();
        }
        if (count < 0) {
            // Close channel on EOF, invalidates the key
            channel.close();
        }
    }
}
