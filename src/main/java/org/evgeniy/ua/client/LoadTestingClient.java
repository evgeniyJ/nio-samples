package org.evgeniy.ua.client;

import org.evgeniy.ua.Main;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadTestingClient {

    public static void main(String[] args) {
        List<Socket> sockets = new ArrayList<>();
        System.out.println("Opening sockets...");
        for (int i = 0; i < 10_000; i++) {
            try {
                sockets.add(new Socket("localhost", Main.DEFAULT_PORT));
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
