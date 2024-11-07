package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatVe extends AppCompatActivity {
    private LinearLayout datesContainer;
    private LinearLayout classesContainer;

    private String selectedServiceType = ""; // Không lọc loại dịch vụ mặc định
    private String selectedCity = ""; // Không lọc thành phố mặc định
    private String selectedClub = ""; // Không lọc câu lạc bộ mặc định

    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTrangChu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_ve);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnToiHoSo = findViewById(R.id.btDatVeToiHoSo);
        btnToiHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ttcn);
            }
        });

        Intent trangChu = new Intent(this, TrangChu.class);
        btnToiTrangChu = findViewById(R.id.btDatVeToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
            }
        });

        TextView txtQuayLai = findViewById(R.id.back);
        txtQuayLai.setOnClickListener(v -> {
            Intent intent = new Intent(DatVe.this, TrangChu.class);
            startActivity(intent);
        });

        datesContainer = findViewById(R.id.dates_container);
        classesContainer = findViewById(R.id.classes_container);

        renderDates();
        renderClasses();

        TextView filterButton = findViewById(R.id.txtBoLoc);
        filterButton.setOnClickListener(v -> showFilterDialog());
    }

    private void renderDates() {
        datesContainer.removeAllViews();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", new Locale("vi", "VN"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", new Locale("vi", "VN"));

        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = new LinearLayout(this);
            dayLayout.setOrientation(LinearLayout.VERTICAL);
            dayLayout.setPadding(20, 10, 20, 10);

            TextView dayOfWeekView = new TextView(this);
            String dayOfWeek = dayFormat.format(calendar.getTime());
            dayOfWeekView.setText(dayOfWeek);
            dayOfWeekView.setTextSize(16);
            dayOfWeekView.setGravity(View.TEXT_ALIGNMENT_CENTER);

            TextView dayOfMonthView = new TextView(this);
            String dayOfMonth = dateFormat.format(calendar.getTime());
            dayOfMonthView.setText(dayOfMonth);
            dayOfMonthView.setTextSize(16);
            dayOfMonthView.setGravity(View.TEXT_ALIGNMENT_CENTER);

            if (i == 0) {
                dayOfWeekView.setBackgroundColor(getResources().getColor(R.color.my_color));
                dayOfWeekView.setTextColor(getResources().getColor(R.color.white));
                dayOfMonthView.setBackgroundColor(getResources().getColor(R.color.my_color));
                dayOfMonthView.setTextColor(getResources().getColor(R.color.white));
            } else {
                dayOfWeekView.setTextColor(getResources().getColor(R.color.black));
                dayOfMonthView.setTextColor(getResources().getColor(R.color.black));
            }

            dayLayout.addView(dayOfWeekView);
            dayLayout.addView(dayOfMonthView);
            datesContainer.addView(dayLayout);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void renderClasses() {
        classesContainer.removeAllViews();

        String[] classNames = {"Tour Đà Lạt", "Tour Vũng Tàu", "Tour Phú Quốc", "Tour Thái Lan"};
        String[] types = {"TourGG", "FakerTravel", "GenguBay", "AvanDuWorld"};
        String[] locations = {"Xe", "Máy Bay", "Thuyền","Xe"};
        String[] startTimes = {"09:45", "11:00", "17:00", "19:20"};
        String[] money = {"500.000 VND", "1.000.000 VND", "1.500.000 VND", "2.000.000 VND"};
        String[] cities = {"Đà Lạt", "Vũng Tàu", "Phú Quốc", "Thái Lan"};
        int[] imageIds = {R.drawable.da_lat, R.drawable.da_lat, R.drawable.da_lat, R.drawable.da_lat};

        for (int i = 0; i < classNames.length; i++) {
            if ((selectedServiceType.isEmpty() || money[i].contains(selectedServiceType)) &&
                    (selectedCity.isEmpty() || cities[i].equals(selectedCity)) &&
                    (selectedClub.isEmpty() || locations[i].equals(selectedClub))) {

                LinearLayout classLayout = new LinearLayout(this);
                classLayout.setOrientation(LinearLayout.VERTICAL);
                classLayout.setPadding(15, 15, 15, 15);
                classLayout.setBackground(getResources().getDrawable(R.drawable.day_background));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 20);
                classLayout.setLayoutParams(params);

                ImageView classImage = new ImageView(this);
                classImage.setImageResource(imageIds[i]);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200);
                classImage.setLayoutParams(imageParams);
                classLayout.addView(classImage);

                TextView classNameView = new TextView(this);
                classNameView.setText(classNames[i]);
                classNameView.setTextSize(18);
                classNameView.setPadding(0, 0, 0, 10);
                classLayout.addView(classNameView);

                TextView locationView = new TextView(this);
                locationView.setText("Nơi: " + locations[i]);
                locationView.setTextColor(getResources().getColor(R.color.my_color2));
                classLayout.addView(locationView);

                TextView startTimeView = new TextView(this);
                startTimeView.setText("Thời gian bắt đầu: " + startTimes[i]);
                startTimeView.setTextColor(getResources().getColor(R.color.my_color2));
                classLayout.addView(startTimeView);

                TextView durationView = new TextView(this);
                durationView.setText("Giá Tiền: " + money[i]);
                durationView.setTextColor(getResources().getColor(R.color.my_color2));
                classLayout.addView(durationView);

                TextView cityView = new TextView(this);
                cityView.setText("Địa điểm: " + cities[i]);
                cityView.setTextColor(getResources().getColor(R.color.my_color2));
                classLayout.addView(cityView);

                int finalI = i;
                classLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(DatVe.this, ThongTinDatVe.class);
                    intent.putExtra("className", classNames[finalI]);
                    intent.putExtra("classCode", "798435");
                    intent.putExtra("dateTime", startTimes[finalI] + " " + cities[finalI]);
                    intent.putExtra("duration", money[finalI]);
                    intent.putExtra("type", types[finalI]);
                    intent.putExtra("location", locations[finalI]);
                    intent.putExtra("maxSeats", 20);
                    startActivity(intent);
                });

                classesContainer.addView(classLayout);
            }
        }
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

            renderClasses(); // Làm mới các lớp dựa trên các bộ lọc mới
            dialog.dismiss(); // Đóng hộp thoại sau khi áp dụng
        });

        dialog.show(); // Hiển thị hộp thoại
    }

}

