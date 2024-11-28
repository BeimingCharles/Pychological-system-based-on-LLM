package com.example.test.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestChoiceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String[][] str;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TestChoiceFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestChoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestChoiceFragment newInstance(String param1, String param2) {
        TestChoiceFragment fragment = new TestChoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("sd","dsfa");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_choice2, container, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView jkzt=(TextView) view.findViewById(R.id.jkzt);
        TextView hhh=(TextView)view.findViewById(R.id.hhh);
        TextView jkz=(TextView)view.findViewById(R.id.jkz);
      //  System.out.println(textView.getText().toString());
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
       Log.i("d","sdfsdfsa");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // 发起 POST 请求
                    String response = OkHttpUtils.getRequest(Constants.IP + "/qa_bp/get_all_qa");

                    // System.out.println("get Response" + response);
                    Log.i("Response", response);
                    // 处理响应


                    str=OkHttpUtils.handleResponse3(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<=1;i++){
                                Log.i("烦烦烦",str[0][0]);
                                for(int j=0;j<=1;j++){
                                    name.setText(str[0][0]);
                                    jkzt.setText(str[1][0
                                            ]);
                                    hhh.setText(str[0][1]);
                                    jkz.setText(str[1][1]);
                                }

                            }
                        }
                    });




                    System.out.println(str);

                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), Constants.NETERR, Toast.LENGTH_SHORT).show());
                }

            }
        }).start();


        // Now find the TextView in the inflated view



        return view;
    }



}