package com.example.tourdulich.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class DanhMuc {
    public String ten;
    public String hinh;
    public String loai;

    // Constructor mặc định
    public DanhMuc() {}

    // Constructor có tham số
    public DanhMuc(String ten, String hinh, String loai) {
        this.ten = ten;
        this.hinh = hinh;
        this.loai = loai;
    }
}
