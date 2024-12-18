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
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CreateTourAdapter extends BaseAdapter {

    private Context context;
    private List<Tour> tourList;
    private LayoutInflater inflater;

    public CreateTourAdapter(Context context, List<Tour> tourList) {
        this.context = context;
        this.tourList = tourList;

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
        convertView = inflater.inflate(R.layout.tour_list,null);

        ImageView img = (ImageView) convertView.findViewById(R.id.imgVHinhTourList);
        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenTourList);
        TextView txtNKH = (TextView) convertView.findViewById(R.id.txtNKHTourList);
        TextView txtSoLuongVe = (TextView) convertView.findViewById(R.id.txtSoVeTourList);
        TextView txtGiaTien = (TextView) convertView.findViewById(R.id.txtGiaTienTourList);

        Tour tour = tourList.get(position);
        Glide.with(context)
                .load(Uri.parse(tour.hinhTour))
                .into(img);
        txtTen.setText(tour.tenTour);
        txtNKH.setText(String.format("Ngày khởi hành: %s",tour.ngayKhoiHanh));
        txtSoLuongVe.setText(String.format("Số vé: %d",tour.soLuongVe));
        String formattedPrice = formatPrice(String.valueOf(tour.giaTien));
        txtGiaTien.setText(String.format("Giá tiền: %s",formattedPrice));

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
