package org.example;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;

public class Test {
    public static void main(String[] args) throws IOException {
        String resp = "5johanhello how are you?";
        int len = Integer.parseInt(String.valueOf(resp.charAt(0)));
        String name = resp.substring(1, len + 1);
        String msg = resp.substring(len + 1);
        System.out.println(name + " " + msg);
    }
}
