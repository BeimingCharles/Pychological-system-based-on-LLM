package com.example.test.fragment;
import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Data;
import com.example.test.my_tools.GraphApiService;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.test.R;
import com.example.test.my_tools.GraphRequest;
import com.example.test.my_tools.GraphResponse;
import com.example.test.my_tools.OkHttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {
    private EditText searchTermEditText;
    private Button searchButton;

    private Retrofit retrofit;
    private GraphApiService graphApiService;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.143:6671/")  // 替换为您的 Java 后端地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        graphApiService = retrofit.create(GraphApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // 获取控

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WebView webView = getView().findViewById(R.id.graphWebView);

        // 设置 WebView 支持 JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 加载 assets 目录下的 HTML 文件
        webView.loadUrl("file:///android_asset/chart.html");

        // 使用 WebViewClient 来避免链接跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient());

        // 获取控件
//        searchTermEditText = getView().findViewById(R.id.searchTerm);
//        searchButton =getView().findViewById(R.id.searchButton);
//
//        // 设置按钮点击事件
//        searchButton.setOnClickListener(v -> {
//            String searchTerm = searchTermEditText.getText().toString();
//            Log.e("sdf",searchTerm);
//            if (!searchTerm.isEmpty()) {
//               trry(searchTerm);
//              //  sendRequest(searchTerm);
//            } else {
//                Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    private void trry(String search) {
        // 使用 Executor 来处理后台线程操作
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // 使用 JSONObject 来构建 JSON 数据
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", search); // 使用 search 参数填充 name 字段
                String json = jsonObject.toString(); // 将 JSONObject 转为字符串

                // 发起 POST 请求
                String response = OkHttpUtils.postRequest(Constants.IP + "/graph_bp/get_node", json);
                GraphResponse graphResponse = GraphParser.parseGraphData(response);

                // 更新 UI，确保在主线程中调用
                runOnUiThread(() -> updateGraph(graphResponse));

                // 打印响应
                Log.i("Post", response);

            } catch (JSONException e) {
                Log.e("Post", "JSON 格式化失败", e);
            } catch (IOException e) {
                Log.e("Post", "网络请求失败", e);
            }
        });
    }

    private void updateGraph(GraphResponse graphResponse) {
        // 使用 WebView 加载图表
        WebView webView = getView().findViewById(R.id.graphWebView);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);

            // 将 graphResponse 转换为 HTML 或 JSON 数据传给 WebView
            String graphData = generateGraphHtml(graphResponse);
            Log.e("GraphData", graphData);
            webView.loadData(graphData, "text/html", "UTF-8");
        } else {
            Log.e("WebViewError", "WebView is null!");
        }
    }

    private String generateGraphHtml(GraphResponse graphResponse) {
        // 将 GraphResponse 转换为适合 D3.js 使用的 HTML 页面内容
        return "<html><body><script src='https://d3js.org/d3.v7.min.js'></script><script>" +
                "var nodes = " + new Gson().toJson(graphResponse.getNodes()) + ";" +
                "var links = " + new Gson().toJson(graphResponse.getLinks()) + ";" +
                " // D3.js 代码来可视化图形..." +
                "d3.select('body').append('svg').attr('width', 800).attr('height', 600);" + // 示例 D3.js 可视化代码
                "</script></body></html>";
    }

}
