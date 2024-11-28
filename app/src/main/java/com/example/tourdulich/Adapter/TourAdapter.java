package com.example.tourdulich.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TourAdapter extends BaseAdapter {

    private Context context;
    private List<Tour> tourList;
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
        convertView = inflater.inflate(R.layout.tour_items,null);

        ImageView imgHinh = (ImageView) convertView.findViewById(R.id.imageViewHinhTourItem);
        TextView txtTen = (TextView) convertView.findViewById(R.id.textViewTenTour);
        TextView txtPhuongTien = (TextView) convertView.findViewById(R.id.textViewPhuongTien);
        TextView txtNgayKhoiHanh = (TextView) convertView.findViewById(R.id.textViewNgayKhoiHanh);
        TextView txtGia = (TextView) convertView.findViewById(R.id.textViewGiaTour);
        ImageView sao1 = (ImageView) convertView.findViewById(R.id.imageViewSao1TourItem);
        ImageView sao2 = (ImageView) convertView.findViewById(R.id.imageViewSao2TourItem);
        ImageView sao3 = (ImageView) convertView.findViewById(R.id.imageViewSao3TourItem);
        ImageView sao4 = (ImageView) convertView.findViewById(R.id.imageViewSao4TourItem);
        ImageView sao5 = (ImageView) convertView.findViewById(R.id.imageViewSao5TourItem);
        TextView txtSoBinhLuan = (TextView) convertView.findViewById(R.id.textViewSoBinhLuan);

        Tour tour = tourList.get(position);
        Uri imageUri = Uri.parse(tour.hinhTour);
        Glide.with(context)
                .load(imageUri)
                .into(imgHinh);
        txtTen.setText(tour.tenTour);
        txtPhuongTien.setText(String.format("Phương tiện: %s",tour.phuongTien));
        txtNgayKhoiHanh.setText(String.format("Ngày khởi hành: %s",tour.ngayKhoiHanh));
        String formattedPrice = formatPrice(String.valueOf(tour.giaTien));
        txtGia.setText(String.format("Giá: %s",formattedPrice));
//        double soSao = soSaoList.stream().filter(sosao->sosao.tenTour.equals(tour.tenTour)).mapToDouble(value -> value.soSao).sum();
//        int tongSao = (int) soSaoList.stream().filter(sosao->sosao.tenTour.equals(tour.tenTour)).count();
//        String sao = String.valueOf((double) (soSao/tongSao));
        double soSao = tour.soSao;
        if(soSao==0){
            sao1.setImageResource(R.drawable.star_border_24);
            sao2.setImageResource(R.drawable.star_border_24);
            sao3.setImageResource(R.drawable.star_border_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>0&&soSao<0.8) {
            sao1.setImageResource(R.drawable.star_half_24);
            sao2.setImageResource(R.drawable.star_border_24);
            sao3.setImageResource(R.drawable.star_border_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=0.8&&soSao<1.2) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_border_24);
            sao3.setImageResource(R.drawable.star_border_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=1.2&&soSao<1.8) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_half_24);
            sao3.setImageResource(R.drawable.star_border_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=1.8&&soSao<2.2) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_border_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=2.2&&soSao<2.8) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_half_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=2.8&&soSao<3.2) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_24);
            sao4.setImageResource(R.drawable.star_border_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=3.2&&soSao<3.8) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_24);
            sao4.setImageResource(R.drawable.star_half_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=3.8&&soSao<4.2) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_24);
            sao4.setImageResource(R.drawable.star_24);
            sao5.setImageResource(R.drawable.star_border_24);
        } else if (soSao>=4.2&&soSao<4.8) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_24);
            sao4.setImageResource(R.drawable.star_24);
            sao5.setImageResource(R.drawable.star_half_24);
        } else if (soSao>=4.8) {
            sao1.setImageResource(R.drawable.star_24);
            sao2.setImageResource(R.drawable.star_24);
            sao3.setImageResource(R.drawable.star_24);
            sao4.setImageResource(R.drawable.star_24);
            sao5.setImageResource(R.drawable.star_24);
        }
        txtSoBinhLuan.setText(String.format("%d bình luận", tour.soBinhLuan));

        return convertView;
    }

    private String formatPrice(String price) {
        try {
            // Xóa tất cả các dấu phẩy và khoảng trắng, rồi chia giá thành các phần
            long priceValue = Long.parseLong(price);  // Chuyển giá sang long để xử lý

            // Sử dụng NumberFormat để định dạng giá tiền
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
            String formatted = numberFormat.format(priceValue);
            return formatted.replace(',', ' ');
        } catch (NumberFormatException e) {
            return price;  // Nếu không thể chuyển đổi, giữ nguyên giá gốc
        }
    }
}
