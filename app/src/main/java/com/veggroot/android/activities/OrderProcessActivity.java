package com.veggroot.android.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
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
    TextView priceNoOfItems, cost, finalCost, address;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    RecyclerView orderProcessRecyclerView;
    CartAdaptor cartAdaptor;
    List<Cart> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        priceNoOfItems = findViewById(R.id.priceNoOfItems);
        cost = findViewById(R.id.orderPrice);
        finalCost = findViewById(R.id.orderTotalValue);
        address = findViewById(R.id.orderProcessAddress);
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
        loadAddress();
    }

    public void confirmOrder(View view) {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

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
                setTitle("Delivery (" + cartList.size() + ")");
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

    private void loadAddress() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid()).child("info");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address.setText("" + dataSnapshot.child("address").getValue() + "\n" + dataSnapshot.child("pincode").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderProcessActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editAddress(View view) {
        startActivity(new Intent(OrderProcessActivity.this, EditAddressActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
