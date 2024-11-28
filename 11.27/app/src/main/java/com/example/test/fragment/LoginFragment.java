package com.example.test.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView mtv;
        mtv=getView().findViewById(R.id.textView7);
        mtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller= Navigation.findNavController(view);
                controller.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        Button btn;
        EditText et_account;
        et_account=getView().findViewById(R.id.editTextAccount);
        EditText et_pwd;
        et_pwd=getView().findViewById(R.id.editTextPassword);

        btn=getView().findViewById(R.id.buttonLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = et_account.getText().toString();
                String password = et_pwd.getText().toString();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpUtils.logParams(username, password);

                            String json = "{\n" +
                                    "    \"username\":\"" + username + "\",\n" +
                                    "    \"password\":\"" + password + "\"\n" +
                                    "}";

                            // 创建 http 客户端
//                        OkHttpClient client = new OkHttpClient();
                            System.out.println("post Json"+ json);
                            // 创建 http 请求
                            // 发起 POST 请求
                            String response = OkHttpUtils.postRequest(Constants.IP + "/auth_bp/login", json);

                            System.out.println("post Response" + response);
                            Log.i("Response", response);
                            // 处理响应
                            String str=OkHttpUtils.handleResponse(response, getActivity(),true, MainActivity.class);
                            System.out.println(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), Constants.NETERR, Toast.LENGTH_SHORT).show());
                        }
                    }
                }).start();


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}