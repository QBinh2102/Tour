package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietDatVe extends AppCompatActivity {

    private Button btnQuayLai, btnThanhToan;
    private TextView txtTen, txtGia, txtNgayKhoiHanh, txtNgayKetThuc, txtTongTien, txtSoLuongVeDat;
    private ImageView imgTour, btnGiamSoVe, btnTangSoVe;

    private int tongTien = 0; // Tổng tiền hiện tại
    private int soVeDat = 1;  // Mặc định số vé đặt là 1
    private Tour tour;

    private FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refDuLieu = FirebaseDatabase.getInstance().getReference("Bài đánh giá");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_dat_ve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy dữ liệu tour từ Intent
        Intent intent = getIntent();
        tour = (Tour) intent.getSerializableExtra("tour");

        // Ánh xạ các thành phần giao diện
        btnQuayLai = findViewById(R.id.btQuayLaiTuChiTietDatVe);
        btnThanhToan = findViewById(R.id.btThanhToanTuCTDV);
        txtTen = findViewById(R.id.textViewCTTenTour);
        txtGia = findViewById(R.id.textViewCTGiaTour);
        txtNgayKhoiHanh = findViewById(R.id.textViewCTNgayKhoiHanh);
        txtNgayKetThuc = findViewById(R.id.textViewCTNgayKetThuc);
        txtTongTien = findViewById(R.id.textViewCTTongTien);
        txtSoLuongVeDat = findViewById(R.id.soLuongVeDat);
        imgTour = findViewById(R.id.imageViewCTDV);
        btnGiamSoVe = findViewById(R.id.btnGiamSoLuongVe);
        btnTangSoVe = findViewById(R.id.btnTangSoLuongVe);

        // Hiển thị thông tin tour
        showThongTinTour(tour);

        // Giảm số lượng vé
        btnGiamSoVe.setOnClickListener(v -> {
            if (soVeDat > 1) {
                soVeDat--;
                updateTongTien();
            }
        });

        // Tăng số lượng vé
        btnTangSoVe.setOnClickListener(v -> {
            if (soVeDat < tour.soLuongVe) {
                soVeDat++;
                updateTongTien();
            }
        });

        // Quay lại màn hình trước
        btnQuayLai.setOnClickListener(v -> finish());

        // Thanh toán
        btnThanhToan.setOnClickListener(v -> {
            // Truyền tổng tiền sang màn hình xác nhận thanh toán
            Intent thanhToanIntent = new Intent(ChiTietDatVe.this, XacNhanThanhToan.class);
            thanhToanIntent.putExtra("total", tongTien);
            thanhToanIntent.putExtra("pay_tour",tour);
            thanhToanIntent.putExtra("sl_ve",soVeDat);
            startActivity(thanhToanIntent);
        });
    }

    // Hiển thị thông tin tour
    private void showThongTinTour(Tour tour) {
        Glide.with(ChiTietDatVe.this).load(tour.hinhTour).into(imgTour);
        txtTen.setText(tour.tenTour);
        txtGia.setText(formatPrice(tour.giaTien) + " VND");
        txtNgayKhoiHanh.setText(tour.ngayKhoiHanh);
        txtNgayKetThuc.setText(tour.ngayKetThuc);
        txtSoLuongVeDat.setText(String.valueOf(soVeDat));
        updateTongTien();
    }

    // Cập nhật tổng tiền khi số vé thay đổi
    private void updateTongTien() {
        int giaVe = Integer.parseInt(tour.giaTien);
        tongTien = giaVe * soVeDat;
        txtTongTien.setText(formatPrice(String.valueOf(tongTien)) + " VND");
        txtSoLuongVeDat.setText(String.valueOf(soVeDat));
    }

    // Định dạng giá tiền
    private String formatPrice(String price) {
        try {
            long priceValue = Long.parseLong(price);
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
            return numberFormat.format(priceValue).replace(',', '.');
        } catch (NumberFormatException e) {
            return price;
        }
    }
}
