package com.example.test.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.Database.ChatRecord;
import com.example.test.Database.ChatRecordDatabase;
import com.example.test.R;

import java.util.List;

public class SettingFragment extends Fragment {
    ChatRecordDatabase DB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn,btn2;
        btn=getView().findViewById(R.id.seven);
        btn2=getView().findViewById(R.id.second);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_mineFragment_to_settingFragment);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            NavController navController=Navigation.findNavController(view);
            navController.navigate(R.id.action_settingFragment_to_aboutAppFragment);



//                // 删除记录
//                for(int i=2;i<=10;i++){
//                    DB.chatRecordDao().deleteById(i);
//                }
//                int rowsDeleted = DB.chatRecordDao().deleteById(1);
//                if (rowsDeleted > 0) {
//                    Toast.makeText(getContext(), "删除成功: ID " + 1, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}





























