package com.example.tourdulich.trang;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.example.tourdulich.CSDL.BaiDanhGia;
import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
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

    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");



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