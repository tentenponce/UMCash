package com.uhac.umcash.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Exequiel Egbert V. Ponce on 12/10/2016.
 */

public class Company implements Parcelable{

    @SerializedName("company_id")
    private int id;

    @SerializedName("pt_id")
    private int ptId;

    @SerializedName("company_name")
    private String name;

    @SerializedName("company_desc")
    private String desc;

    @SerializedName("company_address")
    private String address;

    @SerializedName("company_img")
    private String img;

    protected Company(Parcel in) {
        id = in.readInt();
        ptId = in.readInt();
        name = in.readString();
        desc = in.readString();
        address = in.readString();
        img = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPtId() {
        return ptId;
    }

    public void setPtId(int ptId) {
        this.ptId = ptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(ptId);
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(address);
        parcel.writeString(img);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
