package com.example.tourdulich.CSDL;

public class BaiDanhGia {
    public String idBDG;
    public LuuThongTinUser idUser;
    public Tour idTour;
    public int soSao;
    public String binhLuan;

    public BaiDanhGia(){}

    public BaiDanhGia(String idBDG, LuuThongTinUser idUser, Tour idTour, int soSao, String binhLuan) {
        this.idBDG = idBDG;
        this.idUser = idUser;
        this.idTour = idTour;
        this.soSao = soSao;
        this.binhLuan = binhLuan;
    }
}
