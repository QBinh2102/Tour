package com.example.tourdulich.trang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tourdulich.R;

public class SuaVaXoaDanhMuc extends AppCompatActivity {
    private LinearLayout layoutDeSuaVaXoa;
    private String categoryToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_va_xoa_danh_muc);

        layoutDeSuaVaXoa = findViewById(R.id.layoutDeSuaVaXoa);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Nhận dữ liệu từ Intent (danh mục cần sửa)
        categoryToEdit = getIntent().getStringExtra("category");

        // Nếu có danh mục cần sửa, hiển thị vào EditText
        if (categoryToEdit != null) {
            EditText editText = new EditText(this);
            editText.setText(categoryToEdit);  // Hiển thị danh mục cần sửa
            editText.setHint("Nhập tên danh mục mới");

            // Thêm EditText vào layoutDeSuaVaXoa
            layoutDeSuaVaXoa.addView(editText);

            // Tạo nút Lưu
            Button btnSave = new Button(this);
            btnSave.setText("Lưu");
            btnSave.setOnClickListener(v -> {
                String newCategory = editText.getText().toString().trim();
                if (!newCategory.isEmpty()) {
                    // Cập nhật danh mục mới vào SharedPreferences
                    updateDanhMuc(categoryToEdit, newCategory);
                    Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được sửa!", Toast.LENGTH_SHORT).show();
                    finish();  // Quay lại trang chính sau khi sửa
                } else {
                    Toast.makeText(SuaVaXoaDanhMuc.this, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                }
            });

            // Tạo nút Hủy
            Button btnCancelEdit = new Button(this);
            btnCancelEdit.setText("Hủy");
            btnCancelEdit.setOnClickListener(v -> {
                // Quay lại mà không thay đổi gì
                finish();  // Quay lại trang chính mà không thay đổi gì
            });

            // Thêm nút Lưu và Hủy vào layoutDeSuaVaXoa
            layoutDeSuaVaXoa.addView(btnSave);
            layoutDeSuaVaXoa.addView(btnCancelEdit);
        }

        // Xử lý nút Hủy để quay lại trang trước mà không thay đổi gì
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateDanhMuc(String oldCategory, String newCategory) {
        // Cập nhật danh mục mới vào SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        String danhMucList = sharedPreferences.getString("DanhMucList", "");

        if (!danhMucList.isEmpty()) {
            String[] categories = danhMucList.split(",");
            StringBuilder newDanhMucList = new StringBuilder();

            // Duyệt qua tất cả danh mục và thay thế tên cũ bằng tên mới
            for (String category : categories) {
                if (category.equals(oldCategory)) {
                    newDanhMucList.append(newCategory).append(",");
                } else {
                    newDanhMucList.append(category).append(",");
                }
            }

            // Cập nhật lại danh mục vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DanhMucList", newDanhMucList.toString());
            editor.apply();
        }
    }
}
