package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.Adapter.DanhGiaAdapter;
import com.example.tourdulich.Database.BaiDanhGia;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class XemBinhLuanChuaDangNhap extends AppCompatActivity {

    private Button btnQuayLai;
    private ListView lvDanhGia;

    private DatabaseReference bdgRef = FirebaseDatabase.getInstance().getReference("Bài đánh giá");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_binh_luan_chua_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnQuayLai = findViewById(R.id.btQuayLaiTuDanhGiaChuaDangNhap);
        lvDanhGia = findViewById(R.id.listViewTatCaDanhGiaCDN);

        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");

        //Show danh sách đánh giá
        showDSDanhGia();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemBinhLuanChuaDangNhap.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tour);
                startActivity(intent);
            }
        });
    }

    private void showDSDanhGia() {
        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");
        ArrayList<BaiDanhGia> baiDanhGias = new ArrayList<>();
        bdgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        BaiDanhGia baiDanhGia = dataSnapshot.getValue(BaiDanhGia.class);
                        if(baiDanhGia.idTour.equals(tour.idTour)&&baiDanhGia.trangThai.equals("Đã thanh toán")){
                            baiDanhGias.add(baiDanhGia);
                        }
                    }
                    if(baiDanhGias.isEmpty()){
                        Toast.makeText(XemBinhLuanChuaDangNhap.this, "Chưa có đánh giá nào!", Toast.LENGTH_SHORT).show();
                    }else {
                        DanhGiaAdapter danhGiaAdapter = new DanhGiaAdapter(XemBinhLuanChuaDangNhap.this, baiDanhGias, baiDanhGias.size());
                        lvDanhGia.setAdapter(danhGiaAdapter);
                    }
                }else{
                       //Toast.makeText(XemBinhLuanChuaDangNhap.this, "Chưa có đánh giá nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(XemBinhLuanChuaDangNhap.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}