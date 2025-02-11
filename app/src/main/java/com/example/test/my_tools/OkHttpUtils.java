package com.example.test.my_tools;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.test.fragment.QuestionnaireResultFragment;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static int statusCode;

    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static String uploadImage(File imageFile, int id) throws IOException {
        Log.d("UploadImage", "开始上传图片，用户 ID: " + id);

        if (imageFile == null || !imageFile.exists()) {
            Log.e("UploadImage", "文件不存在或为空，无法上传");
            throw new IOException("未找到图片文件进行上传");
        }

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // JSON 数据中改为 id
        String jsonContent = "{\"id\": \"" + id + "\"}";
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json"), jsonContent);

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile", imageFile.getName(), imageBody) // 文件部分
                .addFormDataPart("id", String.valueOf(id)) // id 参数
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.IP + "/profile_bp/upload")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e("UploadImage", "请求失败，响应码: " + response.code());
                Log.e("UploadImage", "响应内容: " + response.body().string());
                throw new IOException("请求失败，响应码: " + response.code());
            }

            String responseBody = response.body().string();
            Log.d("UploadImage", "上传成功，后端返回: " + responseBody);

            return responseBody;
        }
    }







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
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
                .readTimeout(30, TimeUnit.SECONDS)     // 读取超时
                .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时
                .build();

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

    public static Response postRequest2(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // 设置连接超时
                .readTimeout(30, TimeUnit.SECONDS)     // 设置读取超时
                .writeTimeout(30, TimeUnit.SECONDS)    // 设置写入超时
                .build();

        // 创建 JSON 请求体
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        // 构建 POST 请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 执行请求并返回响应
        return client.newCall(request).execute();
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
            int id=jsonObject.getInt("user_id");
            String ScoreScl90=jsonObject.getString("scl_90");
            String ScoreSDS=jsonObject.getString("SDS");
            Log.i("fuck",ScoreSDS);

            //获取已存在数据
            Data.setId(id);
            Log.i("心理状",String.valueOf(Data.id));
            Data.setScore_Scl_90(ScoreScl90);
            Data.setScore_SDS(ScoreSDS);
            QuestionnaireResultFragment.over[0]=Data.Score_Scl_90;
            QuestionnaireResultFragment.over[1]=Data.Score_SDS;
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
        String[][] sett = new String[2][2];
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
    //处理问卷问题
    public static String[] handleResponse4(String res) throws JSONException {
        String[] quesall=new String[100];
        if (res.trim().startsWith("{")) { // 简单检查是否是 JSON 开头
            JSONObject jsonObject = new JSONObject(res);

            // 获取响应的消息字段
            String msg = jsonObject.optString("content", "No message found");

            // 根据 statusCode 进行不同的处理
            if (statusCode == 200) {
                // 正确响应，处理业务逻辑
                Log.i("ResponseHandler", "Received successful response with status code 200");
                String content2=jsonObject.getString("questions");
                String opt_num= jsonObject.getString("answer");
                JSONArray jsonArray=new JSONArray(content2);
                quesall[1]=opt_num;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    quesall[i+2] = obj.getString("question");
                    if(i==jsonArray.length()-1){
                        quesall[0]=obj.getString("id");
                    }

                    System.out.println(quesall[i]);
                }

                System.out.println(statusCode);
                return quesall;
            } else {
                // 错误处理，非 200 状态码
                Log.i("ResponseHandler", "Received error response with status code: " + statusCode);
            }
        } else {
            // 非 JSON 格式的响应
            Log.i("ResponseHandler", "Unexpected response format, not a valid JSON.");
        }
     return quesall;
    }
    //处理问卷结果
    public static String handleResponse5(String res) throws JSONException {
        String quesall="";
        if (res.trim().startsWith("{")) { // 简单检查是否是 JSON 开头
            JSONObject jsonObject = new JSONObject(res);

            // 获取响应的消息字段
            String msg = jsonObject.optString("content", "No message found");

            // 根据 statusCode 进行不同的处理
            if (statusCode == 200) {
                // 正确响应，处理业务逻辑
                Log.i("ResponseHandler", "Received successful response with status code 200");
                String content2=jsonObject.getString("result");
                quesall=content2;
                return quesall;
            } else {
                // 错误处理，非 200 状态码
                Log.i("ResponseHandler", "Received error response with status code: " + statusCode);
            }
        } else {
            // 非 JSON 格式的响应
            Log.i("ResponseHandler", "Unexpected response format, not a valid JSON.");
        }
        return quesall;
    }
}

