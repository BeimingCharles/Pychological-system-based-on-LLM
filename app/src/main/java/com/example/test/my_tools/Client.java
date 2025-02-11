package com.example.test.my_tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        // 创建客户端Socket对象，来凝结指定IP和端口
        try(Socket clientSocket = new Socket("10.138.161.144",8848)) {

            // 客户端向服务端发送一条信息（输出流）
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));
            writer.write("hello~你好"); // 写入缓冲区（内存）
            writer.flush(); // 清空缓冲区，发送至网络
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
