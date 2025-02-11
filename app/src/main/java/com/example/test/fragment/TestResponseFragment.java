package com.example.test.fragment;

import static com.example.test.R.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Data;
import com.example.test.my_tools.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class TestResponseFragment extends Fragment {
    int count=0;
    int tata=0;
    int toal=0;
    public static  int  choice;//绝定进行那个问卷

  String decide[];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_response, container, false);
        ViewGroup parentLayout = (ViewGroup) view.findViewById(R.id.unique); // 获取父布局
        ScrollView scrollView1=new ScrollView(getActivity());

        if(choice==1) decide=Data.first;
        else decide=Data.second;
        int ovee= Integer.parseInt(decide[0]);
        //Dynamically generate 30 rows
        for (int i = 0; i <ovee; i++) {
           // inflating item_layout.xml，这个布局每次迭代都会复制一份
            TextView textView = new TextView(getActivity());
            RelativeLayout layout1=new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams ppp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 300);
            ppp.setMargins(0, 30, 0, 20); // Set bottom margin for spacing
            layout1.setLayoutParams(ppp);
            layout1.setId(2*i);
            //first= Data.first_questionnaire;

          //  textView.setId(View.generateViewId());  // Unique ID for each TextView
            textView.setText( decide[i+2] ); // 设置TextView的内容
            textView.setId(i); // 设置每个TextView的唯一ID
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, 70);
                        textView.setLayoutParams(textParams);
                        textView.setGravity(Gravity.CENTER);
            layout1.addView(textView); // 添加TextView到当前循环的LinearLayout中


            RadioGroup radioGroup = new RadioGroup(getActivity());
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
            RelativeLayout.LayoutParams groupParams = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 200);
            groupParams.addRule(RelativeLayout.BELOW, textView.getId());
            radioGroup.setLayoutParams(groupParams);
            int match=LinearLayout.LayoutParams.MATCH_PARENT;
            int inc = 1; //
            int a = 10;
            int click_count= Integer.parseInt(decide[1]);
            for (int j = 1 ; j <= click_count; j++) {

                RadioButton radioButton = new RadioButton(getActivity());

                // Set different width and height to simulate different sizes
                int width = 150 +inc; // Decreasing width for each button
                int height = 150 + inc; // Decreasing height for each button
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, height);

                // Set margins and gravity
                params.setMargins((1200-click_count*(150-(10+5*click_count)/2))/(click_count+1), 20, 4, 20);  // Left margin 4dp
                radioButton.setLayoutParams(params);
                radioButton.setGravity(Gravity.CENTER);  // Centered

                // Set background resource based on index
                if (click_count%2==0) {
                    if(j<=click_count/2){
                        radioButton.setBackgroundResource(R.drawable.cleckright);
                        inc-=a;
                    }
                    else{
                        radioButton.setBackgroundResource(R.drawable.cleckk);
                        inc+=a;
                    }
                    // Middle button style

                } else if (click_count%2==1) {
                    if(j==click_count/2+1){
                        inc +=a;
                        radioButton.setBackgroundResource(R.drawable.cleckmid);
                        inc -= a;
                    }
                    else if(j<=click_count/2){
                        radioButton.setBackgroundResource(R.drawable.cleckright);
                        inc-=a;
                    }
                    else {
                        radioButton.setBackgroundResource(R.drawable.cleckk);
                        inc+=a;
                    }

                }

                // Remove default circular indicator
                radioButton.setButtonDrawable(null);

                // Add RadioButton to RadioGroup
                radioGroup.addView(radioButton);
            }
            layout1.addView(radioGroup);

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                // 查找选中的 RadioButton
                View checkedRadioButton = getView().findViewById(checkedId);
                Drawable currentDrawable = checkedRadioButton.getBackground();

                // 改变选中项的背景
                if (currentDrawable.getConstantState().equals(ContextCompat.getDrawable(getActivity(), R.drawable.cleckk).getConstantState())) {
                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_left);
                } else if(currentDrawable.getConstantState().equals(ContextCompat.getDrawable(getActivity(), R.drawable.cleckmid).getConstantState())){
                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_mid);
                } else {
                    checkedRadioButton.setBackgroundResource(R.drawable.solid_hook_right);
                }

                // 更新父布局的透明度
                for (int k = 0; k < radioGroup.getChildCount(); k++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(k);
                    if (radioGroup.getVisibility() != View.GONE && radioButton.getId() == checkedId) {
                        // 强制刷新
                        radioButton.setAlpha(1f);  // 如果需要，可以调整透明度
                    }
                }

                // 计算并更新总分
                int selected = checkedId - ((click_count + 1) * (tata - 1) + 1);
                toal += selected;
                tata++;

                // 设置透明度并滚动
                RelativeLayout parentRowLayout = (RelativeLayout) (checkedRadioButton.getParent().getParent());
                for (int ii = 0; ii < parentRowLayout.getChildCount(); ii++) {
                    View childView = parentRowLayout.getChildAt(ii);
                    childView.setAlpha(0.4f);
                }

                // 自动滚动到下一个问题
                int nextPosition = tata; // 下一道题的索引
                if (nextPosition < ovee) { // 确保下一题存在
                    // 获取下一个问题的 RelativeLayout
                    RelativeLayout nextRowLayout = (RelativeLayout) parentLayout.getChildAt(nextPosition * 2);
                    Log.i("NextPosition", "Next layout: " + nextRowLayout);

                    // 执行滚动
                    scrollView1.post(() -> {
                        // 计算目标位置
                        int targetY = nextRowLayout.getTop();
                        Log.i("Scroll", "TargetY: " + targetY);  // 打印目标位置
                        scrollView1.smoothScrollTo(0, targetY); // 滚动到下一道题
                    });
                }
            });
            parentLayout.addView(layout1);

        }
      //  parentLayout.addView(layout); // 将LinearLayout添加到父布局上
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn1,btn2;
        btn1=getView().findViewById(id.first);
        btn2=getView().findViewById(id.second);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController= Navigation.findNavController(view);
                navController.navigate(id.action_testResponseFragment_to_testchoiceFragment);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) { int id=Data.id;
                Log.i("count", String.valueOf(count));
                if(count==Integer.parseInt(decide[0])){


                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("提示")
                            .setMessage("做题完毕，确定答案点击提交")
                            .setPositiveButton("提交", (dialog, which) -> {
                                // 提交按钮被点击时执行的操作
                                Log.i("Dialog", "用户点击了提交");
                                dialog.dismiss(); // 关闭弹窗
                                new Thread(() -> {
                                try {
                                    String questionname;
                                    if(choice==1)  questionname="scl-90";
                                    else questionname="SDS";
                                    String[] over;
                                    String json = "{\n" +
                                            "    \"id\":\"" + 1 + "\",\n" +
                                            "    \"questionnairename\":\"" +questionname + "\",\n" +
                                            "    \"score\":\"" + toal + "\"\n" +
                                            "}";
                                    // 发起 POST 请求
                                    String response = OkHttpUtils.postRequest(Constants.IP + "/qa_result_bp/get_result", json);
                                    String result=OkHttpUtils.handleResponse5(response);
                                    Log.i("hhh",result);
                                    if(choice==1)
                                        QuestionnaireResultFragment.over[0]=result;
                                    else QuestionnaireResultFragment.over[1]=result;
                                    Log.i("hhh",QuestionnaireResultFragment.over[0]);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Activity activity = getActivity();
                                    if (activity != null) {
                                        activity.runOnUiThread(() ->
                                                Toast.makeText(getContext(), Constants.NETERR, Toast.LENGTH_SHORT).show()
                                        );
                                    }

                                }
                            }).start();
                                NavController navController= Navigation.findNavController(view);
                                navController.navigate(R.id.action_testResponseFragment_to_testchoiceFragment);
                            })
                            .setNegativeButton("取消", (dialog, which) -> {

                                dialog.dismiss(); // 关闭弹窗
                            })
                            .show();

                }
                else{
                    Log.i("notok","noover");
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("警告")
                            .setMessage("未完成题目，请做题完毕后提交")
                            .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
                            .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                            .show();
                }


            }
        });
    }
}