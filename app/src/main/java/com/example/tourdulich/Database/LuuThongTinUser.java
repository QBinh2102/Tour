package com.example.tourdulich.Database;

public class LuuThongTinUser {
    public String id;
    public String tenNguoiDung;
    public String hinhDaiDien;
    public String diaChi;
    public String soDienThoai;
    public String email;
    public String ngaySinh;
    public String gioiTinh;
    public String role = "user";

    public LuuThongTinUser(){};
    public LuuThongTinUser(String id, String ten, String hinh, String diaChi,String soDienThoai,String email,
                           String ngaySinh, String gioiTinh){
        this.id=id;
        this.tenNguoiDung = ten;
        this.hinhDaiDien=hinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }
}
