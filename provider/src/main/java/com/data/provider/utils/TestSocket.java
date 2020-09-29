package com.data.provider.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TestSocket {
    public static void main(String[] args) throws Exception {
        try {
            Socket socket = new Socket("192.168.235.135", 33472);
            //读取服务器端数据
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //向服务器端发送数据
            PrintStream out = new PrintStream(socket.getOutputStream());
            System.out.print("请输入: \t");
            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.println(str);

            String ret = input.readLine();
            System.out.println("服务器端返回过来的是: " + ret);
            // 如接收到 "OK" 则断开连接
            if ("OK".equals(ret)) {
                System.out.println("客户端将关闭连接");
                Thread.sleep(500);

            }

            out.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    socket = null;
//                    System.out.println("客户端 finally 异常:" + e.getMessage());
//                }
//            }
        }

    }
}
