package com.example.tourdulich.Database;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    public String id;
    public String ten;
    public String hinh;

    // Constructor mặc định
    public DanhMuc() {}

    // Constructor có tham số
    public DanhMuc(String id, String ten, String hinh) {
        this.id = id;
        this.ten = ten;
        this.hinh = hinh;
    }

    public String getName() {
        return ten;
    }

    public void setName(String name) {
        this.ten = name;
    }
}
