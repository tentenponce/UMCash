package com.uhac.umcash.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * group of proware orders with reservation code
 * Created by Exequiel Egbert V. Ponce on 8/3/2016.
 */
public class ReservationGroup implements Parcelable{

    @SerializedName("reserve_code")
    private String code;

    @SerializedName("account_name")
    private String accountName;

    @SerializedName("product_orders")
    private ArrayList<ProductOrder> productOrders;

    @SerializedName("reserve_date")
    private Date reservedDate;

    @SerializedName("date_today")
    private Date dateToday;

    protected ReservationGroup(Parcel in) {
        code = in.readString();
        productOrders = in.createTypedArrayList(ProductOrder.CREATOR);
        reservedDate = new Date(in.readLong());
        dateToday = new Date(in.readLong());
    }

    public static final Creator<ReservationGroup> CREATOR = new Creator<ReservationGroup>() {
        @Override
        public ReservationGroup createFromParcel(Parcel in) {
            return new ReservationGroup(in);
        }

        @Override
        public ReservationGroup[] newArray(int size) {
            return new ReservationGroup[size];
        }
    };

    public String getCode() {
        return code;
    }

    public ArrayList<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public Date getReservedDate() {
        return reservedDate;
    }

    public Date getDateToday() {
        return dateToday;
    }

    public int getItemCount() {
        return productOrders.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeTypedList(productOrders);
        parcel.writeLong(reservedDate.getTime());
        parcel.writeLong(dateToday.getTime());
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
