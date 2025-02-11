package com.example.test.fragment;

import com.example.test.my_tools.GraphResponse;
import com.google.gson.Gson;

public class GraphParser {
    public static GraphResponse parseGraphData(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GraphResponse.class);
    }
}
