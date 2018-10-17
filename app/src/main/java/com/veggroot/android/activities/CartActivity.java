package com.veggroot.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import com.veggroot.android.model.Vegetable;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView noOfItems, totalCost;
    RecyclerView cartRecyclerView;
    CartAdaptor cartAdaptor;
    List<Cart> categoriesTopList = new ArrayList<>();
    List<Cart> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        noOfItems = findViewById(R.id.no_of_items);
        totalCost = findViewById(R.id.cartTotalPrice);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);
        Toast.makeText(this, categoriesList.size() + "", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid()).child("cart");
        mDatabase.keepSynced(true);
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
        }).attachToRecyclerView(cartRecyclerView);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double cost = 0D;
                categoriesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Cart cart = dataSnapshot1.getValue(Cart.class);
                    categoriesList.add(cart);


                }
                if (categoriesList.size() == 0) {
                    noOfItems.setText("0");
                    totalCost.setText("Rs 0");
                } else {
                    noOfItems.setText("" + categoriesList.size());
                    for (int i = 0; i < categoriesList.size(); i++) {
                        cost = cost + categoriesList.get(i).getCost() * categoriesList.get(i).totalNumber;
                    }
                    totalCost.setText("Rs " + cost);
                }

                cartAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        cartAdaptor = new CartAdaptor(CartActivity.this, categoriesList);
        cartRecyclerView.setAdapter(cartAdaptor);
    }


    public void placeOrder(View view) {
        startActivity(new Intent(CartActivity.this, OrderProcessActivity.class));

    }

    private void loadList() {

    }

  /*  @Override
    protected void onStop() {
        super.onStop();
        List<Cart> cartList = cartAdaptor.getCategoriesList();
        mDatabase.setValue(null);
        for (int i = 0; i < cartAdaptor.getCategoriesList().size(); i++) {
            Cart cart = new Cart(cartList.get(i).getCost(),
                    cartList.get(i).getItemId(),
                    cartList.get(i).getItemName(),
                    cartList.get(i).getItemImage(),
                    cartList.get(i).getMarketPrice(),
                    cartList.get(i).getTotalNumber(), cartList.get(i).getUnit()
            );
            mDatabase.child(cartList.get(i).getItemName()).setValue(cart);
        }


    }*/
}
