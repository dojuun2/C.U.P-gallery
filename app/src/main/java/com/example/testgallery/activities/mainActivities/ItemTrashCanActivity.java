package com.example.testgallery.activities.mainActivities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testgallery.R;
import com.example.testgallery.activities.subActivities.ItemAlbumMultiSelectActivity;
import com.example.testgallery.activities.subActivities.ItemSecretMultiSelectActivity;
import com.example.testgallery.activities.subActivities.ItemTrashCanMultiSelectActivity;
import com.example.testgallery.adapters.ItemAlbumAdapter;
import com.example.testgallery.adapters.ItemTrashCanAdapter;
import com.example.testgallery.adapters.ItemTrashCanAdapter2;
import com.example.testgallery.adapters.ItemTrashCanAdapter3;
import com.example.testgallery.models.Image;


import java.io.File;
import java.util.ArrayList;

public class ItemTrashCanActivity extends AppCompatActivity {
    private ArrayList<String> myAlbum;
    private String path_folder ;
    private RecyclerView ryc_album;
    private RecyclerView ryc_list_album;
    private Intent intent;
    private String album_name;
    Toolbar toolbar_item_album;
    private ItemTrashCanAdapter itemTrashCanAdapter;
    private ItemTrashCanAdapter2 itemTrashCanAdapter2;
    private ItemTrashCanAdapter3 itemTrashCanAdapter3;

