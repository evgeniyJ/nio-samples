package org.evgeniy.ua.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadTestingClient {

    private static final int DEFAULT_PORT = 4567;

    public static void start(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        List<Socket> sockets = new ArrayList<>();
        System.out.println("Opening sockets for server on port " + port + "...");
        for (int i = 0; i < 10_000; i++) {
            try {
                sockets.add(new Socket("localhost", port));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Print any string to exit");
        new Scanner(System.in).next();

        System.out.println("Closing connections");
        sockets.forEach(s -> {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
