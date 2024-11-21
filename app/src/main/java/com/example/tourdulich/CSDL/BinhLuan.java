package com.example.tourdulich.CSDL;

public class BinhLuan {
    public String idBinhLuan;
    public String binhLuan;
    public Tour tenTour;

    public BinhLuan(){

    }
    public BinhLuan(String id, String binhLuan, Tour tour){
        this.idBinhLuan = id;
        this.binhLuan=binhLuan;
        this.tenTour=tour;
    }
}
