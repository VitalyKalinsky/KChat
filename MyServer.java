package org.example;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;

public class MyServer {
    final static Object flag = new Object();
    static LinkedList<Socket> sockets;
    static ServerSocket ss;
    static Thread initSocketThread;
    static Thread serviceSocketsThread;
    static Runnable serviceSockets = () -> {
        System.out.println("delivering..");
        while (!ss.isClosed()) {
            if(!sockets.isEmpty()) {
                synchronized (flag) {
                    sockets.forEach(s -> {
                        try {
                            DataInputStream dis = new DataInputStream(s.getInputStream());

                            if (dis.available() > 0) {
                                String resp = dis.readUTF();
                                int len = Integer.parseInt(String.valueOf(resp.charAt(0)));
                                String name = resp.substring(1, len + 1);
                                String msg = resp.substring(len + 1);
                                sockets.stream().filter(s1 -> !s1.equals(s)).forEach(s1 -> {
                                    try {
                                        DataOutputStream dos = new DataOutputStream(s1.getOutputStream());
                                        dos.writeUTF(name + " sent: " + msg);
                                        dos.flush();
                                    } catch (IOException e) {
                                        System.out.println("ex " + e.getMessage());
                                    }
                                });
                                System.out.println("message delivered");
                            }
                        } catch (IOException e) {
                            System.out.println("ex " + Arrays.toString(e.getStackTrace()));
                            throw new RuntimeException(e);
                        }
                    });
                    flag.notify();
                }
            }
        }
        System.out.println("finish");
    };

    static Runnable initSocket = () -> {
        while (!ss.isClosed()) {
            try {
                Socket socket = ss.accept();
                synchronized (flag) {
                    sockets.add(socket);
                    System.out.println(sockets);
                    System.out.println("client accepted");
                    flag.wait(200);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };


    static {
        try {
            sockets = new LinkedList<>();
            ss = new ServerSocket(6666);
            initSocketThread = new Thread(initSocket);
            serviceSocketsThread = new Thread(serviceSockets);
        } catch (IOException e) {
            System.out.println("Port is busy");
        }
    }

    public static void main(String[] args) throws IOException {
        initSocketThread.start();
        serviceSocketsThread.start();
    }
}
