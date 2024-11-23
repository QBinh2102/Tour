package com.example.tourdulich.trang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.CSDL.Tour;
import com.example.tourdulich.Adapter.TourAdapter;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatVe extends AppCompatActivity {

    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;

    private ListView lvTour;
    private ArrayList<Tour> arrayTour;
    private TourAdapter tourAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Tour");

    private String selectedServiceType = ""; // Không lọc loại dịch vụ mặc định
    private String selectedCity = ""; // Không lọc thành phố mặc định
    private String selectedClub = ""; // Không lọc câu lạc bộ mặc định

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        arrayTour = new ArrayList<>();
        lvTour = findViewById(R.id.listViewTour);

        //Chuyển Trang Thông Tin Cá Nhân
        if(firebaseUser != null) {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu đã đăng nhập thành công
            Intent ttcn = new Intent(this, ThongTinCaNhan.class);
            btnToiHoSo = findViewById(R.id.btDatVeToiHoSo);
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
            btnToiHoSo = findViewById(R.id.btDatVeToiHoSo);
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
        btnToiTrangChu = findViewById(R.id.btDatVeToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btDatVeToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent giaoDich = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btDatVeToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(giaoDich);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Show toàn bộ tour trên firebase
        showTour();

        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tour tour = arrayTour.get(position);

                Intent intent = new Intent(DatVe.this, ThongTinDatVe.class);
                intent.putExtra("tour_item", tour);
                startActivity(intent);
            }
        });

        TextView filterButton = findViewById(R.id.txtBoLoc);
        filterButton.setOnClickListener(v -> showFilterDialog());
    }

    private void showTour(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Tour tour = snapshot.getValue(Tour.class);
                        if (tour != null) {
                            arrayTour.add(tour); // Thêm tour vào danh sách
                        }
                    }
                    tourAdapter = new TourAdapter(DatVe.this,arrayTour);
                    lvTour.setAdapter(tourAdapter);
//                    if(tourAdapter==null) {
//                        tourAdapter = new TourAdapter(DatVe.this, arrayTour);
//                        lvTour.setAdapter(tourAdapter);
//                    }else{
//                        tourAdapter.notifyDataSetChanged();
//                    }
                }else{
                    Toast.makeText(DatVe.this, "Không có tour nào", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DatVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bộ lọc");

        View view = getLayoutInflater().inflate(R.layout.activity_bo_loc_dat_ve, null);
        builder.setView(view);

        Spinner serviceTypeSpinner = view.findViewById(R.id.service_type_spinner);
        Spinner citySpinner = view.findViewById(R.id.city_spinner);
        Spinner clubSpinner = view.findViewById(R.id.service_spinner);

        ArrayAdapter<CharSequence> serviceTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.service_types, android.R.layout.simple_spinner_item);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceTypeSpinner.setAdapter(serviceTypeAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(this,
                R.array.cars, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner.setAdapter(clubAdapter);

        Button applyButton = view.findViewById(R.id.apply_button);

        AlertDialog dialog = builder.create();  // Tạo đối tượng dialog
        applyButton.setOnClickListener(v -> {
            // Đặt biến bộ lọc từ các giá trị đã chọn trong hộp thoại
            selectedServiceType = serviceTypeSpinner.getSelectedItem().toString();
            selectedCity = citySpinner.getSelectedItem().toString();
            selectedClub = clubSpinner.getSelectedItem().toString();

            dialog.dismiss(); // Đóng hộp thoại sau khi áp dụng
        });

        dialog.show(); // Hiển thị hộp thoại
    }

}
