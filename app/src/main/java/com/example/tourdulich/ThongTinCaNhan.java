package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongTinCaNhan extends AppCompatActivity {

    private LinearLayout btnToiTrangChu;
    private TextView txtEmail;
    private TextView txtTenHoSo;
    private TextView txtDienThoai;
    private TextView txtDiaChi;
    private ProgressBar thanhTienTrinh;
    private String email, dienThoai, diaChi, tenHoSo;
    private ImageView imageView;
    private FirebaseAuth xacThucFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Chuyển sang TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnToiTrangChu = findViewById(R.id.btHoSoToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
            }
        });

        txtTenHoSo = findViewById(R.id.txtTenHoSo);
        txtEmail = findViewById(R.id.txtEmailHoSo);
        txtDienThoai = findViewById(R.id.txtDienThoaiHoSo);
        txtDiaChi = findViewById(R.id.txtDiaChiHoSo);
        xacThucFirebase = FirebaseAuth.getInstance();
        thanhTienTrinh = findViewById(R.id.thanhTienTrinh);

        FirebaseUser firebaseUser = xacThucFirebase.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(ThongTinCaNhan.this, "Thông tin người dùng không tồn tại",
                    Toast.LENGTH_SHORT).show();
        }else {
            thanhTienTrinh.setVisibility(View.VISIBLE);
            HienThiThongTinUser(firebaseUser);
        }

    }

    private void HienThiThongTinUser(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        Log.d("TAG", "User ID: " + userID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser thongTinUser = snapshot.getValue(LuuThongTinUser.class);
                if(thongTinUser != null){
                    tenHoSo = firebaseUser.getDisplayName();
                    email = thongTinUser.email;
                    diaChi = thongTinUser.diaChi;
                    dienThoai = thongTinUser.soDienThoai;

                    txtTenHoSo.setText(tenHoSo);
                    txtEmail.setText(email);
                    txtDiaChi.setText(diaChi);
                    txtDienThoai.setText(dienThoai);
                }else{
                    Log.d("TAG", "thongTinUser is null");
                }
                thanhTienTrinh.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinCaNhan.this, "Có lỗi xảy ra",
                        Toast.LENGTH_SHORT).show();
                thanhTienTrinh.setVisibility(View.GONE);
            }
        });
    }
}