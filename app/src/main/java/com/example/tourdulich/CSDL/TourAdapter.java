package com.example.tourdulich.CSDL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourdulich.R;

import java.util.List;

public class TourAdapter extends BaseAdapter {

    private Context context;
    private List<Tour> tourList;
    private List<SoSao> soSaoList;
    private List<BinhLuan> binhLuanList;
    private LayoutInflater inflater;

    public TourAdapter(Context context, List<Tour> tourList) {
        this.context = context;
        this.tourList = tourList;
//        this.soSaoList = soSaoList;
//        this.binhLuanList = binhLuanList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tourList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.danh_sach_tour,null);

        ImageView imgHinh = (ImageView) convertView.findViewById(R.id.imageViewHinhTour);
        TextView txtTen = (TextView) convertView.findViewById(R.id.textViewTenTour);
        TextView txtPhuongTien = (TextView) convertView.findViewById(R.id.textViewPhuongTien);
        TextView txtNgayKhoiHanh = (TextView) convertView.findViewById(R.id.textViewNgayKhoiHanh);
        TextView txtGia = (TextView) convertView.findViewById(R.id.textViewGiaTour);
        TextView txtSoSao = (TextView) convertView.findViewById(R.id.textViewSoSao);
        TextView txtSoBinhLuan = (TextView) convertView.findViewById(R.id.textViewSoBinhLuan);

        Tour tour = tourList.get(position);
        imgHinh.setImageResource(tour.hinhTour);
        txtTen.setText(tour.tenTour);
        txtPhuongTien.setText(String.format("Phương tiện: %s",tour.phuongTien));
        txtNgayKhoiHanh.setText(String.format("Ngày khởi hành: %s",tour.ngayKhoiHanh));
        txtGia.setText(String.format("Giá: %s",tour.giaTien));
//        double soSao = soSaoList.stream().filter(sosao->sosao.tenTour.equals(tour.tenTour)).mapToDouble(value -> value.soSao).sum();
//        int tongSao = (int) soSaoList.stream().filter(sosao->sosao.tenTour.equals(tour.tenTour)).count();
//        String sao = String.valueOf((double) (soSao/tongSao));
        double soSao = 0;
        txtSoSao.setText(String.format("%.2f/5 sao",soSao));
//        int binhLuan = (int) binhLuanList.stream().filter(binhluan->binhluan.tenTour.equals(tour.tenTour)).count();
//        String countBL = String.valueOf(binhLuan);
        txtSoBinhLuan.setText(String.format("%s comments", 0));

        return convertView;
    }
}
