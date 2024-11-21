package com.example.tourdulich.CSDL;


public class Tour {
    public String idTour;
    public String danhMuc;
    public String giaTien;
    public String hinhTour;
    public String ngayKetThuc;
    public String ngayKhoiHanh;
    public String phuongTien;
    public int soBinhLuan;
    public int soLuongVe;
    public int soSao;
    public String tenTour;

    public Tour(){

    }

    public Tour(String idTour, String tenTour, String hinhTour, String danhMuc, String phuongTien,
                String ngayKhoiHanh, String ngayKetThuc, String giaTien, int soLuongVe) {
        this.idTour = idTour;
        this.tenTour = tenTour;
        this.hinhTour = hinhTour;
        this.danhMuc = danhMuc;
        this.phuongTien = phuongTien;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayKetThuc = ngayKetThuc;
        this.giaTien = giaTien;
        this.soSao = 0;
        this.soBinhLuan = 0;
        this.soLuongVe = soLuongVe;
    }

}
