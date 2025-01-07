package com.example.test.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.Database.ChatRecord;
import com.example.test.Database.ChatRecordDatabase;
import com.example.test.R;
import com.example.test.adapter.MessageAdapter;
import com.example.test.my_tools.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatRecordFragment extends Fragment {

    RecyclerView recyclerView;
 //   List<Message> hhh=ChatFragment.messgaeList;
    List<Message> messgaeList;
    MessageAdapter messageAdapter;
    ChatRecordDatabase DB;

    public ChatRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomNavigationView bottomHostView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomHostView.setVisibility(View.GONE);  // 使用 Java 语法
        return inflater.inflate(R.layout.fragment_chat_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messgaeList = new ArrayList<>();
        messgaeList.clear();
        recyclerView = getView().findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        messageAdapter = new MessageAdapter(messgaeList);
        recyclerView.setAdapter(messageAdapter);

        // 使列表从顶部显示，设置stackFromEnd为true
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(false);  // 设置为false，让新的消息出现在顶部
        recyclerView.setLayoutManager(llm);

        DB = Room.databaseBuilder(requireContext(), ChatRecordDatabase.class, "peopleDB")
                .allowMainThreadQueries()
                .build();

        List<ChatRecord> chatRecords = DB.chatRecordDao().getChatRecords();
        for (int f =chatRecords.size() ; f >= 1; f--) {
            ChatRecord chatRecord = DB.chatRecordDao().getChatRecord(f);
            addResponse(chatRecord.toString(), (f + 1) % 2);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 恢复底部导航栏可见性
        BottomNavigationView bottomHostView = requireActivity().findViewById(R.id.bottomNavigationView);
        if (bottomHostView != null) {
            bottomHostView.setVisibility(View.VISIBLE);
        }
    }

    void addResponse(String response, int a) {
        if (a == 1)
            addToChat(response, Message.SENT_BY_BOT);
        else
            addToChat(response, Message.SENT_BY_ME);
    }

    void addToChat(String message, String sentBy) {
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                // 新的消息添加到顶部
                messgaeList.add(0, new Message(message, sentBy));  // 使用add(0, ...) 将消息放到顶部
                messageAdapter.notifyDataSetChanged();  // 通知Adapter数据已更新
                recyclerView.smoothScrollToPosition(0);  // 滚动到顶部
            }
        });
    }
}