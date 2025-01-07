package com.example.test.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.OkHttpUtils;
import com.example.test.my_tools.Data;

import org.json.JSONObject;

public class LoginFragment extends Fragment {

    SharedPreferences preference;
    EditText et_account;
    EditText et_pwd;
    CheckBox cb_check;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 确保在这里初始化 preference
        preference = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);


        et_account = view.findViewById(R.id.editTextAccount);
        et_pwd = view.findViewById(R.id.editTextPassword);
        cb_check = view.findViewById(R.id.cb_check);

        reload();

        TextView mtv = view.findViewById(R.id.textView7);
        mtv.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_loginFragment_to_registerFragment);
        });

        cb_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 选中状态
                Log.d("CheckBox", "CheckBox is checked");
            } else {
                // 未选中状态
                Log.d("CheckBox", "CheckBox is unchecked");
            }
        });


        Button btn = view.findViewById(R.id.buttonLogin);
        btn.setOnClickListener(v -> {
            String username = et_account.getText().toString();
            String password = et_pwd.getText().toString();
            SharedPreferences.Editor editor = preference.edit();

            if (cb_check.isChecked()) {

                editor.putString("account", username);
                editor.putString("password", password);
                editor.putBoolean("isRemember", cb_check.isChecked());
                editor.apply(); // 数据异步保存

                // 读取刚刚保存的数据并打印日志
                String savedAccount = preference.getString("account", "未找到账户");
                String savedPassword = preference.getString("password", "未找到密码");
                boolean isRememberSaved = preference.getBoolean("isRemember", false);

                Log.i("成功存入!!", "账号: " + savedAccount + "，密码: " + savedPassword + "，记住密码: " + isRememberSaved);
            }else{
                editor.putBoolean("isRemember", cb_check.isChecked());
                editor.apply();
            }


            new Thread(() -> {
                try {
                    OkHttpUtils.logParams(username, password);
                    String json = "{\n" +
                            "    \"username\":\"" + username + "\",\n" +
                            "    \"password\":\"" + password + "\"\n" +
                            "}";
                    Data.username = username;
                    System.out.println("post Json" + json);

                    String response = OkHttpUtils.postRequest(Constants.IP + "/auth_bp/login", json);
                    String str = OkHttpUtils.handleResponse(response, getActivity(), true, MainActivity.class);
                    System.out.println(str);
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), Constants.NETERR, Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;
    }

    private void reload() {
          // 确保 preference 已经被正确初始化
        if (preference != null) {
            boolean isRemember = preference.getBoolean("isRemember", false);
            String account = preference.getString("account", "");
            et_account.setText(account);
            if (isRemember) {
                String pwd = preference.getString("password", "");
                et_pwd.setText(pwd);
                cb_check.setChecked(true);
            }else {
                //清空密码部分
                et_pwd.setText("");
                cb_check.setChecked(false);
            }
        } else {
            Log.e("LoginFragment", "SharedPreferences is not initialized.");
        }
    }
}

