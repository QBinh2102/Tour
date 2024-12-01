package com.example.tourdulich.Page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TrangChu extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

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

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tours.get(0));
                startActivity(intent);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this,ThongTinDatVe.class);
                intent.putExtra("tour_item",tours.get(1));
                startActivity(intent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this,ThongTinDatVe.class);
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

        //Chuyển Trang Thông Tin Cá Nhân
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

        // Kiểm tra lại role từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String role = preferences.getString("role", "user");  // Mặc định là "user" nếu chưa có role

        // Nếu role là admin, chuyển đến TrangChuAdmin
        if ("admin".equals(role)) {
            Intent intent = new Intent(TrangChu.this, TrangChuAdmin.class);
            startActivity(intent);
            finish();  // Đảm bảo không quay lại TrangChu
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

        //Hiển thị chào mừng người dùng trên trang chủ
        lbWelcome = findViewById(R.id.textViewWelcome);
        if (firebaseUser != null) {
            hoTen = firebaseUser.getDisplayName();
            lbWelcome.setText("Chào mừng " + hoTen);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                Glide.with(TrangChu.this)
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

                Glide.with(TrangChu.this)
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

                Glide.with(TrangChu.this)
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