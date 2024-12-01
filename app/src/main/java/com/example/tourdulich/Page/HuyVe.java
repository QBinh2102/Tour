package com.example.tourdulich.Page;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.BaiDanhGia;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HuyVe extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnHuyVe;
    private ImageView imgTour;
    private TextView txtTen;
    private TextView txtGia;
    private TextView txtPhuongTien;
    private TextView txtNgayKhoiHanh;
    private TextView txtNgayKetThuc;
    private TextView txtSoVeMua;
    private TextView txtTongTienMua;
    private TextView txtTrangThai;

    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
    private DatabaseReference bdgRef = FirebaseDatabase.getInstance().getReference("Bài đánh giá");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_huy_ve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnQuayLai = findViewById(R.id.btQuayLaiTuHuyVe);
        btnHuyVe = findViewById(R.id.btHuyVeTour);
        imgTour = findViewById(R.id.imageViewTourHuyVe);
        txtTen = findViewById(R.id.textViewTenTourHuyVe);
        txtGia = findViewById(R.id.textViewGiaTourHuyVe);
        txtPhuongTien = findViewById(R.id.textViewPhuongTienTourHuyVe);
        txtNgayKhoiHanh = findViewById(R.id.textViewNKHTourHuyVe);
        txtNgayKetThuc = findViewById(R.id.textViewNKTTourHuyVe);
        txtSoVeMua = findViewById(R.id.textViewSoVeDaMua);
        txtTongTienMua = findViewById(R.id.textViewTongTienDaMua);
        txtTrangThai = findViewById(R.id.textViewTrangThaiGiaoDich);

        Intent intent = getIntent();
        BaiDanhGia bdg = (BaiDanhGia) intent.getSerializableExtra("gd_item");

        //Show thông tin về tour đã đặt, số lượng vé, tổng tiền của user
        showThongTin(bdg);

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(HuyVe.this,GiaoDich.class);
                startActivity(ql);
            }
        });

        btnHuyVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bdg.trangThai.equals("Đã thanh toán")) {
                    tourRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    Tour tour = dataSnapshot.getValue(Tour.class);
                                    if(bdg.idTour.equals(tour.idTour)){
                                        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                        try {
                                            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(tour.ngayKhoiHanh);
                                            int dKhoiHanh = Integer.parseInt(formatter.format(date1));
                                            Calendar calendar = Calendar.getInstance();
                                            String dd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                                            String MM = String.valueOf(calendar.get(Calendar.MONTH)+1);
                                            String yyyy = String.valueOf(calendar.get(Calendar.YEAR));
                                            String format = String.format("%s/%s/%s",dd,MM,yyyy);
                                            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(format);
                                            int dHienTai = Integer.parseInt(formatter.format(date2));
                                            int tg = dKhoiHanh-dHienTai;
                                            if(tg>14){
                                                openDialog(bdg);
                                            }else{
                                                Toast.makeText(HuyVe.this,"Đã hết hạn hủy vé",Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (bdg.trangThai.equals("Đã hủy vé")) {
                    Toast.makeText(HuyVe.this,"Bạn đã hủy vé!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showThongTin(BaiDanhGia bdg) {
        tourRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Tour tour = dataSnapshot.getValue(Tour.class);
                        if(tour.idTour.equals(bdg.idTour)){
                            Uri uri = Uri.parse(tour.hinhTour);
                            Glide.with(HuyVe.this)
                                    .load(uri)
                                    .into(imgTour);
                            txtTen.setText(String.format("Tên: %s",tour.tenTour));
                            String formattedPrice = formatPrice(String.valueOf(tour.giaTien));
                            txtGia.setText(String.format("Giá: %s", formattedPrice));
                            txtPhuongTien.setText(String.format("Phương tiện: %s",tour.phuongTien));
                            txtNgayKhoiHanh.setText(String.format("Ngày khởi hành: %s",tour.ngayKhoiHanh));
                            txtNgayKetThuc.setText(String.format("Ngày kết thúc: %s",tour.ngayKetThuc));
                            txtSoVeMua.setText(String.format("Số vé đã mua: %d",bdg.soVe));
                            String Price = formatPrice(String.valueOf(bdg.tongTien));
                            txtTongTienMua.setText(String.format("Tổng tiền: %s", Price));
                            txtTrangThai.setText(String.format("Trạng thái: %s", bdg.trangThai));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HuyVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialog(BaiDanhGia bdg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_xac_nhan_huy_ve);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(false);

        Button btnHuy = dialog.findViewById(R.id.btHuyHuyVe);
        Button btnXacNhan = dialog.findViewById(R.id.btXacNhanHuyVe);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdgRef.child(bdg.idBaiDanhGia).child("trangThai").setValue("Đã hủy vé");
                bdgRef.child(bdg.idBaiDanhGia).child("soSao").setValue(0);
                bdgRef.child(bdg.idBaiDanhGia).child("binhLuan").setValue("");
                tourRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Tour tour = dataSnapshot.getValue(Tour.class);
                                if(tour.idTour.equals(bdg.idTour)) {
                                    tourRef.child(tour.idTour).child("soLuongVe").setValue(tour.soLuongVe+bdg.soVe);
                                    tourRef.child(tour.idTour).child("soLuongDat").setValue(tour.soLuongDat-1);
                                    bdgRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                int tongSao = 0;
                                                int luongSao = 0;
                                                int soBinhLuan = 0;
                                                for (DataSnapshot data : snapshot.getChildren()) {
                                                    BaiDanhGia danhGia = data.getValue(BaiDanhGia.class);
                                                    if (danhGia.idTour.equals(tour.idTour)&&danhGia.trangThai.equals("Đã thanh toán")) {
                                                        tongSao += danhGia.soSao;
                                                        luongSao++;
                                                        if(danhGia.binhLuan!="")
                                                            soBinhLuan++;
                                                    }
                                                }
                                                double tongSoSao = Double.parseDouble(String.valueOf(tongSao));
                                                double soLuongSao = Double.parseDouble(String.valueOf(luongSao));
                                                if(soLuongSao!=0) {
                                                    double tbSao = tongSoSao / soLuongSao;
                                                    tourRef.child(tour.idTour).child("soSao").setValue(tbSao);
                                                }else
                                                    tourRef.child(tour.idTour).child("soSao").setValue(0);
                                                tourRef.child(tour.idTour).child("soBinhLuan").setValue(soBinhLuan);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(HuyVe.this, "Hủy tour thành công!!!", Toast.LENGTH_SHORT).show();
                Intent gd = new Intent(HuyVe.this,GiaoDich.class);
                startActivity(gd);
            }
        });

        dialog.show();
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