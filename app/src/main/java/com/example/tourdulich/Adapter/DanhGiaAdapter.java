package com.example.tourdulich.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tourdulich.CSDL.BaiDanhGia;
import com.example.tourdulich.CSDL.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhGiaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BaiDanhGia> baiDanhGias;
    private LayoutInflater inflater;
    private int count;
    private LuuThongTinUser user;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Người đăng ký");

    public DanhGiaAdapter(Context context, ArrayList<BaiDanhGia> baiDanhGias, int count) {
        this.context = context;
        this.baiDanhGias = baiDanhGias;
        this.inflater = LayoutInflater.from(context);
        this.count = count;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.bai_danh_gia_items,null);

        ImageView imgHDD = convertView.findViewById(R.id.imageViewHinhUserBDG);
        TextView txtTen = convertView.findViewById(R.id.textViewTenUserBDG);
        TextView txtSoSao = convertView.findViewById(R.id.textViewSoSaoBDG);
        TextView txtThoiGian = convertView.findViewById(R.id.textViewThoiGianBDG);
        TextView txtNoiDung = convertView.findViewById(R.id.textViewNoiDungBDG);

        BaiDanhGia baiDanhGia = baiDanhGias.get(position);
        String idUser = baiDanhGia.idUser;
//        //firebaseUser.getDisplayName();
        userRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(LuuThongTinUser.class);
                Uri uri = Uri.parse(user.hinhDaiDien);
                Glide.with(context)
                        .load(uri).into(imgHDD);
                txtTen.setText(user.tenNguoiDung);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
        txtSoSao.setText(String.format(baiDanhGia.soSao+" sao"));
        txtNoiDung.setText(baiDanhGia.binhLuan);
        txtThoiGian.setText(baiDanhGia.thoiGian);

        return convertView;
    }
}
