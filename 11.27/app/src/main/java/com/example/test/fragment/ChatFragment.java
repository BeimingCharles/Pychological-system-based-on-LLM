package com.example.test.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Database.ChatRecord;
import com.example.test.Database.ChatRecordDatabase;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;


import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Message;
import com.example.test.adapter.MessageAdapter;
import com.example.test.my_tools.OkHttpUtils;
import com.example.test.Database.ChatRecordDatabase;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messgaeList = new ArrayList<>();  // 直接在声明时初始化
    MessageAdapter messageAdapter;
    ChatRecordDatabase DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 初始化视图
        recyclerView = view.findViewById(R.id.recycler_view);
        welcomeTextView = view.findViewById(R.id.welcome_text);
        sendButton = view.findViewById(R.id.ButtonSend);
        messageEditText = view.findViewById(R.id.message_edit_text);

        // 配置 RecyclerView
        recyclerView.setNestedScrollingEnabled(false);
        messageAdapter = new MessageAdapter(messgaeList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        //写入本地数据库
        DB = Room.databaseBuilder(requireContext(), ChatRecordDatabase.class, "peopleDB")
                // 默认不允许在主线程中连接数据库   强制在主线程中处理
                .allowMainThreadQueries()
                .build();

        // 设置点击事件
        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();


            // 添加用户消息到聊天
            addToChat(question, Message.SENT_BY_ME);

            // 清空输入框
            messageEditText.setText("");

            // 隐藏欢迎文本
            welcomeTextView.setVisibility(View.GONE);

            // 网络请求线程
            new Thread(() -> {
                ChatRecord chatRecord1=new ChatRecord(question);
                DB.chatRecordDao().insertDataOne(chatRecord1);

                String get_content;
                try {
                    String json = "{\n" +
                            "    \"content\":\"" + question + "\"\n" +
                            "}";
                    System.out.println("post Json" + json);
                    String response = OkHttpUtils.postRequest(Constants.IP + "/chat_bp/formal_chat", json);
                    System.out.println("post Response" + response);

                    get_content = OkHttpUtils.handleResponse2(response);
                    System.out.println(get_content);
                    requireActivity().runOnUiThread(() -> addToChat(get_content, Message.SENT_BY_BOT));


                    ChatRecord chatRecord2=new ChatRecord(get_content);
                    DB.chatRecordDao().insertDataOne(chatRecord2);

                    //selectData(null);

                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), Constants.NETERR, Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        return view;
    }

    void addToChat(String message, String sentBy) {
        requireActivity().runOnUiThread(() -> {
            messgaeList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }
}
