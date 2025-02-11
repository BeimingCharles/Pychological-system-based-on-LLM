package com.example.test.fragment;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Database.ChatRecord;
import com.example.test.Database.ChatRecordDatabase;
import com.example.test.LoadingActivity;
import com.example.test.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Data;
import com.example.test.my_tools.Message;
import com.example.test.adapter.MessageAdapter;
import com.example.test.my_tools.OkHttpUtils;
import com.example.test.Database.ChatRecordDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messgaeList = new ArrayList<>();  // 直接在声明时初始化
    MessageAdapter messageAdapter;
    ChatRecordDatabase DB;
    BottomNavigationView bottomNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 初始化视图
        recyclerView = view.findViewById(R.id.recycler_view);
        welcomeTextView = view.findViewById(R.id.welcome_text);
        sendButton = view.findViewById(R.id.ButtonSend);
        messageEditText = view.findViewById(R.id.message_edit_text);

        // 初始化 DrawerLayout
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        View navView = view.findViewById(R.id.nav_view);

        // 设置抽屉菜单点击事件
        TextView menuItem1 = navView.findViewById(R.id.menu_item_1);
        TextView menuItem2 = navView.findViewById(R.id.menu_item_2);

        menuItem1.setOnClickListener(v -> {
            Toast.makeText(getContext(), "进入陪聊记录", Toast.LENGTH_SHORT).show();
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.action_chatFragment_to_chatRecordFragment);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        menuItem2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "进入测评结果", Toast.LENGTH_SHORT).show();
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.action_chatFragment_to_questionnaireresultFragment);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // 配置 RecyclerView
        recyclerView.setNestedScrollingEnabled(false);
        messageAdapter = new MessageAdapter(messgaeList);
        recyclerView.setAdapter(messageAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        // 数据库配置
        DB = Room.databaseBuilder(requireContext(), ChatRecordDatabase.class, "peopleDB")
                .allowMainThreadQueries()
                .build();

        // 设置发送按钮点击事件
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
                ChatRecord chatRecord1 = new ChatRecord(question);
                DB.chatRecordDao().insertDataOne(chatRecord1);

                try {
                    String json = "{ \"content\":\"" + question + "\" }";
                    String response = OkHttpUtils.postRequest(Constants.IP + "/chat_bp/formal_chat", json);
                    String get_content = OkHttpUtils.handleResponse2(response);
                    requireActivity().runOnUiThread(() -> addToChat(get_content, Message.SENT_BY_BOT));

                    ChatRecord chatRecord2 = new ChatRecord(get_content);
                    DB.chatRecordDao().insertDataOne(chatRecord2);

                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), Constants.NETERR, Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        // 设置左上角图标点击事件
        view.findViewById(R.id.menu_icon).setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
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
