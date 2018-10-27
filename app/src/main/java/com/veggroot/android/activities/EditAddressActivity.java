package com.veggroot.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veggroot.android.R;

public class EditAddressActivity extends AppCompatActivity {
    EditText address, pincode;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        address = findViewById(R.id.editAddress);
        pincode = findViewById(R.id.editPinCode);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void saveDetails(View view) {
        if (checkValidation()) {
            mDatabase
                    .child("user")
                    .child(mAuth.getUid())
                    .child("info")
                    .child("address")
                    .setValue(address.getText().toString().trim());
            mDatabase
                    .child("user")
                    .child(mAuth.getUid())
                    .child("info")
                    .child("pincode")
                    .setValue(pincode.getText().toString().trim());
            finish();
        }

    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(address.getText().toString())) {
            address.setError("Field is empty");
            return false;
        } else if (TextUtils.isEmpty(pincode.getText().toString())) {
            pincode.setError("Field is empty");
            return false;
        } else if (TextUtils.isEmpty(address.getText().toString()) && TextUtils.isEmpty(pincode.getText().toString())) {
            address.setError("Field is empty");
            pincode.setError("Field is empty");
            return false;
        } else {
            return true;
        }
    }
}
