
package com.example.test.my_tools;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GraphApiService {

    // 定义一个 POST 请求，向后端发送请求
    @POST("/graph_bp/get_node")
    Call<GraphResponse> generateGraph(@Body GraphRequest requestBody);
}
