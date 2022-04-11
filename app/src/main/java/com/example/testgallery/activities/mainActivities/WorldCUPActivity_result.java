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
                AlertDialog.Builder builder = new AlertDialog.Builder(WorldCUPActivity_result.this);

                builder.setTitle("월드컵 결과 저장");
                builder.setMessage("삭제리스트에 위치한 사진은 삭제됩니다");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String[] paths = new String[Deletelist.size()];
                        ArrayList<String> list = new ArrayList<>();
                        int i = 0;

                        String folderName = "휴지통";      // 생성할 폴더 이름
                        String afterFilePath = "/storage/emulated/0/Pictures";     // 옮겨질 경로
                        String path = afterFilePath+"/"+folderName;     // 옮겨질 경로 + 생성할 폴더 이름 => 휴지통 경로
                        File dir = new File(path);

                        if (!dir.exists()) {        // 폴더 없으면 폴더 생성
                            dir.mkdirs();
                            for (String img :Deletelist){
                                File imgFile = new File(img);
                                File desImgFile = new File(path,"휴지통" + "_" + imgFile.getName());
                                list.add(desImgFile.getPath());
                                imgFile.renameTo(desImgFile);
                                imgFile.deleteOnExit();
                                paths[i] = desImgFile.getPath();
                                i++;
                            }
                            // 밑에 코드가 있어야 휴지통으로 이동한 복사본이 보임
                            MediaScannerConnection.scanFile(getApplicationContext(),paths, null, null);
                        } else {
                            for (String img :Deletelist){
                                File imgFile = new File(img);
                                File desImgFile = new File(path,"휴지통" + "_" + imgFile.getName());
                                list.add(desImgFile.getPath());
                                imgFile.renameTo(desImgFile);
                                imgFile.deleteOnExit();
                                paths[i] = desImgFile.getPath();
                                i++;
                            }
                            // 밑에 코드가 있어야 휴지통으로 이동한 복사본이 보임
                            MediaScannerConnection.scanFile(getApplicationContext(),paths, null, null);
                        }
                        finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                break;

            case R.id.btn_wc_delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(WorldCUPActivity_result.this);

                builder1.setTitle("확인");
                builder1.setMessage("월드컵을 취소하시겠습니까?");

                builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                finish();

                            }
                        });

                    builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert1 = builder1.create();
                    alert1.show();

                    break;
        }

    }
}
