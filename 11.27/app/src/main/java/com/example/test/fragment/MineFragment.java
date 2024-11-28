package com.example.test.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;

import java.io.InputStream;

public class MineFragment extends Fragment {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 2910;
    private ImageView imageSelected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_setting,btn_chat_record;
        btn_setting=getView().findViewById(R.id.five);
        btn_chat_record=getView().findViewById(R.id.ButtonChatRecord);


        TextView username = view.findViewById(R.id.username);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("修改用户名");

                // 创建一个 EditText，用户输入新用户名
                final EditText input = new EditText(requireContext());
                input.setHint("请输入新用户名");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // 设置对话框的按钮和点击事件
                builder.setPositiveButton("确定", (dialog, which) -> {
                    String newUsername = input.getText().toString().trim();
                    if (!newUsername.isEmpty()) {
                        // 更新 TextView 文本
                        username.setText(newUsername);

                        // 保存用户名到 SharedPreferences
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", newUsername);
                        editor.apply();

                        Toast.makeText(requireContext(), "用户名已更新", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }
                });

                // 添加取消按钮
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

                // 显示对话框
                builder.show();
            }
        });


        imageSelected = view.findViewById(R.id.picture);
        imageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileOperations();
            }
        });
        btn_chat_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_mineFragment_to_chatRecordFragment);
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_mineFragment_to_settingFragment);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!hasManageExternalStoragePermission()) {
                requestManageExternalStoragePermission();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+
            checkAndRequestMediaPermissions();
        }
    }

    // 检查是否具有 MANAGE_EXTERNAL_STORAGE 权限
    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean hasManageExternalStoragePermission() {
        return Environment.isExternalStorageManager();
    }

    // 请求 MANAGE_EXTERNAL_STORAGE 权限
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestManageExternalStoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + requireContext().getPackageName()));
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkAndRequestMediaPermissions() {
        boolean needsRequest = false;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            needsRequest = true;
            Log.d(TAG, "checkAndRequestMediaPermissions: nned READ_MEDIA_IMAGES");
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            needsRequest = true;
            Log.d(TAG, "checkAndRequestMediaPermissions: nned READ_MEDIA_VIDEO");
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            needsRequest = true;
            Log.d(TAG, "checkAndRequestMediaPermissions: nned READ_MEDIA_AUDIO");
        }

        if (needsRequest) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            }, MULTIPLE_PERMISSIONS_REQUEST_CODE);
        } else {
        }
    }
    private void performFileOperations() {
        // 执行媒体文件访问逻辑
        selectPicture();
    }
    private void selectPicture() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        } catch (SecurityException e) {
            Log.e("SelectPicture", "安全异常: 可能是权限不足导致无法获取图片Uri。" + e.getMessage());
            Toast.makeText(getContext(), "安全异常: 无法获取图片Uri，请检查权限设置", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SelectPicture", "获取图片Uri时出错: " + e.getMessage());
            Toast.makeText(getContext(), "获取图片失败，请稍后重试或检查设备存储", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == requireActivity().RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageSelected.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "加载图片失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}

























