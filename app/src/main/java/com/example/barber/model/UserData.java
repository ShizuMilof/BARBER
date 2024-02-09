package com.example.barber.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
    private String userUid;
    private String username;
    private String userEmail;

    // Constructors, getters, setters...

    // Public constructor for Parcelable
    public UserData() {
        // Default constructor
    }

    protected UserData(Parcel in) {
        userUid = in.readString();
        username = in.readString();
        userEmail = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeString(username);
        dest.writeString(userEmail);
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    // Setter methods
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
