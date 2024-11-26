package com.example.tourdulich.trang;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class XemBinhLuan extends AppCompatActivity {

    private Button btnQuayLai;
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
                Intent intent = new Intent(XemBinhLuan.this,ThongTinDatVe.class);
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
            }
        });


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
                        if (dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                BaiDanhGia baiDanhGia = snapshot.getValue(BaiDanhGia.class);
                                if(baiDanhGia.idTour.equals(tour.idTour)&&baiDanhGia.idUser.equals(user.id)){
                                    if(baiDanhGia.soSao==1){
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_border_24);
                                        sao3.setImageResource(R.drawable.star_border_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 1;
                                    } else if (baiDanhGia.soSao==2) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_border_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 2;
                                    } else if (baiDanhGia.soSao==3) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_border_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 3;
                                    } else if (baiDanhGia.soSao==4) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_24);
                                        sao5.setImageResource(R.drawable.star_border_24);
                                        soSao = 4;
                                    } else if (baiDanhGia.soSao==5) {
                                        sao1.setImageResource(R.drawable.star_24);
                                        sao2.setImageResource(R.drawable.star_24);
                                        sao3.setImageResource(R.drawable.star_24);
                                        sao4.setImageResource(R.drawable.star_24);
                                        sao5.setImageResource(R.drawable.star_24);
                                        soSao = 5;
                                    }

                                    if(baiDanhGia.binhLuan!=""){
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
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        BaiDanhGia baiDanhGia = dataSnapshot.getValue(BaiDanhGia.class);
                        if(!baiDanhGia.idUser.equals(userID)&&baiDanhGia.idTour.equals(tour.idTour)){
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