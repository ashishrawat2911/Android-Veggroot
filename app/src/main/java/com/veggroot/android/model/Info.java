package com.veggroot.android.model;

import android.widget.EditText;

public class Info {
    private String name, email, address, pincode,phonenumber;

    public Info() {
    }

    public Info(String name, String email, String address, String pincode, String phonenumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.pincode = pincode;
        this.phonenumber = phonenumber;
    }
}
