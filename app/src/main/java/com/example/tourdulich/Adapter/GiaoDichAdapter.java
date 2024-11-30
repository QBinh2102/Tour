package com.example.tourdulich.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.BaiDanhGia;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GiaoDichAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BaiDanhGia> baiDanhGias;
    private LayoutInflater inflater;
    private Tour tour;
    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");

    public GiaoDichAdapter(Context context, ArrayList<BaiDanhGia> baiDanhGias) {
        this.context = context;
        this.baiDanhGias = baiDanhGias;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return baiDanhGias.size();
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
        convertView = inflater.inflate(R.layout.giao_dich_items,null);

        ImageView hinh = (ImageView) convertView.findViewById(R.id.imageViewTourGiaoDich);
        TextView ten = (TextView) convertView.findViewById(R.id.textViewTenTourGiaoDich);
        ImageView sao1 = (ImageView) convertView.findViewById(R.id.imageViewSao1GDItems);
        ImageView sao2 = (ImageView) convertView.findViewById(R.id.imageViewSao2GDItems);
        ImageView sao3 = (ImageView) convertView.findViewById(R.id.imageViewSao3GDItems);
        ImageView sao4 = (ImageView) convertView.findViewById(R.id.imageViewSao4GDItems);
        ImageView sao5 = (ImageView) convertView.findViewById(R.id.imageViewSao5GDItems);
        TextView soBinhLuan = (TextView) convertView.findViewById(R.id.textViewSoBinhLuanGDItems);
        TextView soVe = (TextView) convertView.findViewById(R.id.textViewSoLuongVeGDItems);
        TextView tongTien = (TextView) convertView.findViewById(R.id.textViewTongTienGDItems);
        TextView trangThai = (TextView) convertView.findViewById(R.id.textViewTrangThaiGDItems);

        BaiDanhGia baiDanhGia = baiDanhGias.get(position);
        String tourId = baiDanhGia.idTour;
        soVe.setText(String.format("Số lượng vé: x%d",baiDanhGia.soVe));
        String formattedPrice = formatPrice(String.valueOf(baiDanhGia.tongTien));
        tongTien.setText(String.format("Tổng tiền: %s",formattedPrice));
        trangThai.setText(String.format("Trạng thái: %s",baiDanhGia.trangThai));
        tourRef.child(tourId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tour = snapshot.getValue(Tour.class);
                Uri uri = Uri.parse(tour.hinhTour);
                Glide.with(context)
                        .load(uri)
                        .into(hinh);
                ten.setText(tour.tenTour);
                soBinhLuan.setText(String.format("%d đánh giá",tour.soBinhLuan));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

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
