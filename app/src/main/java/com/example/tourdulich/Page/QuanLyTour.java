package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import com.example.tourdulich.Adapter.CreateTourAdapter;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyTour extends AppCompatActivity {

    private Button btnThem;
    private Button btnQuayLai;
    private ListView lvTour;
    private CreateTourAdapter tourAdapter;
    private ArrayList<Tour> arrayTour;
    private ProgressBar progressBar;

    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_tour);

        btnThem = findViewById(R.id.btnThemTour);
        btnQuayLai = findViewById(R.id.btnQuayLaiTuQuanLyTour);
        lvTour = findViewById(R.id.listTour);
        progressBar = findViewById(R.id.progressBar10);
        progressBar.setVisibility(View.VISIBLE);
        arrayTour = new ArrayList<>();

        showDanhSach();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(QuanLyTour.this, TrangChuAdmin.class);
                startActivity(ql);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(QuanLyTour.this, AddTour.class);
                startActivity(add);
            }
        });

        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tour tour = arrayTour.get(position);
                Intent intent = new Intent(QuanLyTour.this, CapNhatTour.class);
                intent.putExtra("tour",tour);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDanhSach() {
        tourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayTour.clear(); // Xóa danh sách cũ trước khi tải mới

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy từng tour từ Firebase
                        Tour tour = snapshot.getValue(Tour.class);
                        if (tour != null) {
                            arrayTour.add(tour); // Thêm vào danh sách nếu hợp lệ
                        }
                    }

                    // Kiểm tra danh sách sau khi tải
                    if (!arrayTour.isEmpty()) {
                        // Gắn adapter và hiển thị dữ liệu
                        if (tourAdapter == null) {
                            tourAdapter = new CreateTourAdapter(QuanLyTour.this, arrayTour);
                            lvTour.setAdapter(tourAdapter);
                        } else {
                            tourAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(QuanLyTour.this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuanLyTour.this, "Không có danh mục nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }

                // Ẩn progress bar sau khi tải xong
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Ẩn progress bar nếu xảy ra lỗi
                Toast.makeText(QuanLyTour.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}