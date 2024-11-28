package com.example.test.my_tools;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static int statusCode;

    public static String getRequest(String url) throws IOException {
        // 创建 OkHttp 客户端
        OkHttpClient client = new OkHttpClient();

        // 创建 GET 请求
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.i("d","true");
        // 发送请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            // 检查响应是否成功

            if (!response.isSuccessful()) {  Log.i("d","dsfasdfdsaddddddddd");
                throw new IOException("Unexpected code " + response);

            }
            statusCode = response.code();
            Log.i("num", String.valueOf(statusCode));
            // 返回响应体内容
            return response.body().string();
        }catch (Exception e){
            Log.i("j","");
        }
        return "rrr";
    }

    public static String postRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        try {
            // 确保在调用后关闭 response 以释放资源
            statusCode = response.code();

            // 检查响应体是否为 null
            if (response.body() != null) {
                return response.body().string();
            } else {
                // 如果响应体为空，返回一个错误消息或做其他处理
                return "Error: Response body is null";
            }
        } finally {
            // 确保无论如何都会关闭响应
            if (response != null) {
                response.close();
            }
        }
    }


    public static int getStatusCode() {
        return statusCode;
    }

    // 打印日志
    public static void logParams(String username, String password) {
        Log.i("username", username);
        Log.i("password", password);
    }

    // 处理服务器响应
    // 处理服务器响应
    // 处理服务器响应，增加 shouldNavigate 参数来控制是否跳转页面
    public static String handleResponse(String res, Activity activity, boolean shouldNavigate,Class<?> targetActivity) throws JSONException {

        if (res.trim().startsWith("{")) { // 简单检查是否是 JSON 开头
            JSONObject jsonObject = new JSONObject(res);

            // 获取响应的消息字段
            String msg = jsonObject.optString("message", "No message found");

            // 根据 statusCode 进行不同的处理
            if (statusCode==200) {
                // 正确响应，处理业务逻辑
                Log.i("ResponseHandler", "Received successful response with status code 200");

                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

                    // 只有在 shouldNavigate 为 true 时才跳转页面
                    if (shouldNavigate) {
                        Intent intent = new Intent(activity, targetActivity);  // 替换为你要跳转的 Activity
                        activity.startActivity(intent);
                    }
                });

               // return msg;
            } else {
                // 错误处理，非 200 状态码
               // Log.i("esponseHandler", "Received error response with status code: " + statusCode);
                activity.runOnUiThread(() -> Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show());
            }
        }
        else {
            // 非 JSON 格式的响应
            Log.i("ResponseHandler", "Unexpected response format, not a valid JSON.");
            activity.runOnUiThread(() -> Toast.makeText(activity, "Unexpected response format: " + res, Toast.LENGTH_LONG).show());
        }
        return "你好";
    }


    public static String handleResponse2(String res) throws JSONException {
        if (res.trim().startsWith("{")) { // 简单检查是否是 JSON 开头
            JSONObject jsonObject = new JSONObject(res);

            // 获取响应的消息字段
            String msg = jsonObject.optString("content", "No message found");

            // 根据 statusCode 进行不同的处理
            if (statusCode == 200) {
                // 正确响应，处理业务逻辑
                Log.i("ResponseHandler", "Received successful response with status code 200");
                String content2=jsonObject.getString("content");
                //System.out.println(content2);
                System.out.println(statusCode);
                return content2;
            } else {
                // 错误处理，非 200 状态码
                Log.i("ResponseHandler", "Received error response with status code: " + statusCode);
            }
        } else {
            // 非 JSON 格式的响应
            Log.i("ResponseHandler", "Unexpected response format, not a valid JSON.");
        }
        return "nothing";
    }

    public static String[][] handleResponse3(String res) throws JSONException {
        String[][] sett = new String[5][1];
        if (res.trim().startsWith("{")) { // 简单检查是否是 JSON 开头
            JSONObject jsonObject = new JSONObject(res);

            // 获取响应的消息字段
            //String msg = jsonObject.optString("names", "No message found");

            // 根据 statusCode 进行不同的处理
            if (statusCode == 200) {
                // 正确响应，处理业务逻辑
                Log.i("ResponseHandler", "Received successful response with status code 200");
                String content2 = jsonObject.getString("names");
                String content3 = jsonObject.getString("introductions");
                //清洗数据
                String trimmedString = content2.substring(2, content2.length() - 2);
                String trtt = content3.substring(2, content3.length() - 2);
                String[] itt = trtt.split("\",\"");


                // 以逗号分割字符串
                String[] items = trimmedString.split("\",\"");

                sett[0] = items;

                sett[1] = itt;
                // 输出结果
                for (String item : items) {
                    System.out.println(item);
                }
                for (String item : itt) {
                    System.out.println(item);
                }
                //System.out.println(content2);
                System.out.println(statusCode);
                return sett;
            } else {
                // 错误处理，非 200 状态码
                Log.i("ResponseHandler", "Received error response with status code: " + statusCode);
            }
        } else {
            // 非 JSON 格式的响应
            Log.i("ResponseHandler", "Unexpected response format, not a valid JSON.");
        }

        return sett;
    }
}

