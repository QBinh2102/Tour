package com.example.tourdulich.Page;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TrangChuAdmin extends AppCompatActivity {
    private TextView lbWelcome;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;
    private ImageView img1, img2, img3;
    private TextView txtTen1, txtTen2, txtTen3;
    private ImageView sao1NB1, sao2NB1, sao3NB1, sao4NB1, sao5NB1;
    private ImageView sao1NB2, sao2NB2, sao3NB2, sao4NB2, sao5NB2;
    private ImageView sao1NB3, sao2NB3, sao3NB3, sao4NB3, sao5NB3;
    private TextView soLuongDat1, soLuongDat2, soLuongDat3;
    private TextView gia1, gia2, gia3;
    private CardView c1, c2, c3;
    private ProgressBar progressBar1, progressBar2, progressBar3;

    private ArrayList<Tour> tours = new ArrayList<>();

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
    private String hoTen;
    private LinearLayout danhMucContainer;
    ArrayList<DanhMuc> danhMucList = new ArrayList<>();
    private Uri selectedImageUri; // Dùng để lưu trữ URI của ảnh được chọn
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        img1 = findViewById(R.id.imageViewNoiBat1);
        img2 = findViewById(R.id.imageViewNoiBat2);
        img3 = findViewById(R.id.imageViewNoiBat3);
        txtTen1 = findViewById(R.id.textViewTenNoiBat1);
        txtTen2 = findViewById(R.id.textViewTenNoiBat2);
        txtTen3 = findViewById(R.id.textViewTenNoiBat3);

        sao1NB1 = findViewById(R.id.imageView1SaoNoiBat1);
        sao2NB1 = findViewById(R.id.imageView2SaoNoiBat1);
        sao3NB1 = findViewById(R.id.imageView3SaoNoiBat1);
        sao4NB1 = findViewById(R.id.imageView4SaoNoiBat1);
        sao5NB1 = findViewById(R.id.imageView5SaoNoiBat1);

        sao1NB2 = findViewById(R.id.imageView1SaoNoiBat2);
        sao2NB2 = findViewById(R.id.imageView2SaoNoiBat2);
        sao3NB2 = findViewById(R.id.imageView3SaoNoiBat2);
        sao4NB2 = findViewById(R.id.imageView4SaoNoiBat2);
        sao5NB2 = findViewById(R.id.imageView5SaoNoiBat2);

        sao1NB3 = findViewById(R.id.imageView1SaoNoiBat3);
        sao2NB3 = findViewById(R.id.imageView2SaoNoiBat3);
        sao3NB3 = findViewById(R.id.imageView3SaoNoiBat3);
        sao4NB3 = findViewById(R.id.imageView4SaoNoiBat3);
        sao5NB3 = findViewById(R.id.imageView5SaoNoiBat3);

        soLuongDat1 = findViewById(R.id.textViewLuongDatVeNoiBat1);
        soLuongDat2 = findViewById(R.id.textViewLuongDatVeNoiBat2);
        soLuongDat3 = findViewById(R.id.textViewLuongDatVeNoiBat3);
        gia1 = findViewById(R.id.textViewGiaNoiBat1);
        gia2 = findViewById(R.id.textViewGiaNoiBat2);
        gia3 = findViewById(R.id.textViewGiaNoiBat3);

        c1 = findViewById(R.id.cardViewNoiBat1);
        c2 = findViewById(R.id.cardViewNoiBat2);
        c3 = findViewById(R.id.cardViewNoiBat3);

        progressBar1 = findViewById(R.id.progressBarNB1);
        progressBar2 = findViewById(R.id.progressBarNB2);
        progressBar3 = findViewById(R.id.progressBarNB3);

        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        progressBar3.setVisibility(View.VISIBLE);

        //show thông tin tour nổi bật
        showTT();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy UID của người dùng
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("role", "admin");  // Hoặc "user" tùy vào vai trò của người dùng

        // Lưu vai trò người dùng vào Firebase
        myRef.child(userId).updateChildren(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User role updated successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.d("Firebase", "Failed to update user role", e);
                });

        if (firebaseUser != null) {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu đã đăng nhập thành công
            Intent ttcn = new Intent(this, ThongTinCaNhan.class);
            btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        } else {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu chưa đăng nhập
            Intent ttcncdn = new Intent(this, ThongTinChuaDangNhap.class);
            btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcncdn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        }
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuAdmin.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tours.get(0));
                startActivity(intent);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuAdmin.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tours.get(1));
                startActivity(intent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuAdmin.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tours.get(2));
                startActivity(intent);
            }
        });
