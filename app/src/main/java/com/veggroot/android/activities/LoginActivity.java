package com.veggroot.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veggroot.android.R;

public class LoginActivity extends AppCompatActivity {
    EditText editText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = findViewById(R.id.phoneLogin);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainCategoryActivity.class));
            finish();
        }
    }
    public void login(View view) {
        String number = editText.getText().toString().trim();
        if (TextUtils.isEmpty(number) || number.length() < 10) {
            editText.setError("Valid number is required");
            editText.requestFocus();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("phonenumber", number);
        startActivity(intent);
        finish();
    }
}