    private int spanCount;
    private int isSecret;
    private int duplicateImg;
    private int isAlbum;
    private static final int REQUEST_CODE_PIC = 10;
    private static final int REQUEST_CODE_CHOOSE = 55;
    private static final int REQUEST_CODE_ADD = 56;
    private static final int REQUEST_CODE_SECRET = 57;
    private ArrayList<Image> listImageSelected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_album);
        intent = getIntent();
        setUpSpanCount();
        mappingControls();
        setData();
        setRyc();
        events();
    }

    private void setUpSpanCount() {
        SharedPreferences sharedPref = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        spanCount = sharedPref.getInt("span_count", 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD) {
            ArrayList<String> resultList = data.getStringArrayListExtra("list_result");
            if(resultList !=null) {
                myAlbum.addAll(resultList);
                spanAction();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            if(data != null) {
                int isMoved = data.getIntExtra("move", 0);
                if (isMoved == 1) {
                    ArrayList<String> resultList = data.getStringArrayListExtra("list_result");
                    if (resultList != null) {
                        myAlbum.remove(resultList);
                        spanAction();
                    }
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SECRET) {
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PIC) {
            String path_img = data.getStringExtra("path_img");
            if(isSecret == 1) {
                myAlbum.remove(path_img);
                spanAction();
            }else if (duplicateImg == 2){
                myAlbum.remove(path_img);
                spanAction();
            }
        }
    }

    private void spanAction() {
        if(spanCount == 1) {
            ryc_list_album.setAdapter(new ItemTrashCanAdapter3(myAlbum));
        }
        else if(spanCount == 2) {
            ryc_list_album.setAdapter(new ItemTrashCanAdapter2(myAlbum));
        }
        else{

            ryc_list_album.setAdapter(new ItemTrashCanAdapter(myAlbum, album_name, path_folder));
        }
    }

    private void setRyc() {
        album_name = intent.getStringExtra("name");
        ryc_list_album.setLayoutManager(new GridLayoutManager(this, spanCount));

        ItemAlbumAdapter itemAlbumAdapter = new ItemAlbumAdapter(myAlbum, album_name, path_folder);

        itemTrashCanAdapter = new ItemTrashCanAdapter(myAlbum, album_name, path_folder);

        if(spanCount == 1)
            ryc_list_album.setAdapter(new ItemTrashCanAdapter3(myAlbum));
        else if(spanCount == 2)
            ryc_list_album.setAdapter(new ItemTrashCanAdapter2(myAlbum));
        else

            ryc_list_album.setAdapter(new ItemTrashCanAdapter(myAlbum, album_name, path_folder));
    }

    private void animationRyc() {
        switch(spanCount) {
            case 1:
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_layout_ryc_1);
                ryc_list_album.setAnimation(animation1);
            case 2:
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout_ryc_1);
                ryc_list_album.setAnimation(animation2);
                break;
            case 3:
                Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout_ryc_2);
                ryc_list_album.setAnimation(animation3);
                break;
            case 4:
                Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout_ryc_3);
                ryc_list_album.setAnimation(animation4);
                break;
        }
    }


    private void events() {
        // Toolbar events
        toolbar_item_album.inflateMenu(R.menu.menu_top_item_trash_can);
        toolbar_item_album.setTitle(album_name);

        // Show back button
        toolbar_item_album.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar_item_album.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Toolbar options
        toolbar_item_album.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.change_span_count:
                        spanCountEvent();
                        break;
                    case R.id.menuChoose:
                        if(isSecret == 0) {
                            Intent intent_mul = new Intent(ItemTrashCanActivity.this, ItemTrashCanMultiSelectActivity.class);
                            intent_mul.putStringArrayListExtra("data_1", myAlbum);
                            intent_mul.putExtra("name_1", album_name);
                            intent_mul.putExtra("path_folder", path_folder);
                            startActivityForResult(intent_mul, REQUEST_CODE_CHOOSE);
                        }else {
                            Intent intent_mul = new Intent(ItemTrashCanActivity.this, ItemSecretMultiSelectActivity.class);
                            intent_mul.putStringArrayListExtra("data_1", myAlbum);
                            intent_mul.putExtra("name_1", album_name);
                            startActivityForResult(intent_mul, REQUEST_CODE_SECRET);
                        }
                        break;
                    case R.id.album_delete_all:
                        deleteAllEvents();
                        break;
                    case R.id.menu_restore_all:
                        restoreAllEvents();
                        break;
                }
                return true;
            }
        });
        if(isSecret == 1)
            hideMenu();
    }

    private void hideMenu() {
        toolbar_item_album.getMenu().findItem(R.id.menu_add_image).setVisible(false);
    }

    private void spanCountEvent() {
        if(spanCount == 1){
            spanCount++;
            ryc_list_album.setLayoutManager(new GridLayoutManager(this, spanCount));
            ryc_list_album.setAdapter(itemTrashCanAdapter2);
        }

        else if(spanCount < 4 && spanCount > 1) {
            spanCount++;
            ryc_list_album.setLayoutManager(new GridLayoutManager(this, spanCount));
            ryc_list_album.setAdapter(itemTrashCanAdapter);
        }
        else if(spanCount == 4) {

            spanCount = 1;
            ryc_list_album.setLayoutManager(new LinearLayoutManager(this));
            ryc_list_album.setAdapter(itemTrashCanAdapter3);

        }


        animationRyc();
        SharedPreferences sharedPref = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("span_count", spanCount);
        editor.commit();
    }

    private void deleteAllEvents() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemTrashCanActivity.this);

        builder.setTitle("확인");
        builder.setMessage("모든 사진을 삭제하시겠습니까?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                File path = new File("/storage/emulated/0/Pictures/휴지통");
                File[] fileList = path.listFiles();

                for(int i=0; i<fileList .length; i++){
                    fileList[i].delete();
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
    }

    private void restoreAllEvents(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemTrashCanActivity.this);

        builder.setTitle("확인");
        builder.setMessage("모든 사진을 복구하시겠습니까?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                File path = new File("/storage/emulated/0/Pictures/휴지통");       // 복원 전 폴더

                File[] filelist = path.listFiles();     // listFiles() : 디렉토리 경로에 있는 파일들을 배열로 리턴
                // 복원 전 폴더에 있는 파일들을 filelist에 배열로 저장

                String[] paths = path.list();           // list() : 디렉토리 경로에 있는 파일들의 이름들을 배열로 리턴
                // 복원 전 폴더에 있는 파일들의 이름들을 filelist에 배열로 저장

                for(int i = 0; i<filelist.length; i++){
                    File file = new File(filelist[i].getPath());

                    int start = file.getName().indexOf("_"); // 첫 _ 위치 추출
                    int end = file.getName().indexOf("_", 4); //  추출

                    String fileRestore_folder = file.getName().substring(start+1, end);

                    File move_path = new File("/storage/emulated/0/Pictures/" + fileRestore_folder);      // 복원 후 폴더
                    File move_path2 = new File("/storage/emulated/0/" + fileRestore_folder);      // Pictures 였을 경우 복원 후 폴더

                    switch(fileRestore_folder){
                        case "Pictures":
                            File MoveFile1 = new File(move_path2,fileRestore_folder + "_" + file.getName());     // 이동할 경로와 이동 후 파일명
                            filelist[i].renameTo(MoveFile1);     // filelist에 있는 파일들 이름 변경
                            filelist[i].deleteOnExit();         // 원래 파일 삭제
                            paths[i] = MoveFile1.getPath();      // 복원한 파일의 경로를 문자열로 저장
                            break;

                        case "Screenshot":
                            File MoveFile2 = new File(move_path,fileRestore_folder + "_" + file.getName());     // 이동할 경로와 이동 후 파일명
                            filelist[i].renameTo(MoveFile2);     // filelist에 있는 파일들 이름 변경
                            filelist[i].deleteOnExit();         // 원래 파일 삭제
                            paths[i] = MoveFile2.getPath();      // 복원한 파일의 경로를 문자열로 저장
                            break;

                        default:
                            File MoveFile = new File(move_path,fileRestore_folder + "_" + file.getName());     // 이동할 경로와 이동 후 파일명
                            filelist[i].renameTo(MoveFile);     // filelist에 있는 파일들 이름 변경
                            filelist[i].deleteOnExit();         // 원래 파일 삭제
                            paths[i] = MoveFile.getPath();      // 복원한 파일의 경로를 문자열로 저장
                            break;
                    }

                }
                MediaScannerConnection.scanFile(getApplicationContext(),paths, null, null);

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
    }

    private void setData() {
        myAlbum = intent.getStringArrayListExtra("data");
        path_folder = intent.getStringExtra("path_folder");
        isSecret = intent.getIntExtra("isSecret", 0);
        duplicateImg = intent.getIntExtra("duplicateImg",0);
        itemTrashCanAdapter2 = new ItemTrashCanAdapter2(myAlbum);
        isAlbum = intent.getIntExtra("ok",0);
        itemTrashCanAdapter3 = new ItemTrashCanAdapter3(myAlbum);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    private void mappingControls() {
        ryc_list_album = findViewById(R.id.ryc_list_album);
        toolbar_item_album = findViewById(R.id.toolbar_item_album);
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i=0;i<myAlbum.size();i++) {
                File file = new File(myAlbum.get(i));
                if(!file.exists()) {
                    myAlbum.remove(i);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            spanAction();
        }
    }

}
