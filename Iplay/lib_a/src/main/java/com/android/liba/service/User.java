package com.android.liba.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("token")
    public String token;

    @SerializedName("uid")
    public String uid;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("create_time")
    public String createTime;

    @SerializedName("login_count")
    public String loginCount;

    @SerializedName("phone")
    public String phone;
    
    @SerializedName("cid")
    public String cid;

    protected User(Parcel in) {
        token = in.readString();
        uid = in.readString();
        nickname = in.readString();
        createTime = in.readString();
        loginCount = in.readString();
        phone = in.readString();
        cid = in.readString();
    }
   public User()
   {

   }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(uid);
        dest.writeString(nickname);
        dest.writeString(createTime);
        dest.writeString(loginCount);
        dest.writeString(phone);
        dest.writeString(cid);
    }
}
