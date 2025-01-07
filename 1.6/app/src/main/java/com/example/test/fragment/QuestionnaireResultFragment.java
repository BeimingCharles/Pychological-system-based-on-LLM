package com.example.test.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class QuestionnaireResultFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static  String over[]=new String[10];


    public QuestionnaireResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BottomNavigationView bottomHostView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomHostView.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_questionnaire_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);  TextView one = null;
        if(over!=null){
            for(int k=0;k<over.length;k++){
                if(over[k]!=null&&k==0){
                    one=getView().findViewById(R.id.scl90result);
                    one.setText(over[k]);
                }
                else if(over[k]!=null&&k==1){
                    one=getView().findViewById(R.id.SDSresult);
                    one.setText(over[k]);
                }
            }
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
}