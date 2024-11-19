package com.example.tourdulich.CSDL;


public class Tour {
    public String tenTour;
    public int hinhTour;
    public String danhMuc;
    public String phuongTien;
    public String ngayKhoiHanh;
    public String ngayKetThuc;
    public String giaTien;
    public int soSao;
    public int soBinhLuan;
    public int soLuongVe;

    public Tour(){

    }

    public Tour(String tenTour, int hinhTour, String danhMuc, String phuongTien,
                String ngayKhoiHanh, String ngayKetThuc, String giaTien, int soLuongVe) {
        this.tenTour = tenTour;
        this.hinhTour = hinhTour;
        this.danhMuc = danhMuc;
        this.phuongTien = phuongTien;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayKetThuc = ngayKetThuc;
        this.giaTien = giaTien;
        this.soSao = 0;
        soBinhLuan = 0;
        this.soLuongVe = soLuongVe;
    }

}
