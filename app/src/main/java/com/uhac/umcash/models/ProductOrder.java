package com.uhac.umcash.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * object for orders of products
 * Created by Exequiel Egbert V. Ponce on 12/10/2016.
 */

public class ProductOrder implements Parcelable{

    @SerializedName("product_item")
    Product product;

    @SerializedName("order_qty")
    int qty;

    public ProductOrder(Product product, int qty) {
        this.product = product;
        this.qty = qty;
    }

    protected ProductOrder(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        qty = in.readInt();
    }

    public static final Creator<ProductOrder> CREATOR = new Creator<ProductOrder>() {
        @Override
        public ProductOrder createFromParcel(Parcel in) {
            return new ProductOrder(in);
        }

        @Override
        public ProductOrder[] newArray(int size) {
            return new ProductOrder[size];
        }
    };

    public void setQuantity(int qty) {
        this.qty = qty;
    }

    public int getQuantity() {
        return qty;
    }

    public Product getProductItem() {
        return product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(product, i);
        parcel.writeInt(qty);
    }
}
