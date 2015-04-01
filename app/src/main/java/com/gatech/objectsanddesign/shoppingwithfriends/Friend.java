package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Extend the user class to be able to have a rating relationship between a user and a friend
 */

public class Friend extends User {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
    private final double rating;

    Friend(String first, String last, String uid, String email, double rating) {
        super(first, last, uid, email);
        this.rating = rating;
    }

    protected Friend(Parcel in) {
        super(in);
        rating = in.readLong();
    }

    public double getRating() {
        return rating;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(rating);
    }

}
