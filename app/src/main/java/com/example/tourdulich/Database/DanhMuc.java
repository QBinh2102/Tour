package com.example.tourdulich.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class DanhMuc implements Parcelable {
    public String ten;
    public String hinh;

    // Constructor mặc định
    public DanhMuc() {}

    // Constructor có tham số
    public DanhMuc(String ten, String hinh) {
        this.ten = ten;
        this.hinh = hinh;
    }

    // Phương thức tạo đối tượng từ Parcel
    protected DanhMuc(Parcel in) {
        ten = in.readString();
        hinh = in.readString();
    }

    // Tạo đối tượng từ Parcel
    public static final Creator<DanhMuc> CREATOR = new Creator<DanhMuc>() {
        @Override
        public DanhMuc createFromParcel(Parcel in) {
            return new DanhMuc(in);
        }

        @Override
        public DanhMuc[] newArray(int size) {
            return new DanhMuc[size];
        }
    };

    // Phương thức ghi đối tượng vào Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ten);
        dest.writeString(hinh);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
