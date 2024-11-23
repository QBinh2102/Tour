package com.example.tourdulich.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourdulich.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<Uri> imageUris;

    public ImageAdapter(ArrayList<Uri> imageUris) {
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hinh_tour_items, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);

        // Sử dụng Glide để tải ảnh từ Firebase Storage và hiển thị vào ImageView
        Glide.with(holder.itemView.getContext())
                .load(imageUri)     // Hình ảnh lỗi nếu không thể tải
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUris.size(); // Số lượng ảnh trong danh sách
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // Lấy ImageView trong item layout
        }
    }
}
