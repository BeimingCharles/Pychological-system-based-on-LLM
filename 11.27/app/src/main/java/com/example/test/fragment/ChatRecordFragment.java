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
        // Inflate the layout for this fragment
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
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(false);
        recyclerView.setLayoutManager(llm);

        DB = Room.databaseBuilder(requireContext(), ChatRecordDatabase.class, "peopleDB")
                // 默认不允许在主线程中连接数据库   强制在主线程中处理
                .allowMainThreadQueries()
                .build();

        List<ChatRecord> chatRecords=DB.chatRecordDao().getChatRecords();
        for(int f=1;f<=chatRecords.size();f++){
            ChatRecord chatRecord= DB.chatRecordDao().getChatRecord(f);
            addResponse(chatRecord.toString(),f%2-1);
        }


      /*  for(ChatRecord cr:chatRecords){

            Log.d("SettingFragment", "ChatRecord: " + cr.toString());

            addResponse(chatRecords.toString(),1);

        }
        addResponse("sdfs",1);*/

      //  Iterator<Message> iterator = hhh.iterator();
     /*   int pd=0;
        while (iterator.hasNext()) {
            Message element = iterator.next();
            // 打印或处理元素
            String aaa=element.getMessage();

            addResponse(aaa,pd%2);
            pd++;


            // 添加后整体更新
            messageAdapter.notifyItemRangeInserted(0, messgaeList.size());
        }*/
    }

    void addResponse(String response,int a) {
        //  messgaeList.remove(messgaeList.size());
        if(a==1)
            addToChat(response, Message.SENT_BY_BOT);
        else
            addToChat(response,Message.SENT_BY_ME);
    }

    void addToChat(String message, String sentBy) {
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                messgaeList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
}