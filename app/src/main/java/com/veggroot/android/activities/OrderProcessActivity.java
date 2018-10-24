package com.veggroot.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.R;
import com.veggroot.android.adaptor.CartAdaptor;
import com.veggroot.android.model.Cart;
import com.veggroot.android.model.Order;
import com.veggroot.android.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessActivity extends AppCompatActivity {
    Integer noOfItems;
    Double totalCost;
    TextView priceNoOfItems, cost, finalCost;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    RecyclerView orderProcessRecyclerView;
    CartAdaptor cartAdaptor;
    List<Cart> cartList = new ArrayList<>();

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
        orderProcessRecyclerView = findViewById(R.id.orderProcessRecyclerView);
        orderProcessRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderProcessRecyclerView.setHasFixedSize(true);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                cartAdaptor.removeFromList(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(orderProcessRecyclerView);
        loadList();
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

    private void loadList() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid()).child("cart");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double inCost = 0D;
                cartList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Cart cart = dataSnapshot1.getValue(Cart.class);
                    cartList.add(cart);
                    inCost = inCost + cart.getCost() * cart.getTotalNumber();
                }
                if (cartList.size() == 0) {
                   finish();
                }
                priceNoOfItems.setText("Price ( " + cartList.size() + " items )");
                cost.setText("Rs " + inCost);
                finalCost.setText("Rs " + inCost);
                cartAdaptor = new CartAdaptor(OrderProcessActivity.this, cartList);
                orderProcessRecyclerView.setAdapter(cartAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderProcessActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
