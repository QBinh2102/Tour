package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.Adapter.TourAdapter;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatVe extends AppCompatActivity {

    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;

    private ListView lvTour;
    private ArrayList<Tour> arrayTour;
    private TourAdapter tourAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Tour");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private String selectedServiceType = ""; // Không lọc loại dịch vụ mặc định
    private String selectedCity = ""; // Không lọc thành phố mặc định
    private String selectedClub = ""; // Không lọc câu lạc bộ mặc định

    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);


        //Chuyển Trang Thông Tin Cá Nhân
        if (firebaseUser != null) {
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
        } else {
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

        //Chuyển sang thông tin đặt vé
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


    private void showTour() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Tour tour = snapshot.getValue(Tour.class);
                        if (tour != null) {
                            arrayTour.add(tour); // Thêm tour vào danh sách
                        }
                    }
                    tourAdapter = new TourAdapter(DatVe.this, arrayTour);
                    lvTour.setAdapter(tourAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(DatVe.this, "Không có tour nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DatVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    String GiaTien = "";
    String PhuongTien = "";

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bộ lọc");

        View view = getLayoutInflater().inflate(R.layout.activity_bo_loc_dat_ve, null);
        builder.setView(view);

        Spinner serviceTypeSpinner = view.findViewById(R.id.service_type_spinner);
        Spinner clubSpinner = view.findViewById(R.id.service_spinner);

//        ArrayAdapter<CharSequence> serviceTypeAdapter = ArrayAdapter.createFromResource(this,
//                R.array.service_types, android.R.layout.simple_spinner_item);
//        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        serviceTypeSpinner.setAdapter(serviceTypeAdapter);
//
//        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(this,
//                R.array.cars, android.R.layout.simple_spinner_item);
//        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        clubSpinner.setAdapter(clubAdapter);

        showTTBoLoc(serviceTypeSpinner,clubSpinner,view);
        serviceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    GiaTien = "500000";
                } else if (position == 2) {
                    GiaTien = "1000000";
                } else if (position == 3) {
                    GiaTien = "2000000";
                } else if (position == 4) {
                    GiaTien = "5000000";
                } else if (position == 0) {
                    GiaTien = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txtText = (TextView) view;
                PhuongTien = txtText.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button applyButton = view.findViewById(R.id.apply_button);

        AlertDialog dialog = builder.create();  // Tạo đối tượng dialog
//        applyButton.setOnClickListener(v -> {
//            // Lấy giá trị từ spinner
//            selectedServiceType = serviceTypeSpinner.getSelectedItem().toString();
//            selectedClub = clubSpinner.getSelectedItem().toString();
//
//            // Cập nhật danh sách tour
//            showTour();
//            dialog.dismiss(); // Đóng dialog
//        });

        //Chọn Ngày Bộ lọc
        TextView edtTextNgayDi = view.findViewById(R.id.editTextNgayDi);
        edtTextNgayDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(DatVe.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtTextNgayDi.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button ApDung = view.findViewById(R.id.apply_button);
        ApDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayTour = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Tour tour = snapshot.getValue(Tour.class);
                                if (GiaTien.equals("") && PhuongTien.equals("") && edtTextNgayDi.getText().toString().equals("")) {
                                    arrayTour.add(tour);
//                                } else if (!GiaTien.equals("") && !PhuongTien.equals("") && !edtTextNgayDi.getText().toString().equals("")) {
//                                    int giaTien = Integer.parseInt(GiaTien);
//                                    int giaTour = Integer.parseInt(tour.giaTien);
//
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                                    LocalDate ngayCanSoSanh = LocalDate.parse(edtTextNgayDi.getText().toString(), formatter);
//                                    LocalDate ngayKhoiHanh = LocalDate.parse(tour.ngayKhoiHanh,formatter);
//                                    if (Math.abs(ChronoUnit.DAYS.between(ngayKhoiHanh, ngayCanSoSanh)) <= 3) {
//                                        arrayTour.add(tour);
//                                    }
                                } else if (GiaTien!="") {
                                    int giaTien = Integer.parseInt(GiaTien);
                                    int giaTour = Integer.parseInt(tour.giaTien);
                                    if(0<giaTour&&giaTour<=giaTien){
                                        arrayTour.add(tour);
                                    }
                                } else if (PhuongTien!="") {
                                    if(tour.phuongTien.equals(PhuongTien))
                                        arrayTour.add(tour);
                                } else if ((GiaTien!="")&&(PhuongTien!="")) {
                                    int giaTien = Integer.parseInt(GiaTien);
                                    int giaTour = Integer.parseInt(tour.giaTien);
                                    if (0 < giaTour && giaTour <= giaTien && tour.phuongTien.equals(PhuongTien)) {
                                        arrayTour.add(tour);
                                    }
                                } else if (!edtTextNgayDi.getText().toString().equals("")) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate ngayCanSoSanh = LocalDate.parse("20/12/2024", formatter);
                                    LocalDate ngayKhoiHanh = LocalDate.parse(tour.ngayKhoiHanh,formatter);
                                    if (Math.abs(ChronoUnit.DAYS.between(ngayKhoiHanh, ngayCanSoSanh)) <= 3) {
                                        arrayTour.add(tour);
                                    }
                                }
                            }


                        } else {
                            Toast.makeText(DatVe.this, "Không có tour nào", Toast.LENGTH_SHORT).show();
                        }
                        tourAdapter = new TourAdapter(DatVe.this, arrayTour);
                        lvTour.setAdapter(tourAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DatVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //Hủy Áp Dụng
        Button HuyApDung = view.findViewById(R.id.cancel_button);
        HuyApDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Tour tour = snapshot.getValue(Tour.class);
                                if (tour != null) {
                                    arrayTour.add(tour); // Thêm tour vào danh sách
                                }
                            }
                            tourAdapter = new TourAdapter(DatVe.this, arrayTour);
                            lvTour.setAdapter(tourAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(DatVe.this, "Không có tour nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DatVe.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });

        //Quay Lại Bộ Lọc
        Button btnQuayLai = view.findViewById(R.id.btnReturnBL);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show(); // Hiển thị dialog
    }

    private void showTTBoLoc(Spinner serviceTypeSpinner, Spinner clubSpinner, View view) {
        ArrayAdapter<CharSequence> serviceTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.service_types, android.R.layout.simple_spinner_item);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceTypeSpinner.setAdapter(serviceTypeAdapter);

//        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
//                R.array.cities, android.R.layout.simple_spinner_item);
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        citySpinner.setAdapter(cityAdapter);

        ArrayAdapter<CharSequence> clubAdapter = ArrayAdapter.createFromResource(this,
                R.array.cars, android.R.layout.simple_spinner_item);
        clubAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubSpinner.setAdapter(clubAdapter);

        TextView edtTextNgayDi = view.findViewById(R.id.editTextNgayDi);
//        Calendar calendar = Calendar.getInstance();
//        String dd = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
//        String MM = String.valueOf(calendar.get(Calendar.MONTH)+1);
//        String yyyy = String.valueOf(calendar.get(Calendar.YEAR));
//        String format = String.format("%s/%s/%s",dd,MM,yyyy);
        edtTextNgayDi.setHint("dd/MM/yyyy");
    }

}
