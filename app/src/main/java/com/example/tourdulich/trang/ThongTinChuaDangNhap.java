package com.example.tourdulich.trang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;

public class ThongTinChuaDangNhap extends AppCompatActivity {

    private Button btnDangNhap;
    private Button btnDangKy;

    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_chua_dang_nhap);

        //Chuyển sang TRANG ĐĂNG NHẬP
        Intent dangNhap = new Intent(this, MainActivity.class);
        btnDangNhap = findViewById(R.id.btDangNhapTuHoSo);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dangNhap);

            }
        });

        //Chuyển sang ĐĂNG KÝ THÔNG TIN CÁ NHÂN
        Intent dangKy = new Intent(this, TrangDangKy.class);
        btnDangKy = findViewById(R.id.btDangKyTuHoSo);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dangKy);
            }
        });

        //Chuyển sang TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnToiTrangChu = findViewById(R.id.btHoSoNullToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btHoSoNullToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang ĐẶT VÉ
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btHoSoNullToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(datVe);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent giaoDich = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btHoSoNullToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(giaoDich);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}