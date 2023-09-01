package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;

public class MyServer{
    final static Object flag = new Object();
    static LinkedList<Socket> sockets;
    static ServerSocket ss;


    static {
        try {
            sockets = new LinkedList<>();
            ss = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println("Port is busy");
        }
    }

    public static void main(String[] args) throws IOException {
        Timer timer = new Timer(1000, action -> {
            try {
                synchronized (flag) {
                    sockets.add(ss.accept());
                    System.out.println(sockets);
                }
                System.out.println("client accepted");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        timer.start();
        synchronized (flag) {
            sockets.forEach(s -> {
                try (DataInputStream dis = new DataInputStream(s.getInputStream())) {
                    String message = dis.readUTF();

                    sockets.stream().filter(a -> !(a.equals(s)))
                            .forEach(a -> {
                                try (DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
                                    dos.writeUTF("client says: " + message);
                                    dos.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                } catch (IOException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
