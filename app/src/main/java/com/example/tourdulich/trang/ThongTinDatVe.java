package com.example.tourdulich.trang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.R;


public class ThongTinDatVe extends AppCompatActivity {

    private TextView tenTour;
    private TextView giaTour;
    private TextView soSao;
    private TextView soBinhLuan;
    private TextView gioiThieu;
    private TextView thoiGian;
    private TextView phuongTien;
    private TextView soVe;

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

        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour_item");

        tenTour = findViewById(R.id.textViewTTTenTour);
        giaTour = findViewById(R.id.textViewTTGiaTour);
        soSao = findViewById(R.id.textViewTTSoSao);
        soBinhLuan = findViewById(R.id.textViewTTSoBinhLuan);
        gioiThieu = findViewById(R.id.textViewTTGioiThieu);
        thoiGian = findViewById(R.id.textViewTTThoiGian);
        phuongTien = findViewById(R.id.textViewTTPhuongTien);
        soVe = findViewById(R.id.textViewTTSoVe);

        //Show thông tin tour
        showThongTinTour(tour);


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

    }

    private void showThongTinTour(Tour tour) {
        tenTour.setText(tour.tenTour);
        giaTour.setText(String.format("Giá: %s",tour.giaTien));
        soSao.setText(String.format("%.2f sao",tour.soSao));
        soBinhLuan.setText(String.format("%d bình luận", tour.soBinhLuan));
        gioiThieu.setText(tour.gioiThieu);
        thoiGian.setText(String.format("Thời gian: %s-%s", tour.ngayKhoiHanh,tour.ngayKetThuc));
        phuongTien.setText(String.format("Phương tiện: %s", tour.phuongTien));
        soVe.setText(String.format("Số vé còn lại: %d",tour.soLuongVe));
    }
}
