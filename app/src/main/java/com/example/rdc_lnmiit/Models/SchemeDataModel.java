package com.example.rdc_lnmiit.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class SchemeDataModel implements Parcelable {

    String /*id, */ scheme, year, motive, bene, mile, rg_value, picURL, rg_inOperation;

    public SchemeDataModel(){

    }

    public SchemeDataModel(/*String id, */ String scheme, String year, String motive, String bene, String mile, String rg_value, String picURL, String rg_inOperation) {
        //this.id = id;
        this.scheme = scheme;
        this.year = year;
        this.motive = motive;
        this.bene = bene;
        this.mile = mile;
        this.rg_value = rg_value;
        this.picURL = picURL;
        this.rg_inOperation = rg_inOperation;
    }

    /*public String getId() {
        return id;
    }*/

    protected SchemeDataModel(Parcel in) {
        scheme = in.readString();
        year = in.readString();
        motive = in.readString();
        bene = in.readString();
        mile = in.readString();
        rg_value = in.readString();
        picURL = in.readString();
        rg_inOperation = in.readString();
    }

    public static final Creator<SchemeDataModel> CREATOR = new Creator<SchemeDataModel>() {
        @Override
        public SchemeDataModel createFromParcel(Parcel in) {
            return new SchemeDataModel(in);
        }

        @Override
        public SchemeDataModel[] newArray(int size) {
            return new SchemeDataModel[size];
        }
    };

    public String getScheme() {
        return scheme;
    }

    public String getYear() {
        return year;
    }

    public String getMotive() {
        return motive;
    }

    public String getBene() {
        return bene;
    }

    public String getMile() {
        return mile;
    }

    public String getRg_value() {
        return rg_value;
    }

    public String getPicURL() {
        return picURL;
    }

    public String getRg_inOperation(){
        return rg_inOperation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scheme);
        dest.writeString(year);
        dest.writeString(motive);
        dest.writeString(bene);
        dest.writeString(mile);
        dest.writeString(rg_value);
        dest.writeString(picURL);
        dest.writeString(rg_inOperation);
    }
}
