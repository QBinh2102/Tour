package com.example.tourdulich.Page;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourdulich.Adapter.UserAdapter;
import com.example.tourdulich.Adapter.GiaoDichAdapter;
import com.example.tourdulich.Database.BaiDanhGia;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuanLyDonDat extends AppCompatActivity {

    private Button btnQuayLai;
    private ListView lvUser, lvLSGD;
    private ProgressBar progressBar;

    private ArrayList<LuuThongTinUser> arrayUser;
    private ArrayList<BaiDanhGia> arrayLSGD;

    private UserAdapter userAdapter;
    private GiaoDichAdapter giaoDichAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_don_dat);

        btnQuayLai = findViewById(R.id.btnQuayLaiTuQuanLyDonDat);
        lvUser = findViewById(R.id.listUser);
        lvLSGD = findViewById(R.id.listLSGD);
        progressBar = findViewById(R.id.progressBar);

        arrayUser = new ArrayList<>();
        arrayLSGD = new ArrayList<>();

        btnQuayLai.setOnClickListener(v -> finish());

        // Load dữ liệu người dùng
        loadUserData();

        // Xử lý chọn người dùng
        lvUser.setOnItemClickListener((adapterView, view, position, id) -> {
            LuuThongTinUser selectedUser = arrayUser.get(position);
            loadLSGD(selectedUser.id);
        });

        // Xử lý chọn đánh giá trong danh sách giao dịch (xóa nếu cần)
        lvLSGD.setOnItemClickListener((adapterView, view, position, id) -> {
            BaiDanhGia selectedGiaoDich = arrayLSGD.get(position);
            xoaDanhGia(selectedGiaoDich);
        });
    }

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayUser.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    LuuThongTinUser user = data.getValue(LuuThongTinUser.class);
                    arrayUser.add(user);
                }

                userAdapter = new UserAdapter(QuanLyDonDat.this, arrayUser);
                lvUser.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyDonDat.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadLSGD(String userId) {
        progressBar.setVisibility(View.VISIBLE);
        lvUser.setVisibility(View.GONE);
        lvLSGD.setVisibility(View.VISIBLE);

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("donDatHang");

        orderRef.orderByChild("idUser").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayLSGD.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    BaiDanhGia giaoDich = data.getValue(BaiDanhGia.class);
                    arrayLSGD.add(giaoDich);
                }

                giaoDichAdapter = new GiaoDichAdapter(QuanLyDonDat.this, arrayLSGD);
                lvLSGD.setAdapter(giaoDichAdapter);
                progressBar.setVisibility(View.GONE);

                if (arrayLSGD.isEmpty()) {
                    Toast.makeText(QuanLyDonDat.this, "Không có giao dịch nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyDonDat.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void xoaDanhGia(BaiDanhGia giaoDich) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("donDatHang");

        dbRef.child(giaoDich.idBaiDanhGia).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Xóa đánh giá thành công!", Toast.LENGTH_SHORT).show();
                    arrayLSGD.remove(giaoDich);
                    giaoDichAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
