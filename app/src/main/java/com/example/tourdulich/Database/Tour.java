package com.example.tourdulich.Database;


import java.io.Serializable;
import java.util.ArrayList;

public class Tour implements Serializable {
    private String name;
    public String idTour;
    public String tenTour;
    public String danhMuc;
    public String giaTien;
    public String hinhTour;
    public String ngayKetThuc;
    public String ngayKhoiHanh;
    public String phuongTien;
    public String gioiThieu;
    public int soLuongVe;
    public int soBinhLuan;
    public double soSao;
    public int soLuongDat;

    public Tour(){}

    public Tour(String idTour, String tenTour, String hinhTour, String danhMuc, String phuongTien, String name,
                String gioiThieu, String ngayKhoiHanh, String ngayKetThuc, String giaTien, int soLuongVe) {
        this.idTour = idTour;
        this.tenTour = tenTour;
        this.hinhTour = hinhTour;
        this.danhMuc = danhMuc;
        this.phuongTien = phuongTien;
        this.gioiThieu = gioiThieu;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayKetThuc = ngayKetThuc;
        this.giaTien = giaTien;
        this.soSao = 0;
        this.soBinhLuan = 0;
        this.soLuongVe = soLuongVe;
        this.soLuongDat=0;
    }
    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
