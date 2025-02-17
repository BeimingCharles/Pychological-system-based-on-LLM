package com.example.test.fragment;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.test.R;
import com.example.test.adapter.LooperPagerAdapter;
import com.example.test.my_tools.Constants;
import com.example.test.my_tools.Data;
import com.example.test.my_tools.MyViewPager;
import com.example.test.my_tools.OkHttpUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class HomePageFragment extends Fragment implements MyViewPager.OnViewPagerTouchListener {

    private MyViewPager mLoopPager;
    private LooperPagerAdapter mLooperPagerAdapter;
    private Handler mHandler;
    private boolean mIsTouch = false;

    private static List<Integer> sPics = new ArrayList<>();

    static {
        sPics.add(R.mipmap.image1);
        sPics.add(R.mipmap.image2);
        sPics.add(R.mipmap.image3);
        sPics.add(R.mipmap.image4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        InitView(view);
//        getquestion1();
//        getquestion2();
        getProfile();
        return view;
    }

    private void getProfile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = "{\n" +
                        "    \"id\":\"" + Data.id + "\"\n" +
                        "}";
                try {
                    // 发送请求到后端并获取响应
                    Response response = OkHttpUtils.postRequest2(Constants.IP + "/profile_bp/get_profile", json);

                    // 获取响应的字节流
                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();

                        // 将字节流转换为 Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Data.setProfileImage(bitmap);

                    } else {
                        Log.e("Profile", "图片获取失败: " + response.code());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("获取用户图片失败", e);
                }
            }
        }).start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mHandler = new Handler();
        mHandler.post(mLooperTask);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(mLooperTask);
    }

    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if (!mIsTouch) {
                int currentItem = mLoopPager.getCurrentItem();
                mLoopPager.setCurrentItem(++currentItem, false);

            }
            mHandler.postDelayed(this, 3000); // 每3秒切换一次
        }
    };

    private void InitView(View view) {
        mLoopPager = view.findViewById(R.id.looper_pager);
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        mLoopPager.setAdapter(mLooperPagerAdapter);
        mLoopPager.setCurrentItem(mLooperPagerAdapter.getDataRealSize() * 100, false);
        mLoopPager.setOnViewPagerTouchListener(this); // 设置触摸事件监听器
    }

    // 实现 OnViewPagerTouchListener 接口方法
    @Override
    public void onPageTouched(boolean isTouch) {
        mIsTouch = isTouch; // 更新触摸状态
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ll_chat_record, ll_article, ll_chat, ll_mentation;
        ll_chat_record = getView().findViewById(R.id.ll_chat_record);
        ll_article = getView().findViewById(R.id.ll_article);
        ll_chat = getView().findViewById(R.id.ll_chat);
        ll_mentation = getView().findViewById(R.id.ll_mentation);

//        ll_chat_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.action_homePageFragment_to_chatRecordFragment);
//            }
//        });
//        ll_mentation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.action_homePageFragment_to_questionnaireresultFragment);
//            }
//        });
//
//        ll_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.action_homePageFragment_to_chatFragment);
//            }
//        });
    }
}
