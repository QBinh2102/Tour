package com.example.tourdulich.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

public class DanhMucAdapter extends BaseAdapter {

    private Context context;
    private List<DanhMuc> danhMucList;
    private LayoutInflater inflater;
    private DatabaseReference dmRef = FirebaseDatabase.getInstance().getReference("Danh mục");

    public DanhMucAdapter(Context context, List<DanhMuc> danhMucList) {
        this.context = context;
        this.danhMucList = danhMucList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return danhMucList.size();
    }

    @Override
    public Object getItem(int position) {
        return danhMucList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate layout chỉ khi convertView là null
            convertView = inflater.inflate(R.layout.danh_muc_list, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.imgVHinhDanhMuclist);
        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenDanhMuclist);

        DanhMuc danhMuc = danhMucList.get(position);
        Uri imageUri = Uri.parse(danhMuc.hinh);
        Glide.with(context)
                .load(imageUri)
                .into(img);
        txtTen.setText(danhMuc.ten);

        return convertView;
    }
}
