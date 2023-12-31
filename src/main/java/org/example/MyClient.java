package org.example;

import java.io.*;
import java.net.Socket;

public class MyClient {
    static Socket s;
    static String name;
    static Runnable read_message = () ->{
        try{
            while (!s.isClosed()) {
                String res = "";
                DataInputStream din = new DataInputStream(s.getInputStream());
                res = din.readUTF();
                System.out.println(res + "\n");
            }
        } catch (IOException e) {
            System.out.println("ex " + e.getMessage());
        }
    };
    static {
        try {
            s = new Socket("localhost", 6666);
            new Thread(read_message).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        name = args[0];
        try (DataOutputStream dout = new DataOutputStream(s.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String req = "";
            while (!req.equals("stop")) {
                req = reader.readLine();
                dout.writeUTF(formatString(req));
                dout.flush();
            }
        } catch (Exception e) {
            System.out.println("ex " + e.getMessage());
        }
        System.out.println("thx");
    }

    private static String formatString(String str){
        return name.length() + name + str;
    }
}