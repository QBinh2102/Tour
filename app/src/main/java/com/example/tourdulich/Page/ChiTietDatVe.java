package com.example.tourdulich.Page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.BaiDanhGia;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietDatVe extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnThanhToan;
    private TextView txtTen;
    private TextView txtGia;
    private TextView txtNgayKhoiHanh;
    private TextView txtNgayKetThuc;
    private TextView txtTongTien;
    private TextView txtSoLuongVeDat;
    private ImageView imgTour;
    private ImageView btnGiamSoVe;
    private ImageView btnTangSoVe;

    DatabaseReference refDuLieu = FirebaseDatabase.getInstance().getReference("Bài đánh giá");

    //Kiểm tra bài đánh giá đã tồn tại
    boolean flag = false;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

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

        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");

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

        //Show thông tin tour
        showThongTinTour(tour);

        Intent ql = new Intent(this, ThongTinDatVe.class);
        ql.putExtra("tour_item", tour);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ql);
            }
        });

        btnGiamSoVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(txtSoLuongVeDat.getText().toString());
                if (currentQuantity != 1) {
                    int newQuantity = currentQuantity - 1;
                    txtSoLuongVeDat.setText(String.valueOf(newQuantity));
                    int giaVe = Integer.parseInt(tour.giaTien);
                    int tongTien = giaVe * newQuantity;
                    String formattedPrice = formatPrice(String.valueOf(tongTien));
                    txtTongTien.setText(formattedPrice);
                }
            }
        });

        btnTangSoVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(txtSoLuongVeDat.getText().toString());
                if (currentQuantity < tour.soLuongVe) {
                    int newQuantity = currentQuantity + 1;
                    txtSoLuongVeDat.setText(String.valueOf(newQuantity));
                    int giaVe = Integer.parseInt(tour.giaTien);
                    int tongTien = giaVe * newQuantity;
                    String formattedPrice = formatPrice(String.valueOf(tongTien));
                    txtTongTien.setText(formattedPrice);
                }
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDuLieu.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            BaiDanhGia tmp = dataSnapshot.getValue(BaiDanhGia.class);
                            if (tmp.idTour.equals(tour.idTour)) {
                                if(tmp.idUser.equals(User.getUid())) {
                                    flag = true;
                                    int soVeDat = Integer.parseInt(txtSoLuongVeDat.getText().toString());
                                    int giaVe = Integer.parseInt(tour.giaTien);
                                    int tongTien = giaVe * soVeDat;
                                    DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
                                    tourRef.child(tour.idTour).child("soLuongVe").setValue(tour.soLuongVe - soVeDat);
                                    refDuLieu.child(tmp.idBaiDanhGia).child("soVe").setValue(tmp.soVe + soVeDat);
                                    refDuLieu.child(tmp.idBaiDanhGia).child("tongTien").setValue(tmp.tongTien + tongTien);
                                    Toast.makeText(ChiTietDatVe.this, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                                    Intent datVe = new Intent(ChiTietDatVe.this, DatVe.class);
                                    startActivity(datVe);
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String idUser = User.getUid();
                            String idTour = tour.idTour;
                            String idBDG = auth.getUid() + System.currentTimeMillis();
                            int soVeDat = Integer.parseInt(txtSoLuongVeDat.getText().toString());
                            int giaVe = Integer.parseInt(tour.giaTien);
                            int tongTien = giaVe * soVeDat;
                            BaiDanhGia baiDanhGia = new BaiDanhGia(idBDG, idUser, idTour, soVeDat, tongTien);
                            refDuLieu.child(idBDG).setValue(baiDanhGia).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
                                    tourRef.child(idTour).child("soLuongVe").setValue(tour.soLuongVe - soVeDat);
                                    Toast.makeText(ChiTietDatVe.this, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                                    Intent datVe = new Intent(ChiTietDatVe.this, DatVe.class);
                                    startActivity(datVe);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChiTietDatVe.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showThongTinTour(Tour tour) {
        Uri uri = Uri.parse(tour.hinhTour);
        Glide.with(ChiTietDatVe.this)
                .load(uri)
                .into(imgTour);
        txtTen.setText(tour.tenTour);
        txtGia.setText(tour.giaTien);
        txtNgayKhoiHanh.setText(tour.ngayKhoiHanh);
        txtNgayKetThuc.setText(tour.ngayKetThuc);
        String formattedPrice = formatPrice(tour.giaTien);
        txtTongTien.setText(formattedPrice);
    }

    private String formatPrice(String price) {
        try {
            // Xóa tất cả các dấu phẩy và khoảng trắng, rồi chia giá thành các phần
            long priceValue = Long.parseLong(price);  // Chuyển giá sang long để xử lý

            // Sử dụng NumberFormat để định dạng giá tiền
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
            String formatted = numberFormat.format(priceValue);
            return formatted.replace(',', ' ');
        } catch (NumberFormatException e) {
            return price;  // Nếu không thể chuyển đổi, giữ nguyên giá gốc
        }
    }
}