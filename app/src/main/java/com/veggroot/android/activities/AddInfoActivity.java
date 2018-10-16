package com.veggroot.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veggroot.android.R;

public class AddInfoActivity extends AppCompatActivity {
    EditText name, email, address, pincode;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        setTitle("Add Personal Information");
        setFindViewById();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


    }

    private void setFindViewById() {
        name = findViewById(R.id.addInfoName);
        email = findViewById(R.id.addInfoEmail);
        address = findViewById(R.id.addInfoAddress);
        pincode = findViewById(R.id.addInfoPincode);

    }

    public void saveDetails(View view) {
        saveToFirebaseDatabase(
                name.getText().toString(),
                email.getText().toString(),
                address.getText().toString(),
                pincode.getText().toString()
        );
    }

    private void saveToFirebaseDatabase(String name, String email, String address, String pincode) {
        mDatabase.child("user").child(mAuth.getUid()).child("info").child("name").setValue(name);
        mDatabase.child("user").child(mAuth.getUid()).child("info").child("email").setValue(email);
        mDatabase.child("user").child(mAuth.getUid()).child("info").child("address").setValue(address);
        mDatabase.child("user").child(mAuth.getUid()).child("info").child("pincode").setValue(pincode);
        mDatabase.child("user").child(mAuth.getUid()).child("info").child("phone").setValue(mAuth.getCurrentUser().getPhoneNumber());
        startActivity(new Intent(AddInfoActivity.this, MainCategoryActivity.class));
        Toast.makeText(this, "Details has been Saved", Toast.LENGTH_SHORT).show();
    }
}
