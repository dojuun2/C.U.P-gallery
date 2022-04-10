package com.example.testgallery.activities.mainActivities;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;
import androidx.transition.Transition;

import com.example.testgallery.R;
import com.example.testgallery.activities.subActivities.ItemAlbumMultiSelectActivity;
import com.example.testgallery.adapters.ItemAlbumAdapter4;
import com.example.testgallery.models.Image;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;


public class WorldCUPActivity_result extends AppCompatActivity {
    private ItemAlbumAdapter4 adapter;
    private Intent intent;
    private ArrayList<String> Savelist;
    private ArrayList<String> Deletelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup_result);


        setTitle("dddd");
        intent = getIntent();
        Savelist = intent.getStringArrayListExtra("WC_savelist");
        Deletelist= intent.getStringArrayListExtra("WC_deletelist");
        event();
        init();
        getData();
        init2();
        getData2();
    }

    private void event() {
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


    public void OnClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_wc_save:

                break;

            case R.id.btn_wc_delete:


                break;
        }

    }
}
