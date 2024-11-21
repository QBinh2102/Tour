package com.example.tourdulich.CSDL;

public class SoSao {
    public String idSoSao;
    public double soSao;
    public Tour tenTour;

    public SoSao() {

    }

    public SoSao(String id, double soSao, Tour tenTour) {
        this.idSoSao = id;
        this.soSao = soSao;
        this.tenTour = tenTour;
    }
}
