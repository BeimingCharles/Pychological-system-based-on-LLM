package com.example.test.my_tools;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.test.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.internal.cache2.Relay;

public class ControlPicture extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_item);
            ImageView leftImage = findViewById(R.id.leftimage);
            Log.i("pic","did it now");
            if (leftImage != null) {
                leftImage.setBackground(Drawable.createFromPath("@drawable/key"));
            }

    }


}
