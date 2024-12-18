package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourdulich.Api.CreateOrder;
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

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class XacNhanThanhToan extends AppCompatActivity {

    private TextView txtTongTien;
    private Button btnThanhToan;
    private Button btnQuayLai;

    boolean flag = false;

    private DatabaseReference bdgRef = FirebaseDatabase.getInstance().getReference("Bài đánh giá");
    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
    private FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xac_nhan_thanh_toan);

        txtTongTien = findViewById(R.id.tvTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnQuayLai = findViewById(R.id.btnQuayLaiZalo);

        Intent intent = getIntent();
        int total = intent.getIntExtra("total", 0);
        Tour tour = (Tour) intent.getSerializableExtra("pay_tour");
        int sl = intent.getIntExtra("sl_ve",0);

        // Gộp logic định dạng tiền trực tiếp vào đây
        String totalFormatted = NumberFormat.getInstance(Locale.getDefault())
                .format(total)
                .replace(',', '.');
        txtTongTien.setText(totalFormatted + " VND");  // Hiển thị tổng tiền đã định dạng

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);

        // Nút quay lại ChiTietDatVe
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent(XacNhanThanhToan.this, ChiTietDatVe.class);
                startActivity(intentBack);
                finish();
            }
        });


        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(String.valueOf(total));
                    String code = data.getString("returncode");

                    if (code.equals("1")) {
                        String token = data.getString("zptranstoken");
                        ZaloPaySDK.getInstance().payOrder(XacNhanThanhToan.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                bdgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            BaiDanhGia tmp = dataSnapshot.getValue(BaiDanhGia.class);
                                            if (tmp.idTour.equals(tour.idTour) && tmp.idUser.equals(User.getUid())) {
                                                if (tmp.trangThai.equals("Đã thanh toán")) {
                                                    flag = true;
                                                    int soVeDat = sl;
                                                    int giaVe = Integer.parseInt(tour.giaTien);
                                                    int tongTien = giaVe * soVeDat;
                                                    tourRef.child(tour.idTour).child("soLuongVe").setValue(tour.soLuongVe - soVeDat);
                                                    bdgRef.child(tmp.idBaiDanhGia).child("soVe").setValue(tmp.soVe + soVeDat);
                                                    bdgRef.child(tmp.idBaiDanhGia).child("tongTien").setValue(tmp.tongTien + tongTien);
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
                                                                        soBinhLuan++;
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
                                                    Intent intent1 = new Intent(XacNhanThanhToan.this, ThongBaoThanhToan.class);
                                                    intent1.putExtra("result", "Thanh toán thành công");
                                                    startActivity(intent1);

                                                    break;
                                                }
                                            }
                                        }
                                        if (!flag) {
                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                            String idUser = User.getUid();
                                            String idTour = tour.idTour;
                                            String idBDG = auth.getUid() + System.currentTimeMillis();
                                            int soVeDat = sl;
                                            int giaVe = Integer.parseInt(tour.giaTien);
                                            int tongTien = giaVe * soVeDat;
                                            BaiDanhGia baiDanhGia = new BaiDanhGia(idBDG, idUser, idTour, soVeDat, tongTien);
                                            bdgRef.child(idBDG).setValue(baiDanhGia).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    tourRef.child(idTour).child("soLuongVe").setValue(tour.soLuongVe - soVeDat);
                                                    tourRef.child(idTour).child("soLuongDat").setValue(tour.soLuongDat+1);
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
                                                                        soBinhLuan++;
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
                                                    Intent intent1 = new Intent(XacNhanThanhToan.this, ThongBaoThanhToan.class);
                                                    intent1.putExtra("result", "Thanh toán thành công");
                                                    startActivity(intent1);
                                                }
                                            });

                                        }

                                        //
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(XacNhanThanhToan.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                    }
                                    //
                                });
//                                Intent intent1 = new Intent(XacNhanThanhToan.this, ThongBaoThanhToan.class);
//                                intent1.putExtra("result", "Thanh toán thành công");
//                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent1 = new Intent(XacNhanThanhToan.this, ThongBaoThanhToan.class);
                                intent1.putExtra("result", "Hủy thanh toán");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent1 = new Intent(XacNhanThanhToan.this, ThongBaoThanhToan.class);
                                intent1.putExtra("result", "Lỗi Thanh toán");
                                startActivity(intent1);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);

    }
}



