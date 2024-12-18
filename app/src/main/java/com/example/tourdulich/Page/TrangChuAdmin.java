package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrangChuAdmin extends AppCompatActivity {

    private TextView txtWelcome;
    private Button btnDangXuat;
    private ImageView btnQLUser;
    private ImageView btnQLDanhMuc;
    private ImageView btnQLTour;
    private ImageView btnQLDonDat;

    private FirebaseAuth xacThucFirebase;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_trang_chu_admin);

        txtWelcome = findViewById(R.id.txtWelcomeAdmin);
        btnDangXuat = findViewById(R.id.btnDangXuatAdmin);
        btnQLUser = findViewById(R.id.imgVQuanLyUser);
        btnQLDanhMuc = findViewById(R.id.imgVQuanLyDanhMuc);
        btnQLTour = findViewById(R.id.imgVQuanLyTour);
        btnQLDonDat = findViewById(R.id.imgVQuanLyDonDat);

        xacThucFirebase = FirebaseAuth.getInstance();
        firebaseUser = xacThucFirebase.getCurrentUser();

        showTT();

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trangChu = new Intent(TrangChuAdmin.this, TrangChu.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(trangChu);
                finish();
            }
        });

        btnQLUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qlUser = new Intent(TrangChuAdmin.this, QuanLyUser.class);
                startActivity(qlUser);
            }
        });

        btnQLDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qlDanhMuc = new Intent(TrangChuAdmin.this, QuanLyDanhMuc.class);
                startActivity(qlDanhMuc);
            }
        });

        btnQLTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qlTour = new Intent(TrangChuAdmin.this, QuanLyTour.class);
                startActivity(qlTour);
            }
        });

        btnQLDonDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qlDonDat = new Intent(TrangChuAdmin.this, QuanLyDonDat.class);
                startActivity(qlDonDat);
            }
        });
    }

    private void showTT(){
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser thongTinUser = snapshot.getValue(LuuThongTinUser.class);
                txtWelcome.setText(String.format("Chào mừng %s",thongTinUser.tenNguoiDung));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
