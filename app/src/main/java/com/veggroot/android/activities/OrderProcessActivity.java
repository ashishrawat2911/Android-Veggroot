package com.veggroot.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.R;
import com.veggroot.android.model.Cart;
import com.veggroot.android.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessActivity extends AppCompatActivity {
    Integer noOfItems;
    Double totalCost;
    TextView priceNoOfItems, cost, finalCost;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    List<Cart> cartList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Intent i = getIntent();
        totalCost = i.getDoubleExtra(Constants.TOTAL_ITEM_COST, 0D);
        noOfItems = i.getIntExtra(Constants.TOTAL_ITEMS, 0);
        priceNoOfItems = findViewById(R.id.priceNoOfItems);
        cost = findViewById(R.id.orderPrice);
        finalCost = findViewById(R.id.orderTotalValue);

        priceNoOfItems.setText("Price ( " + noOfItems + "items )");
        cost.setText("Rs " + totalCost);
        finalCost.setText("Rs " + totalCost);

    }

    public void confirmOrder(View view) {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Cart cart = dataSnapshot1.getValue(Cart.class);
                    cartList.add(cart);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
