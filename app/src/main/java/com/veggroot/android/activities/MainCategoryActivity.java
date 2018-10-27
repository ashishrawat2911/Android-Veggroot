package com.veggroot.android.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.veggroot.android.R;
import com.veggroot.android.adaptor.ViewPagerCategoryAdaptor;

import java.util.Objects;

public class MainCategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView name, phoneNumber, address, circleAvatar;
    LinearLayout navigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.navHeadName);
        phoneNumber = headerView.findViewById(R.id.navHeadPhoneNumber);
        address = headerView.findViewById(R.id.navHeadAddress);
        circleAvatar = headerView.findViewById(R.id.circleAvatar);
        navigationLayout = headerView.findViewById(R.id.navigation_header_layout);
        navigationView.setNavigationItemSelectedListener(this);
        mSmartTabLayout = (SmartTabLayout) findViewById(R.id.tab_view_pager_fav);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_fav);
        mViewPager.setAdapter(new ViewPagerCategoryAdaptor(getSupportFragmentManager(), this));
        mSmartTabLayout.setViewPager(mViewPager);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("user").child(mAuth.getUid()).child("info").exists()) {
                    name.setVisibility(View.VISIBLE);
                    phoneNumber.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    navigationLayout.setVisibility(View.VISIBLE);
                    circleAvatar.setVisibility(View.VISIBLE);
                    name.setText(dataSnapshot.child("user").child(mAuth.getUid()).child("info").child("name").getValue().toString());
                    address.setText(dataSnapshot.child("user").child(mAuth.getUid()).child("info").child("address").getValue().toString());
                    phoneNumber.setText(dataSnapshot.child("user").child(mAuth.getUid()).child("info").child("phoneNumber").getValue().toString());
                    circleAvatar.setText(getNameInitials(name.getText().toString()));
                } else {
                    name.setVisibility(View.GONE);
                    phoneNumber.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    navigationLayout.setVisibility(View.GONE);
                    circleAvatar.setVisibility(View.GONE);
                }
                if (!dataSnapshot.child("user").child(mAuth.getUid()).exists()) {
                    startActivity(new Intent(MainCategoryActivity.this, LoginActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getNameInitials(String fullName) {
        String initials = "";
        String[] name = fullName.trim().split(" ");
        for (String aName : name) {
            initials = initials + aName.charAt(0);
        }
        return initials;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            startActivity(new Intent(this, CartActivity.class));
        } else if (id == R.id.nav_visit_us) {
            startActivity(new Intent(this, VisitUsActivity.class));

        } else if (id == R.id.nav_SignOut) {
            mAuth.signOut();
            startActivity(new Intent(MainCategoryActivity.this, LoginActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_category, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.cart_icon_badge);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        final TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid()).child("cart");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("" + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainCategoryActivity.this, CartActivity.class));
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart:
                Toast.makeText(this, "cart clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
