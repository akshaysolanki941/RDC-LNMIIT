package com.example.rdc_lnmiit.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersModel {

    String uid, userName, email;

    public UsersModel(){

    }

    public UsersModel(String uid, String userName, String email) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
    }


    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

}
