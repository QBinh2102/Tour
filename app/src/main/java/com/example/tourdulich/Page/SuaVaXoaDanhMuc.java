package com.example.tourdulich.Page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tourdulich.R;
import java.util.HashMap;
import java.util.Map;

public class SuaVaXoaDanhMuc extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private String categoryToEdit = null;
    private GridLayout gridDanhMuc;  // GridLayout chứa các danh mục
    private Button btnDelete;
    private Map<String, CheckBox> checkBoxMap;
    private Map<String, ImageView> categoryImagesMap;
    private String selectedImageUri = null;
    private String currentImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_va_xoa_danh_muc);

        gridDanhMuc = findViewById(R.id.gridDanhMuc);
        btnDelete = findViewById(R.id.btnDelete);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Thiết lập số cột cho GridLayout
        gridDanhMuc.setColumnCount(3); // 3 cột trong grid

        checkBoxMap = new HashMap<>(); // Khởi tạo Map lưu CheckBox
        categoryImagesMap = new HashMap<>(); // Khởi tạo Map lưu ImageView

        // Lấy danh mục cần chỉnh sửa từ Intent
        categoryToEdit = getIntent().getStringExtra("category");

        if (categoryToEdit == null) {
            Toast.makeText(this, "Không tìm thấy danh mục!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadDanhMuc();
        btnDelete.setOnClickListener(v -> {
            boolean isDeleted = false; // Biến kiểm tra xem có danh mục nào được xóa hay không

            for (int i = 0; i < gridDanhMuc.getChildCount(); i++) {
                LinearLayout categoryLayout = (LinearLayout) gridDanhMuc.getChildAt(i);
                CheckBox checkBox = (CheckBox) categoryLayout.getChildAt(2); // Giả sử CheckBox là phần tử thứ 2 trong layout

                if (checkBox != null && checkBox.isChecked()) {
                    String category = (String) checkBox.getTag(); // Lấy tag của checkbox, là tên danh mục
                    // Xóa danh mục khỏi SharedPreferences
                    if (deleteDanhMuc(category)) {
                        // Xóa phần tử trong GridLayout
                        gridDanhMuc.removeViewAt(i);
                        isDeleted = true;
                        i--;  // Giảm chỉ số để tránh bỏ sót phần tử sau khi xóa
                    }
                }
            }

            if (isDeleted) {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được xóa!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Chưa chọn danh mục để xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> {
            // Tạo danh sách mới từ GridLayout
            StringBuilder danhMucMoi = new StringBuilder();
            SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            for (int i = 0; i < gridDanhMuc.getChildCount(); i++) {
                LinearLayout categoryLayout = (LinearLayout) gridDanhMuc.getChildAt(i);
                CheckBox checkBox = (CheckBox) categoryLayout.getChildAt(2); // CheckBox là phần tử thứ 3
                String category = (String) checkBox.getTag(); // Lấy tên danh mục từ tag
                danhMucMoi.append(category).append(","); // Thêm vào danh sách mới
            }

            // Ghi đè danh sách mới lên danh sách cũ trong SharedPreferences
            editor.putString("DanhMucList", danhMucMoi.toString());
            editor.apply();

            // Thông báo và chuyển về Trang Chủ Admin
            Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được cập nhật!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SuaVaXoaDanhMuc.this, TrangChuAdmin.class);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            loadDanhMuc();
            finish();
        });
    }


    private void loadDanhMuc() {
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        String danhMucList = sharedPreferences.getString("DanhMucList", "");

        if (!danhMucList.isEmpty()) {
            String[] categories = danhMucList.split(",");
            for (String category : categories) {
                addDanhMucToLayout(category);
            }
        }

    }

    private void addDanhMucToLayout(String category) {
        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);
        categoryLayout.setGravity(Gravity.CENTER);
        categoryLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Tạo TextView hiển thị tên danh mục
        TextView categoryName = new TextView(this);
        categoryName.setText(category);
        categoryName.setTextSize(18);

        // Tạo Checkbox để chọn danh mục
        CheckBox checkBox = new CheckBox(this);
        checkBox.setTag(category); // Đặt tag là tên danh mục
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Lưu CheckBox vào Map
        checkBoxMap.put(category, checkBox);

        // Tạo ImageView để hiển thị ảnh
        ImageView categoryImage = new ImageView(this);
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        String imageUri = sharedPreferences.getString(category + "_image", null);

        if (imageUri != null) {
            categoryImage.setImageURI(android.net.Uri.parse(imageUri));
        } else {
            categoryImage.setImageResource(R.drawable.danh_muc_mac_dinh); // Ảnh mặc định
        }

        categoryImage.setLayoutParams(new LinearLayout.LayoutParams(150, 150));

        // Lưu ImageView vào Map
        categoryImagesMap.put(category, categoryImage);

        categoryLayout.addView(categoryName);
        categoryLayout.addView(categoryImage);
        categoryLayout.addView(checkBox);

        // Thêm vào GridLayout
        gridDanhMuc.addView(categoryLayout);
    }

    private boolean deleteDanhMuc(String categoryToDelete) {
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        String danhMucList = sharedPreferences.getString("DanhMucList", "");
        if (!danhMucList.isEmpty()) {
            String[] categories = danhMucList.split(",");
            StringBuilder newDanhMucList = new StringBuilder();
            boolean isDeleted = false;
            for (String category : categories) {
                if (!category.equals(categoryToDelete)) {
                    newDanhMucList.append(category).append(",");
                } else {
                    isDeleted = true;
                }
            }
            if (isDeleted) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DanhMucList", newDanhMucList.toString());
                editor.remove(categoryToDelete + "_image");
                editor.apply();
            }
            return isDeleted;
        }
        return false;
    }

    private void saveNewImage(String category, String imageUri) {
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(category + "_image", imageUri);
        editor.apply();
    }
}
