package com.example.tourdulich.trang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TinTuc extends AppCompatActivity {

    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiGiaoDich;

    private ImageView imgSanPham;
    private Button btnUp;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference dbRefTour;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    // Khai báo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    imageUri = uri;
                    imgSanPham.setVisibility(View.VISIBLE);
                    imgSanPham.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tin_tuc);

        dbRefTour = FirebaseDatabase.getInstance().getReference("Tour");
        firebaseStorage = FirebaseStorage.getInstance();
        btnUp = findViewById(R.id.buttonUpload);
        imgSanPham = findViewById(R.id.imageView2);
        imgSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // Sử dụng ActivityResultLauncher thay cho startActivityForResult
                selectImage.launch(intent);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    // Tải ảnh lên Firebase Storage
                    StorageReference storageRef = firebaseStorage.getReference().child("imagesTour/" + System.currentTimeMillis() + ".jpg");
                    UploadTask uploadTask = storageRef.putFile(imageUri);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của ảnh đã upload
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Tạo đối tượng sản phẩm
                            String idTour = dbRefTour.push().getKey();
                            Tour tour = new Tour(idTour,"Tour Phú Quốc",imageUrl,"Thuyền",
                                    "Thuyền","18/12/2024", "20/12/2024","1.000.000",20);

                            // Thêm sản phẩm vào Firebase Database
                            if (idTour != null) {
                                dbRefTour.child(idTour).setValue(tour)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(TinTuc.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                finish(); // Trở về màn hình trước
                                            } else {
                                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                                                Toast.makeText(TinTuc.this, "Lỗi khi thêm sản phẩm: " + errorMessage, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(TinTuc.this, "Tải ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });



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