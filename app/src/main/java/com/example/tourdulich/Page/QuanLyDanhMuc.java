package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.Adapter.DanhMucAdapter;
import com.example.tourdulich.Adapter.UserAdapter;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyDanhMuc extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnThem;
    private ListView lvDanhMuc;
    private ArrayList<DanhMuc> arrayDanhMuc;
    private DanhMucAdapter danhMucAdapter;
    private ProgressBar progressBar;

    private DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference("Danh mục");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        btnQuayLai = findViewById(R.id.btnQuayLaiTuQuanLyDanhMuc);
        btnThem = findViewById(R.id.btnThemDanhMuc);
        lvDanhMuc = findViewById(R.id.listDanhMuc);
        arrayDanhMuc = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar7);
        progressBar.setVisibility(View.VISIBLE);

        showDanhSach();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(QuanLyDanhMuc.this, TrangChuAdmin.class);
                startActivity(ql);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDanhSach() {
        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayDanhMuc.clear(); // Xóa danh sách cũ trước khi tải mới

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy từng tour từ Firebase
                        DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                        if (danhMuc != null) {
                            arrayDanhMuc.add(danhMuc); // Thêm vào danh sách nếu hợp lệ
                        }
                    }

                    // Kiểm tra danh sách sau khi tải
                    if (!arrayDanhMuc.isEmpty()) {
                        // Gắn adapter và hiển thị dữ liệu
                        if (danhMucAdapter == null) {
                            danhMucAdapter = new DanhMucAdapter(QuanLyDanhMuc.this, arrayDanhMuc);
                            lvDanhMuc.setAdapter(danhMucAdapter);
                        } else {
                            danhMucAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(QuanLyDanhMuc.this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuanLyDanhMuc.this, "Không có danh mục nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }

                // Ẩn progress bar sau khi tải xong
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Ẩn progress bar nếu xảy ra lỗi
                Toast.makeText(QuanLyDanhMuc.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}