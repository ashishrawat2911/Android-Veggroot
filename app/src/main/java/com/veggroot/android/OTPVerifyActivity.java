package com.veggroot.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class OTPVerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        Glide.with(this).load("https://images.pexels.com/photos/1449086/pexels-photo-1449086.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
                .into((ImageView) findViewById(R.id.otpVerify));
    }
}
