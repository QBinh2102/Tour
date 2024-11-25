package com.example.tourdulich.CSDL;

public class BaiDanhGia {
    public String idBaiDanhGia;
    public String idUser;
    public String idTour;
    public int soVe;
    public int tongTien;
    public int soSao;
    public String binhLuan;

    public BaiDanhGia(){}

    public BaiDanhGia(String idBaiDanhGia, String idUser, String idTour, int soVe, int tongTien) {
        this.idBaiDanhGia = idBaiDanhGia;
        this.idUser = idUser;
        this.idTour = idTour;
        this.soVe = soVe;
        this.tongTien = tongTien;
        this.soSao = 0;
        this.binhLuan = "";
    }
}
