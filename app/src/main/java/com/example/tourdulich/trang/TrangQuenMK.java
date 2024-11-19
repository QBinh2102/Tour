package com.example.tourdulich.trang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class TrangQuenMK extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnTiepTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_quen_mk);

        //Quay lại TRANG ĐĂNG NHẬP
        Intent trangDangNhap = new Intent(this, MainActivity.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuQuenMatKhau);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start activity MainActivity
                startActivity(trangDangNhap);
            }
        });

        btnTiepTuc = findViewById(R.id.btTiepTucTuQuenMK);
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gửi xác thực mail để reset password;
                FirebaseAuth auth = FirebaseAuth.getInstance();
                EditText mail = findViewById(R.id.editTextEmailTimTK);
                String emailAddress = mail.getText().toString();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(TrangQuenMK.this,"Email sent.",Toast.LENGTH_SHORT).show();
                                }
//                                else{
//                                    Toast.makeText(TrangQuenMK.this,"Email chưa được đăng ký.",Toast.LENGTH_SHORT).show();
//                                }
                            }
                        });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}