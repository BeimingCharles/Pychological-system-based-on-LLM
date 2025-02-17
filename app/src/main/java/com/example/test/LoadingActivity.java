package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.fragment.TestResponseFragment;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Data;
import com.example.test.my_tools.OkHttpUtils;

import org.json.JSONException;

import java.io.IOException;

public class LoadingActivity extends AppCompatActivity {
    Handler mHandler = new Handler();
    String [][] questionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        questionnaire=Data.str;
        // 获取 ViewModel 实例
        getquestion1();
        getquestion2();
        getquestion3();

        // 发起网络请求
        getTestChoiceData();
        //getquestion();

        // 延迟跳转到 LoginActivity
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish(); // 销毁欢迎页面
        }, 3000); // 3秒后跳转
    }



    private void getTestChoiceData() {

        new Thread(() -> {
            try {
                String response = OkHttpUtils.getRequest(Constants.IP + "/qa_bp/get_all_qa");
               // String imageresponse=OkHttpUtils.getRequest(Constants.IP+"/image_bp/upload");

                Log.i("Response", response);
                // 假设 handleResponse3 解析返回数据为二维数组
                String[][] str = OkHttpUtils.handleResponse3(response);
                if (str != null && str.length > 0 && str[0].length > 0) {
                    Log.i("Array Dimensions", "Rows: " + str.length + ", Columns: " + str[0].length);

                    // 逐行逐列输出数组的内容
                    for (int i = 0; i < str.length; i++) {
                        for (int j = 0; j < str[i].length; j++) {
                            Log.i("Array Value", "str[" + i + "][" + j + "]: " + str[i][j]);
                        }
                    }

                    // 在主线程中更新 ViewModel 数据
                    runOnUiThread(() -> {
                        Data data = new Data();
                        data.setStr(str);
                        Log.i("ViewModel Updated", "Data has been updated in ViewModel.");
                    });
                } else {
                    Log.e("Response Error", "Invalid or empty response received.");
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(LoadingActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start(); // 启动线程

    }
    private void getquestion1() {
        new Thread(() -> {
            try {
                String[] over;
                String json = "{\n" +
                        "    \"questionnaire_name\":\"" + "scl-90" + "\"\n" +
                        "}";

                // 发起 POST 请求
                String response = OkHttpUtils.postRequest(Constants.IP + "/qa_bp/get_qa", json);

                // 打印响应
                Log.i("Post", response);

                // 处理响应
                over = OkHttpUtils.handleResponse4(response);
                Data.first=over;
//                for (int k = 0; k < over.length; k++)
//                    System.out.println(over[k]);


            } catch (IOException | JSONException e) {
                e.printStackTrace();

            }
        }).start();
    }
    private void getquestion2() {
        new Thread(() -> {
            try {
                String[] over;
                String json = "{\n" +
                        "    \"questionnaire_name\":\"" + "SDS" + "\"\n" +
                        "}";

                // 发起 POST 请求
                String response = OkHttpUtils.postRequest(Constants.IP + "/qa_bp/get_qa", json);

                // 打印响应
                Log.i("Post", response);

                // 处理响应
                over = OkHttpUtils.handleResponse4(response);
                Data.second=over;
//                for (int k = 0; k < over.length; k++)
//                    System.out.println(over[k]);

                // 在主线程更新 UI

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void getquestion3() {
        new Thread(() -> {
            try {
                String[] over;
                String json = "{\n" +
                        "    \"questionnaire_name\":\"" + "EPQ" + "\"\n" +
                        "}";

                // 发起 POST 请求
                String response = OkHttpUtils.postRequest(Constants.IP + "/qa_bp/get_qa", json);

                // 打印响应
                Log.i("Post", response);

                // 处理响应
                over = OkHttpUtils.handleResponse4(response);
                Data.second=over;
//                for (int k = 0; k < over.length; k++)
//                    System.out.println(over[k]);

                // 在主线程更新 UI

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}


