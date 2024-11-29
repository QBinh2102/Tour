package com.example.tourdulich.Database;

import java.io.Serializable;

public class BaiDanhGia implements Serializable {
    public String idBaiDanhGia;
    public String idUser;
    public String idTour;
    public int soVe;
    public int tongTien;
    public int soSao;
    public String binhLuan;
    public String thoiGian;
    public String trangThai;

    public BaiDanhGia(){}

    public BaiDanhGia(String idBaiDanhGia, String idUser, String idTour, int soVe, int tongTien) {
        this.idBaiDanhGia = idBaiDanhGia;
        this.idUser = idUser;
        this.idTour = idTour;
        this.soVe = soVe;
        this.tongTien = tongTien;
        this.soSao = 0;
        this.binhLuan = "";
        this.thoiGian = "";
        this.trangThai="Đã thanh toán";
    }
}
