package com.example.test.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.my_tools.Data;

import java.util.Arrays;
public class TestChoiceFragment extends Fragment {


    String[][] str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_choice2, container, false);
        TextView name = view.findViewById(R.id.name);
        TextView jkzt = view.findViewById(R.id.jkzt);
        TextView hhh = view.findViewById(R.id.hhh);
        TextView jkz = view.findViewById(R.id.jkz);

        // 获取 ViewModel 实例
        // 获取 Application 范围的 ViewModel



        str=Data.str;


        // 确保 LiveData 更新时使用 postValue 或 setValue（需要在主线程）
        //myViewModel.postQaData(new String[][]{{"Name", "Value1"}, {"Status", "Value2"}});

// 确保观察 LiveData

            if (str != null && str.length > 0) {
                Log.i("LiveData Update", "Data updated in LiveData.");
                name.setText(str[0][0]);
                jkzt.setText(str[1][0]);
                hhh.setText(str[0][1]);
                jkz.setText(str[1][1]);
            } else {
                Log.i("LiveData Update", "Received invalid data in LiveData.");
            }



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView1,textView2;
        textView1=getView().findViewById(R.id.jkzt);
        textView2=getView().findViewById(R.id.jkz);

       textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController= Navigation.findNavController(view);
                TestResponseFragment.choice=1;
                navController.navigate(R.id.action_testchoiceFragment_to_testResponseFragment);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestResponseFragment.choice=2;
                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_testchoiceFragment_to_testResponseFragment);
            }
        });
    }
}