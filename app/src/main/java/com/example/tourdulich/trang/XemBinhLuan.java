package com.example.tourdulich.trang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Adapter.DanhGiaAdapter;
import com.example.tourdulich.CSDL.BaiDanhGia;
import com.example.tourdulich.CSDL.LuuThongTinUser;
import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class XemBinhLuan extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnXoaDanhGia;
    private Button btnDangDanhGia;
    private ImageView imgHinh;
    private TextView txtTen;
    private ImageView sao1;
    private ImageView sao2;
    private ImageView sao3;
    private ImageView sao4;
    private ImageView sao5;
    private EditText noiDung;
    private ListView lvDanhGia;

    private int soSao = 0;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference bdgRef = FirebaseDatabase.getInstance().getReference("Bài đánh giá");
    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");

    private String idBaiDanhGiaCaNhan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_binh_luan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnQuayLai = findViewById(R.id.btQuayLaiTuDanhGia);
        btnXoaDanhGia = findViewById(R.id.btXoaDanhGia);
        btnDangDanhGia = findViewById(R.id.btDangDanhGia);
        imgHinh = findViewById(R.id.imageViewHinhUserDG);
        txtTen = findViewById(R.id.textViewTenUserDG);
        sao1 = findViewById(R.id.imageViewSao1);
        sao2 = findViewById(R.id.imageViewSao2);
        sao3 = findViewById(R.id.imageViewSao3);
        sao4 = findViewById(R.id.imageViewSao4);
        sao5 = findViewById(R.id.imageViewSao5);
        noiDung = findViewById(R.id.editTextNoiDungDanhGia);
        lvDanhGia = findViewById(R.id.listViewTatCaDanhGia);

        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");

        //Show thông tin cá nhân
        showThongTin();

        //Show danh sách đánh giá
        showDSDanhGia();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdgRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int tongSao = 0;
                            int luongSao = 0;
                            int soBinhLuan = 0;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                BaiDanhGia baiDanhGia = dataSnapshot.getValue(BaiDanhGia.class);
                                if (baiDanhGia.idTour.equals(tour.idTour) && baiDanhGia.soSao != 0) {
                                    tongSao += baiDanhGia.soSao;
                                    luongSao++;
                                    if (baiDanhGia.binhLuan != "") {
                                        soBinhLuan++;
                                    }
                                }
                            }
                            double tongSoSao = Double.parseDouble(String.valueOf(tongSao));
                            double soLuongSao = Double.parseDouble(String.valueOf(luongSao));
                            if(soLuongSao!=0) {
                                double tbSao = tongSoSao / soLuongSao;
                                tourRef.child(tour.idTour).child("soSao").setValue(tbSao);
                            }else
                                tourRef.child(tour.idTour).child("soSao").setValue(0);
                            tourRef.child(tour.idTour).child("soBinhLuan").setValue(soBinhLuan);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(XemBinhLuan.this, ThongTinDatVe.class);
                intent.putExtra("tour_item",tour);
                startActivity(intent);
            }
        });

        //Chọn số sao
        sao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_24);
                sao2.setImageResource(R.drawable.star_border_24);
                sao3.setImageResource(R.drawable.star_border_24);
                sao4.setImageResource(R.drawable.star_border_24);
                sao5.setImageResource(R.drawable.star_border_24);
                soSao = 1;
                btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnXoaDanhGia.setClickable(true);
                btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnDangDanhGia.setClickable(true);
            }
        });
        sao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_24);
                sao2.setImageResource(R.drawable.star_24);
                sao3.setImageResource(R.drawable.star_border_24);
                sao4.setImageResource(R.drawable.star_border_24);
                sao5.setImageResource(R.drawable.star_border_24);
                soSao = 2;
                btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnXoaDanhGia.setClickable(true);
                btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnDangDanhGia.setClickable(true);
            }
        });
        sao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_24);
                sao2.setImageResource(R.drawable.star_24);
                sao3.setImageResource(R.drawable.star_24);
                sao4.setImageResource(R.drawable.star_border_24);
                sao5.setImageResource(R.drawable.star_border_24);
                soSao = 3;
                btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnXoaDanhGia.setClickable(true);
                btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnDangDanhGia.setClickable(true);
            }
        });
        sao4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_24);
                sao2.setImageResource(R.drawable.star_24);
                sao3.setImageResource(R.drawable.star_24);
                sao4.setImageResource(R.drawable.star_24);
                sao5.setImageResource(R.drawable.star_border_24);
                soSao = 4;
                btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnXoaDanhGia.setClickable(true);
                btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnDangDanhGia.setClickable(true);
            }
        });
        sao5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_24);
                sao2.setImageResource(R.drawable.star_24);
                sao3.setImageResource(R.drawable.star_24);
                sao4.setImageResource(R.drawable.star_24);
                sao5.setImageResource(R.drawable.star_24);
                soSao = 5;
                btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnXoaDanhGia.setClickable(true);
                btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                btnDangDanhGia.setClickable(true);
            }
        });

        //Xóa bình luận cá nhân
        btnXoaDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sao1.setImageResource(R.drawable.star_border_24);
                sao2.setImageResource(R.drawable.star_border_24);
                sao3.setImageResource(R.drawable.star_border_24);
                sao4.setImageResource(R.drawable.star_border_24);
                sao5.setImageResource(R.drawable.star_border_24);
                noiDung.setText("");
                btnXoaDanhGia.setTextColor(Color.parseColor("#9AADBD"));
                btnXoaDanhGia.setClickable(false);
                btnDangDanhGia.setTextColor(Color.parseColor("#9AADBD"));
                btnDangDanhGia.setClickable(false);
                soSao = 0;
                bdgRef.child(idBaiDanhGiaCaNhan).child("soSao").setValue(0);
                bdgRef.child(idBaiDanhGiaCaNhan).child("binhLuan").setValue("");
                bdgRef.child(idBaiDanhGiaCaNhan).child("thoiGian").setValue("");
            }
        });
        btnXoaDanhGia.setClickable(false);

        btnDangDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdgRef.child(idBaiDanhGiaCaNhan).child("soSao").setValue(soSao);
                String binhLuan = String.valueOf(noiDung.getText());
                bdgRef.child(idBaiDanhGiaCaNhan).child("binhLuan").setValue(binhLuan);
                Calendar calendar = Calendar.getInstance();
                String dd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String MM = String.valueOf(calendar.get(Calendar.MONTH)+1);
                String yyyy = String.valueOf(calendar.get(Calendar.YEAR));
                String format = String.format("%s/%s/%s",dd,MM,yyyy);
                bdgRef.child(idBaiDanhGiaCaNhan).child("thoiGian").setValue(format);
                Toast.makeText(XemBinhLuan.this, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
            }
        });
        btnDangDanhGia.setClickable(false);

    }

    private void showThongTin() {
        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");
        //Hiển thị thông tin cá nhân
        String userID = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser user = snapshot.getValue(LuuThongTinUser.class);
                Uri uri = Uri.parse(user.hinhDaiDien);
                Glide.with(XemBinhLuan.this)
                        .load(uri)
                        .into(imgHinh);
                txtTen.setText(user.tenNguoiDung);

                //Hiển thị bài đánh giá của cá nhân
                bdgRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BaiDanhGia baiDanhGia = snapshot.getValue(BaiDanhGia.class);
                                if (baiDanhGia.idTour.equals(tour.idTour) && baiDanhGia.idUser.equals(user.id)) {
                                    idBaiDanhGiaCaNhan = baiDanhGia.idBaiDanhGia;
                                    if (baiDanhGia.soSao == 1) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_border_24);
                                        sao3.setImageResource(R.drawable.star_border_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 1;
                                        btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnXoaDanhGia.setClickable(true);
                                        btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnDangDanhGia.setClickable(true);
                                    } else if (baiDanhGia.soSao == 2) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_border_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 2;
                                        btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnXoaDanhGia.setClickable(true);
                                        btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnDangDanhGia.setClickable(true);
                                    } else if (baiDanhGia.soSao == 3) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 3;
                                        btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnXoaDanhGia.setClickable(true);
                                        btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnDangDanhGia.setClickable(true);
                                    } else if (baiDanhGia.soSao == 4) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 4;
                                        btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnXoaDanhGia.setClickable(true);
                                        btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnDangDanhGia.setClickable(true);
                                    } else if (baiDanhGia.soSao == 5) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_24);
                                        sao5.setImageResource(R.drawable.star_24);
                                        soSao = 5;
                                        btnXoaDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnXoaDanhGia.setClickable(true);
                                        btnDangDanhGia.setTextColor(Color.parseColor("#FFFFFFFF"));
                                        btnDangDanhGia.setClickable(true);
                                    }

                                    if (baiDanhGia.binhLuan != "") {
                                        noiDung.setText(baiDanhGia.binhLuan);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(XemBinhLuan.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(XemBinhLuan.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDSDanhGia() {
        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");
        String userID = firebaseUser.getUid();
        ArrayList<BaiDanhGia> baiDanhGias = new ArrayList<>();
        bdgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BaiDanhGia baiDanhGia = dataSnapshot.getValue(BaiDanhGia.class);
                        if (!baiDanhGia.idUser.equals(userID) && baiDanhGia.idTour.equals(tour.idTour)) {
                            baiDanhGias.add(baiDanhGia);
                        }
                    }
                    DanhGiaAdapter danhGiaAdapter = new DanhGiaAdapter(XemBinhLuan.this, baiDanhGias, baiDanhGias.size());
                    lvDanhGia.setAdapter(danhGiaAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(XemBinhLuan.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

}