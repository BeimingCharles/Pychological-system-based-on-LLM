package com.example.test.my_tools;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphGenerator {

    private static Context context;

    // 构造方法接收 Context 对象
    public GraphGenerator(Context context) {
        this.context = context;
    }

    public static void use(String args) {
        // 定义 JSON 数据
        try {
            // 解析 JSON 数据
            JSONObject jsonObject = new JSONObject(args);
            JSONArray nodes = jsonObject.getJSONArray("nodes");
            JSONArray links = jsonObject.getJSONArray("links");

            // 构建 JSON 字符串
            StringBuilder graphData = new StringBuilder("{\"nodes\": [");

            // 节点数据
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject node = nodes.getJSONObject(i);
                graphData.append(String.format("{\"id\": %d, \"name\": \"%s\"}",
                        node.getInt("id"), node.getString("name")));
                if (i < nodes.length() - 1) {
                    graphData.append(", ");
                }
            }
            graphData.append("], \"links\": [");

            // 边数据
            for (int i = 0; i < links.length(); i++) {
                JSONObject link = links.getJSONObject(i);
                graphData.append(String.format("{\"source\": %d, \"target\": %d}",
                        link.getInt("source"), link.getInt("target")));
                if (i < links.length() - 1) {
                    graphData.append(", ");
                }
            }
            graphData.append("]}");

            // 使用 context.getAssets().open() 访问 assets 文件夹中的 chart.html
            try {
                InputStream is = context.getAssets().open("chart.html");

                // 使用 BufferedReader 读取文件内容
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                // 逐行读取文件并构建字符串
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // 关闭 reader
                reader.close();

                // 将文件内容转换为字符串
                String template = stringBuilder.toString();

                // 读取 HTML 模板并插入 JSON 数据
                template = template.replace("{{data}}", graphData.toString());

                // 将结果写入 chart_over.html
                // 获取应用的内部存储路径
                File file = new File(context.getFilesDir(), "chart_over.html");

                // 使用 FileOutputStream 写入文件
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    fileOutputStream.write(template.getBytes());
                    fileOutputStream.flush(); // 确保写入
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("HTML 文件生成成功！");
            } catch (IOException e) {
                e.printStackTrace(); // 处理异常
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
