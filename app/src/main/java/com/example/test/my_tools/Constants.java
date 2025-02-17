package com.example.test.my_tools; // 根据你的实际包名进行修改
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Constants {

    // 服务器的 IP 地址
//    public static final String IP = "http://10.0.2.2:6671"; // 或者是实际的服务器 IP 地址
   // GetIPAddress ip=new GetIPAddress();
//    public static  String IP = "http://192.168.43.37:6671";
    public static  String IP = "http://192.168.10.83:6671";
//    static {
//        GetIPAddress ipGetter = new GetIPAddress();
//        String ipAddress = ipGetter.open(); // 调用 open 方法获取 IP 地址
//        IP = "http://" + ipAddress + ":6671"; // 动态拼接 IP 地址
//        Log.i("ip",IP);
//    }
    // 或者是实际的服务器 IP 地址

    // 错误消息
    public static final String NETERR = "网络错误，请稍后再试"; // 网络错误提示

}
