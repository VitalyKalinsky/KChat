package org.example;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;

public class Test {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        serverSocket.accept();
        serverSocket.accept();
        serverSocket.close();
    }
}
