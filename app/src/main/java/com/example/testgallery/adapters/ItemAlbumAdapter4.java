package com.example.testgallery.adapters;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testgallery.R;
import com.example.testgallery.activities.mainActivities.TrashPictureActivity;
import com.example.testgallery.activities.mainActivities.WC_DragDropListener;
import com.example.testgallery.activities.mainActivities.WC_LongClickListener;
import com.example.testgallery.activities.mainActivities.WC_resultPictureListener;
import com.example.testgallery.activities.mainActivities.WC_result_PictureActivity;
import com.example.testgallery.activities.mainActivities.WorldCUPActivity_result;

import java.util.ArrayList;

public class ItemAlbumAdapter4 extends RecyclerView.Adapter<ItemAlbumAdapter4.ItemViewHolder>   {

    private ArrayList<String> listData;
    private WC_DragDropListener listener;



    public void ItemAlbumAdapter4(WC_DragDropListener listener){
        this.listener = listener;
    }


    public void addItem(ArrayList<String> imageList) {
        listData = new ArrayList<>();
        this.listData = imageList;

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {

            return listData.size();

    }





    class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        private Context context;

        ItemViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imgPhoto);
            context = itemView.getContext();

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(getAdapterPosition(),view);
                    return true;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(getAdapterPosition(),view);


                }
            });


        }

        void onBind(String img) {

            Glide.with(context).load(img).into(imageView);
        }


    }
}
