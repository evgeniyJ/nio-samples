package org.evgeniy.ua.client;

import org.evgeniy.ua.util.ProgramArgumentsUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadTestingClient {

    private static final int DEFAULT_PORT = 4567;

    public static void start(String[] args) {
        int port = ProgramArgumentsUtil.extract(args, 1, Integer::valueOf, DEFAULT_PORT);
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
