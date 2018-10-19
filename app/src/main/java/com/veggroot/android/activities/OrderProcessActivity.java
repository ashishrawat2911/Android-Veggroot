package com.veggroot.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.veggroot.android.R;
import com.veggroot.android.utils.Constants;

public class OrderProcessActivity extends AppCompatActivity {
    Integer noOfItems;
    Double totalCost;
    TextView priceNoOfItems, cost, finalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);
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

    }
}
