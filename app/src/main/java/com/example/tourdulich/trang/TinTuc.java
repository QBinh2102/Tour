package com.example.tourdulich.trang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TinTuc extends AppCompatActivity {

    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiGiaoDich;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tin_tuc);

        //Chuyển Trang Thông Tin Cá Nhân
        if(firebaseUser != null) {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu đã đăng nhập thành công
            Intent ttcn = new Intent(this, ThongTinCaNhan.class);
            btnToiHoSo = findViewById(R.id.btTinTucToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        }else {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu chưa đăng nhập
            Intent ttcncdn = new Intent(this, ThongTinChuaDangNhap.class);
            btnToiHoSo = findViewById(R.id.btTinTucToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcncdn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        }

        //Chuyển sang TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnToiTrangChu = findViewById(R.id.btTinTucToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang ĐẶT VÉ
        Intent datve = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btTinTucToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(datve);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent giaoDich = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btTinTucToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(giaoDich);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}