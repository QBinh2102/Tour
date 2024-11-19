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

public class GiaoDich extends AppCompatActivity {

    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiHoSo;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giao_dich);

        //Chuyển Trang Thông Tin Cá Nhân
        if(firebaseUser != null) {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu đã đăng nhập thành công
            Intent ttcn = new Intent(this, ThongTinCaNhan.class);
            btnToiHoSo = findViewById(R.id.btGiaoDichToiHoSo);
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
            btnToiHoSo = findViewById(R.id.btGiaoDichToiHoSo);
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
        btnToiTrangChu = findViewById(R.id.btGiaoDichToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btGiaoDichToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang ĐẶT VÉ
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btGiaoDichToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(datVe);
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