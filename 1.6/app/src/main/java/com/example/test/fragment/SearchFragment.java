package com.example.test.fragment;
import com.example.test.my_tools.GraphApiService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.test.R;
import com.example.test.my_tools.GraphRequest;
import com.example.test.my_tools.GraphResponse;
import com.google.gson.Gson;

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
                .baseUrl("http://localhost:8080/")  // 替换为您的 Java 后端地址
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


        // 获取控件
        searchTermEditText = getView().findViewById(R.id.searchTerm);
        searchButton =getView().findViewById(R.id.searchButton);

        // 设置按钮点击事件
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchTermEditText.getText().toString();
            if (!searchTerm.isEmpty()) {
                sendRequest(searchTerm);
            } else {
                Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendRequest(String searchTerm) {
        GraphRequest request = new GraphRequest(searchTerm);
        Call<GraphResponse> call = graphApiService.generateGraph(request);

        call.enqueue(new Callback<GraphResponse>() {
            @Override
            public void onResponse(Call<GraphResponse> call, Response<GraphResponse> response) {
                if (response.isSuccessful()) {
                    GraphResponse graphResponse = response.body();
                    // 在此处调用方法来更新图表（如 D3.js）
                    updateGraph(graphResponse);
                } else {
                    Toast.makeText(getContext(), "Failed to get data from the server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GraphResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGraph(GraphResponse graphResponse) {
        // 使用 WebView 加载图表
        WebView webView = getView().findViewById(R.id.graphWebView);
        webView.getSettings().setJavaScriptEnabled(true);

        // 将 graphResponse 转换为 HTML 或 JSON 数据传给 WebView
        String graphData = generateGraphHtml(graphResponse);
        webView.loadData(graphData, "text/html", "UTF-8");
    }
    private String generateGraphHtml(GraphResponse graphResponse) {
        // 将 GraphResponse 转换为适合 D3.js 使用的 HTML 页面内容
        // 可以构造一个 HTML 字符串并将其嵌入 WebView
        return "<html><body><script src='https://d3js.org/d3.v7.min.js'></script><script>" +
                "var nodes = " + new Gson().toJson(graphResponse.getNodes()) + ";" +
                "var links = " + new Gson().toJson(graphResponse.getLinks()) + ";" +
                " // D3.js 代码来可视化图形..." +
                "</script></body></html>";
    }
}
