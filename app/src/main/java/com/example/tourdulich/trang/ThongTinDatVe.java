package com.example.tourdulich.trang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourdulich.Adapter.DanhGiaAdapter;
import com.example.tourdulich.Adapter.ImageAdapter;
import com.example.tourdulich.Adapter.TourAdapter;
import com.example.tourdulich.CSDL.BaiDanhGia;
import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;
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
import java.util.ArrayList;
import java.util.Locale;


public class ThongTinDatVe extends AppCompatActivity {

    private TextView tenTour;
    private TextView giaTour;
    private TextView soBinhLuan;
    private TextView gioiThieu;
    private TextView thoiGian;
    private TextView phuongTien;
    private TextView soVe;
    private TextView tbSao;
    private Button btnDatVe;
    private ListView lvBinhLuan;
    private ProgressBar sao1;
    private ProgressBar sao2;
    private ProgressBar sao3;
    private ProgressBar sao4;
    private ProgressBar sao5;


    private ViewPager2 viewPager2;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<BaiDanhGia> baiDanhGias = new ArrayList<>();

    private ProgressBar progressBar;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Bài đánh giá");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_dat_ve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Lấy thông tin tour khi chọn từ ĐẶT VÉ
        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour_item");

        tenTour = findViewById(R.id.textViewTTTenTour);
        giaTour = findViewById(R.id.textViewTTGiaTour);
        soBinhLuan = findViewById(R.id.textViewTTSoBinhLuan);
        gioiThieu = findViewById(R.id.textViewTTGioiThieu);
        thoiGian = findViewById(R.id.textViewTTThoiGian);
        phuongTien = findViewById(R.id.textViewTTPhuongTien);
        soVe = findViewById(R.id.textViewTTSoVe);
        btnDatVe = findViewById(R.id.btDatVe);
        tbSao = findViewById(R.id.textViewTTTrungBinhSao);
        sao1 = findViewById(R.id.progressBar1Sao);
        sao2 = findViewById(R.id.progressBar2Sao);
        sao3 = findViewById(R.id.progressBar3Sao);
        sao4 = findViewById(R.id.progressBar4Sao);
        sao5 = findViewById(R.id.progressBar5Sao);

        lvBinhLuan = findViewById(R.id.listViewBinhLuanTour);
        viewPager2 = findViewById(R.id.viewPager2);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        storage = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
        storageRef = storage.getReference().child("imagesTour/"+tour.tenTour);

        //Show thông tin tour
        getImageUris(tour);

        // Khởi tạo TextView "Quay lại" và thiết lập sự kiện onClick
        TextView txtQuayLai = findViewById(R.id.txtReturn);
        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinDatVe.this, DatVe.class); // Chuyển sang màn hình Trang Chủ
                startActivity(intent);
                finish(); // Đóng Activity hiện tại để tránh quay lại màn hình này khi bấm nút Back
            }
        });

        btnDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser!=null){
                    Intent ctdv = new Intent(ThongTinDatVe.this, ChiTietDatVe.class);
                    ctdv.putExtra("tour",tour);
                    startActivity(ctdv);
                }else{
                    Toast.makeText(ThongTinDatVe.this,"Bạn cần đăng nhập trước khi đặt vé!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getImageUris(Tour tour) {
        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // Lặp qua tất cả các item (ảnh)
                    for (StorageReference item : listResult.getItems()) {
                        item.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    imageUris.add(uri); // Thêm Uri vào danh sách
                                    if (imageUris.size() == listResult.getItems().size()) {
                                        setupViewPager(tour); // Sau khi có đủ Uri, thiết lập ViewPager2
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirebaseStorage", "Error getting Uri: ", e);
                                });
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseStorage", "Error listing files: ", e);
                });
    }

    private void setupViewPager(Tour tour) {
        ImageAdapter adapter = new ImageAdapter(imageUris);
        viewPager2.setAdapter(adapter);
        showThongTinTour(tour);
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void showThongTinTour(Tour tour) {

        tenTour.setText(tour.tenTour);
        String formattedPrice = formatPrice(String.valueOf(tour.giaTien));
        giaTour.setText(String.format("Giá: %s",formattedPrice));
        tbSao.setText(String.format("%.1f",tour.soSao));
        soBinhLuan.setText(String.format("%d bình luận", tour.soBinhLuan));
        gioiThieu.setText(tour.gioiThieu);
        thoiGian.setText(String.format("Thời gian: %s - %s", tour.ngayKhoiHanh,tour.ngayKetThuc));
        phuongTien.setText(String.format("Phương tiện: %s", tour.phuongTien));
        soVe.setText(String.format("Số vé còn lại: %d",tour.soLuongVe));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int countTong = 0;
                int soSao1 = 0;
                int soSao2 = 0;
                int soSao3 = 0;
                int soSao4 = 0;
                int soSao5 = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BaiDanhGia baiDanhGia = snapshot.getValue(BaiDanhGia.class);
                        if(baiDanhGia.idTour.equals(tour.idTour)){
                            baiDanhGias.add(baiDanhGia);

                            if(baiDanhGia.soSao!=0){
                                countTong++;
                                if(baiDanhGia.soSao == 1){
                                    soSao1++;
                                } else if (baiDanhGia.soSao == 2) {
                                    soSao2++;
                                } else if (baiDanhGia.soSao == 3) {
                                    soSao3++;
                                } else if (baiDanhGia.soSao == 4) {
                                    soSao4++;
                                } else if (baiDanhGia.soSao == 5) {
                                    soSao5++;
                                }
                            }
                        }
                    }
                    DanhGiaAdapter danhGiaAdapter = new DanhGiaAdapter(ThongTinDatVe.this, baiDanhGias, 3);
                    lvBinhLuan.setAdapter(danhGiaAdapter);
                    sao1.setProgress((soSao1/countTong)*100);
                    sao2.setProgress((soSao2/countTong)*100);
                    sao3.setProgress((soSao3/countTong)*100);
                    sao4.setProgress((soSao4*countTong)*100);
                    sao5.setProgress((soSao5/countTong)*100);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ThongTinDatVe.this, "Không có tour nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ThongTinDatVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
