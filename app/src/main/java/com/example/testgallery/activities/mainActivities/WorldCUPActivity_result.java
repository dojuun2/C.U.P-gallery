package com.example.testgallery.activities.mainActivities;



import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;
import androidx.transition.Transition;

import com.example.testgallery.R;
import com.example.testgallery.adapters.ItemAlbumAdapter4;
import com.example.testgallery.models.Image;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class WorldCUPActivity_result extends AppCompatActivity {
    private ItemAlbumAdapter4 adapter;
    private Intent intent;
    private ArrayList<String> Imglist;
    private ArrayList<String> Savelist;
    private ArrayList<String> Deletelist;
    private ArrayList<Integer> deletenum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup_result);
        intent = getIntent();
        Savelist = intent.getStringArrayListExtra("WC_savelist");
        Deletelist= intent.getStringArrayListExtra("WC_deletelist");




        init();
        getData();


        init2();
        getData2();


    }




    private void init() {
        RecyclerView recyclerView = findViewById(R.id.WC_recycle);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ItemAlbumAdapter4();
        recyclerView.setAdapter(adapter);
    }

    private void init2() {
        RecyclerView recyclerView = findViewById(R.id.WC_recycle2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ItemAlbumAdapter4();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        adapter.addItem(Savelist);
        adapter.notifyDataSetChanged();
    }
    private void getData2() {
        adapter.addItem(Deletelist);
        adapter.notifyDataSetChanged();
    }


}