//        c4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TrangChu.this,ThongTinDatVe.class);
//                intent.putExtra("tour_item",tours.get(3));
//                startActivity(intent);
//            }
//        });

        danhMucContainer = findViewById(R.id.layoutDanhMuc);

        loadDanhMuc();
        //Hiển thị chào mừng người dùng trên trang chủ
        lbWelcome = findViewById(R.id.textViewWelcome);
        if (firebaseUser != null) {
            hoTen = firebaseUser.getDisplayName();
            lbWelcome.setText("Chào mừng " + hoTen);
        }
        //Chuyển sang trang ĐẶT VÉ
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btTrangChuToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(datVe);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btTrangChuToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent gd = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btTrangChuToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(gd);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });
        ImageView imgVPopupMenu = findViewById(R.id.imgViewPopupMenu);
        imgVPopupMenu.setOnClickListener(view -> {
            Dialog dialog = new Dialog(TrangChuAdmin.this);
            dialog.setContentView(R.layout.dialog_menu_danh_muc);
            dialog.setCancelable(true);  // Cho phép đóng dialog khi bấm ra ngoài

            Button btnCong = dialog.findViewById(R.id.menu_add);

            btnCong.setOnClickListener(v -> {
                themDanhMuc();
                dialog.dismiss();
            });

            dialog.show();
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
    private void showTT(){
        tourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Tour tour = dataSnapshot.getValue(Tour.class);
                        tours.add(tour);
                    }
                }
                for(int i = 0;i<tours.size()-1;i++){
                    for(int j = i+1;j<tours.size();j++){
                        if(tours.get(i).soSao<tours.get(j).soSao){
                            Tour tmp = tours.get(i);
                            tours.set(i,tours.get(j));
                            tours.set(j,tmp);
                        } else if (tours.get(i).soSao==tours.get(j).soSao) {
                            if(tours.get(i).soLuongDat<tours.get(j).soLuongDat){
                                Tour tmp = tours.get(i);
                                tours.set(i,tours.get(j));
                                tours.set(j,tmp);
                            }
                        }
                    }
                }
                Glide.with(TrangChuAdmin.this)
                        .load(Uri.parse(tours.get(0).hinhTour))
                        .into(img1);
                txtTen1.setText(tours.get(0).tenTour);
                soLuongDat1.setText(String.format("%d lượt đặt",tours.get(0).soLuongDat));
                String formattedPrice1 = formatPrice(String.valueOf(tours.get(0).giaTien));
                gia1.setText(String.format("Giá: %s", formattedPrice1));
                double soSao1 = tours.get(0).soSao;
                if(soSao1==0){
                    sao1NB1.setImageResource(R.drawable.star_border_24);
                    sao2NB1.setImageResource(R.drawable.star_border_24);
                    sao3NB1.setImageResource(R.drawable.star_border_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>0&&soSao1<0.8) {
                    sao1NB1.setImageResource(R.drawable.star_half_24);
                    sao2NB1.setImageResource(R.drawable.star_border_24);
                    sao3NB1.setImageResource(R.drawable.star_border_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=0.8&&soSao1<1.2) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_border_24);
                    sao3NB1.setImageResource(R.drawable.star_border_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=1.2&&soSao1<1.8) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_half_24);
                    sao3NB1.setImageResource(R.drawable.star_border_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=1.8&&soSao1<2.2) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_border_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=2.2&&soSao1<2.8) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_half_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=2.8&&soSao1<3.2) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_24);
                    sao4NB1.setImageResource(R.drawable.star_border_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=3.2&&soSao1<3.8) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_24);
                    sao4NB1.setImageResource(R.drawable.star_half_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=3.8&&soSao1<4.2) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_24);
                    sao4NB1.setImageResource(R.drawable.star_24);
                    sao5NB1.setImageResource(R.drawable.star_border_24);
                } else if (soSao1>=4.2&&soSao1<4.8) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_24);
                    sao4NB1.setImageResource(R.drawable.star_24);
                    sao5NB1.setImageResource(R.drawable.star_half_24);
                } else if (soSao1>=4.8) {
                    sao1NB1.setImageResource(R.drawable.star_24);
                    sao2NB1.setImageResource(R.drawable.star_24);
                    sao3NB1.setImageResource(R.drawable.star_24);
                    sao4NB1.setImageResource(R.drawable.star_24);
                    sao5NB1.setImageResource(R.drawable.star_24);
                }

                Glide.with(TrangChuAdmin.this)
                        .load(Uri.parse(tours.get(1).hinhTour))
                        .into(img2);
                txtTen2.setText(tours.get(1).tenTour);
                soLuongDat2.setText(String.format("%d lượt đặt",tours.get(1).soLuongDat));
                String formattedPrice2 = formatPrice(String.valueOf(tours.get(1).giaTien));
                gia2.setText(String.format("Giá: %s", formattedPrice2));
                double soSao2 = tours.get(1).soSao;
                if(soSao2==0){
                    sao1NB2.setImageResource(R.drawable.star_border_24);
                    sao2NB2.setImageResource(R.drawable.star_border_24);
                    sao3NB2.setImageResource(R.drawable.star_border_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>0&&soSao2<0.8) {
                    sao1NB2.setImageResource(R.drawable.star_half_24);
                    sao2NB2.setImageResource(R.drawable.star_border_24);
                    sao3NB2.setImageResource(R.drawable.star_border_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=0.8&&soSao2<1.2) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_border_24);
                    sao3NB2.setImageResource(R.drawable.star_border_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=1.2&&soSao2<1.8) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_half_24);
                    sao3NB2.setImageResource(R.drawable.star_border_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=1.8&&soSao2<2.2) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_border_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=2.2&&soSao2<2.8) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_half_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=2.8&&soSao2<3.2) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_24);
                    sao4NB2.setImageResource(R.drawable.star_border_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=3.2&&soSao2<3.8) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_24);
                    sao4NB2.setImageResource(R.drawable.star_half_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=3.8&&soSao2<4.2) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_24);
                    sao4NB2.setImageResource(R.drawable.star_24);
                    sao5NB2.setImageResource(R.drawable.star_border_24);
                } else if (soSao2>=4.2&&soSao2<4.8) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_24);
                    sao4NB2.setImageResource(R.drawable.star_24);
                    sao5NB2.setImageResource(R.drawable.star_half_24);
                } else if (soSao2>=4.8) {
                    sao1NB2.setImageResource(R.drawable.star_24);
                    sao2NB2.setImageResource(R.drawable.star_24);
                    sao3NB2.setImageResource(R.drawable.star_24);
                    sao4NB2.setImageResource(R.drawable.star_24);
                    sao5NB2.setImageResource(R.drawable.star_24);
                }

                Glide.with(TrangChuAdmin.this)
                        .load(Uri.parse(tours.get(2).hinhTour))
                        .into(img3);
                txtTen3.setText(tours.get(2).tenTour);
                soLuongDat3.setText(String.format("%d lượt đặt",tours.get(2).soLuongDat));
                String formattedPrice3 = formatPrice(String.valueOf(tours.get(2).giaTien));
                gia3.setText(String.format("Giá: %s", formattedPrice3));
                double soSao3 = tours.get(2).soSao;
                if(soSao3==0){
                    sao1NB3.setImageResource(R.drawable.star_border_24);
                    sao2NB3.setImageResource(R.drawable.star_border_24);
                    sao3NB3.setImageResource(R.drawable.star_border_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>0&&soSao3<0.8) {
                    sao1NB3.setImageResource(R.drawable.star_half_24);
                    sao2NB3.setImageResource(R.drawable.star_border_24);
                    sao3NB3.setImageResource(R.drawable.star_border_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=0.8&&soSao3<1.2) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_border_24);
                    sao3NB3.setImageResource(R.drawable.star_border_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=1.2&&soSao3<1.8) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_half_24);
                    sao3NB3.setImageResource(R.drawable.star_border_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=1.8&&soSao3<2.2) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_border_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=2.2&&soSao3<2.8) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_half_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=2.8&&soSao3<3.2) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_24);
                    sao4NB3.setImageResource(R.drawable.star_border_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=3.2&&soSao3<3.8) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_24);
                    sao4NB3.setImageResource(R.drawable.star_half_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=3.8&&soSao3<4.2) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_24);
                    sao4NB3.setImageResource(R.drawable.star_24);
                    sao5NB3.setImageResource(R.drawable.star_border_24);
                } else if (soSao3>=4.2&&soSao3<4.8) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_24);
                    sao4NB3.setImageResource(R.drawable.star_24);
                    sao5NB3.setImageResource(R.drawable.star_half_24);
                } else if (soSao3>=4.8) {
                    sao1NB3.setImageResource(R.drawable.star_24);
                    sao2NB3.setImageResource(R.drawable.star_24);
                    sao3NB3.setImageResource(R.drawable.star_24);
                    sao4NB3.setImageResource(R.drawable.star_24);
                    sao5NB3.setImageResource(R.drawable.star_24);
                }

                progressBar1.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);
                progressBar3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void loadDanhMuc() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Danh mục");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                if (danhMuc != null) {
                    addDanhMucToLayout(danhMuc.ten, danhMuc.hinh); // Add to layout
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Handle category update if necessary
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle category removal if necessary
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Handle child move if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Cannot load categories: " + error.getMessage());
            }
        });
    }

    private void addDanhMucToLayout(String danhMuc, String hinhUrl) {
        LinearLayout container = findViewById(R.id.layoutDanhMuc);
        LinearLayout currentRow = null;

        if (container.getChildCount() == 0 || ((LinearLayout) container.getChildAt(container.getChildCount() - 1)).getChildCount() >= 3) {
            currentRow = new LinearLayout(this);
            currentRow.setOrientation(LinearLayout.HORIZONTAL);
            currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            currentRow.setPadding(5, 5, 5, 5);
            container.addView(currentRow);
        } else {
            currentRow = (LinearLayout) container.getChildAt(container.getChildCount() - 1);
        }

        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        cardParams.setMargins(10, 5, 10, 5);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(0);
        cardView.setRadius(10f);
        cardView.setPadding(0, 0, 0, 0);
        cardView.setCardBackgroundColor(Color.WHITE);

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        frameLayout.setLayoutParams(frameParams);

        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics())
        );
        imageParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageParams);

        // Kiểm tra nếu có URL hình ảnh, dùng Glide để tải ảnh từ URL Firebase
        if (hinhUrl != null && !hinhUrl.isEmpty()) {
            Glide.with(this)
                    .load(hinhUrl)
                    .placeholder(R.drawable.danh_muc_mac_dinh)
                    .error(R.drawable.danh_muc_mac_dinh)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.danh_muc_mac_dinh);  // Hình mặc định
        }

        frameLayout.addView(imageView);
        cardView.addView(frameLayout);

        currentRow.addView(cardView);
    }



    private void themDanhMuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm danh mục mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText edtTenDanhMuc = new EditText(this);
        edtTenDanhMuc.setHint("Nhập tên danh mục");
        layout.addView(edtTenDanhMuc);

        Button btnChonHinh = new Button(this);
        btnChonHinh.setText("Chọn hình ảnh");
        layout.addView(btnChonHinh);

        builder.setView(layout);

        btnChonHinh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String tenDanhMuc = edtTenDanhMuc.getText().toString().trim();

            if (tenDanhMuc.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hinhUrl = selectedImageUri != null ? selectedImageUri.toString() : null;

            // Save category to Firebase
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Danh mục");
            String key = dbRef.push().getKey();
            if (key != null) {
                DanhMuc danhMuc = new DanhMuc(tenDanhMuc, hinhUrl);
                dbRef.child(key).setValue(danhMuc);
                Toast.makeText(TrangChuAdmin.this, "Danh mục đã được thêm", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Log.d("Image URI", "URI: " + selectedImageUri.toString());  // Log URI để kiểm tra
        }
    }
}
