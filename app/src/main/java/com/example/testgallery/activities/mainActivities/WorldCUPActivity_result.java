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
    private ArrayList<String> savelist;
    private ArrayList<String> deletelist;
    private ArrayList<Integer> deletenum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup_result);
        intent = getIntent();
        Imglist = intent.getStringArrayListExtra("WC_list");
        deletenum= intent.getIntegerArrayListExtra("WC_deletenum");
        deletelist = new ArrayList<>();
        savelist = new ArrayList<>();

        tasklist();

        init();
        getData();


        init2();
        getData2();


    }

    private void tasklist() {

        if(deletenum.size()<1){

            for (int i = 0; i < Imglist.size(); i++) {
                savelist.add(Imglist.get(i));
            }

        }



        for (int i = 0; i < Imglist.size(); i++) {

            for(int j = 0 ; j < deletenum.size() ; j++){

                if(deletenum.get(j) == i){

                    deletelist.add((Imglist.get(i)));
                    break;
                }
                else if (j+1 == deletenum.size()){

                    savelist.add((Imglist.get(i)));

                }
            }


        }
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
        adapter.addItem(savelist);
        adapter.notifyDataSetChanged();
    }
    private void getData2() {
        adapter.addItem(deletelist);
        adapter.notifyDataSetChanged();
    }

}