package com.example.rdc_lnmiit;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersModel implements Parcelable {

    String uid, userName, email;

    public UsersModel(){

    }

    public UsersModel(String uid, String userName, String email) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
    }

    protected UsersModel(Parcel in) {
        uid = in.readString();
        userName = in.readString();
        email = in.readString();
    }

    public static final Creator<UsersModel> CREATOR = new Creator<UsersModel>() {
        @Override
        public UsersModel createFromParcel(Parcel in) {
            return new UsersModel(in);
        }

        @Override
        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(userName);
        dest.writeString(email);
    }
}
