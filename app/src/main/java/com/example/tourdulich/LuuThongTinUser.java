package com.example.tourdulich;

public class LuuThongTinUser {
    public String diaChi;
    public String soDienThoai;
    public String email;
    public String ngaySinh;
    public String gioiTinh;

    public LuuThongTinUser(){};
    public LuuThongTinUser(String diaChi,String soDienThoai,String email, String ngaySinh, String gioiTinh){
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }
}
