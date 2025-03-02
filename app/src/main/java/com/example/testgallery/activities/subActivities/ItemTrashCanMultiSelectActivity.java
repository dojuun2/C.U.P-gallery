package com.example.testgallery.activities.subActivities;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testgallery.R;
import com.example.testgallery.activities.mainActivities.ItemTrashCanActivity;
import com.example.testgallery.activities.mainActivities.PictureActivity;
import com.example.testgallery.activities.mainActivities.SlideShowActivity;
import com.example.testgallery.adapters.AlbumSheetAdapter;
import com.example.testgallery.adapters.ImageSelectAdapter;
import com.example.testgallery.models.Album;
import com.example.testgallery.models.Image;
import com.example.testgallery.utility.FileUtility;
import com.example.testgallery.utility.GetAllPhotoFromGallery;
import com.example.testgallery.utility.ListTransInterface;
import com.example.testgallery.utility.SubInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemTrashCanMultiSelectActivity extends AppCompatActivity implements ListTransInterface {
    private ArrayList<String> myAlbum;
    private RecyclerView ryc_album;
    private RecyclerView ryc_list_album;
    private Intent intent;
    private String album_name;
    Toolbar toolbar_item_album;
    private ArrayList<Image> listImageSelected;
    private static int REQUEST_CODE_SLIDESHOW = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_album);
        intent = getIntent();
        setUpData();
        mappingControls();
        setData();
        setRyc();
        events();
    }

    private void setUpData() {
        listImageSelected = new ArrayList<>();
    }
    private void setRyc() {
        album_name = intent.getStringExtra("name_1");
        ryc_list_album.setLayoutManager(new GridLayoutManager(this, 3));
        ImageSelectAdapter imageSelectAdapter = new ImageSelectAdapter(ItemTrashCanMultiSelectActivity.this);
        List<Image> listImg = new ArrayList<>();
        for(int i =0 ; i< myAlbum.size();i++) {
            Image img = new Image();
            img.setThumb(myAlbum.get(i));
            img.setPath(myAlbum.get(i));
            listImg.add(img);
        }
        imageSelectAdapter.setData(listImg);
        imageSelectAdapter.setListTransInterface(this);
        ryc_list_album.setAdapter(imageSelectAdapter);
    }



    private void events() {
        // Toolbar events
        toolbar_item_album.inflateMenu(R.menu.menu_top_multi_trash_can);
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
                    case R.id.menuMultiDelete:
                        deleteEvents();
                        break;
                    case R.id.menuMultiRestore:
                        restoreEvent();
                        break;
                }

                return true;
            }
        });
    }

    private void restoreEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemTrashCanMultiSelectActivity.this);

        builder.setTitle("확인");
        builder.setMessage("사진을 복구하시겠습니까?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                RestoreAsync restoreAsync = new RestoreAsync();
                restoreAsync.execute();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteEvents() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemTrashCanMultiSelectActivity.this);

        builder.setTitle("확인");
        builder.setMessage("사진을 삭제하시겠습니까?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for(int i=0;i<listImageSelected.size();i++) {
                    Uri targetUri = Uri.parse("file://" + listImageSelected.get(i).getPath());
                    File file = new File(targetUri.getPath());
                    if (file.exists()){
                        file.delete();
                    }
                }
                setResult(RESULT_OK);
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
        myAlbum = intent.getStringArrayListExtra("data_1");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void mappingControls() {
        ryc_list_album = findViewById(R.id.ryc_list_album);
        toolbar_item_album = findViewById(R.id.toolbar_item_album);

    }

    @Override
    public void addList(Image img) {
        listImageSelected.add(img);
    }
    public void removeList(Image img) {
        listImageSelected.remove(img);
    }

    public class RestoreAsync extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String[] paths = new String[listImageSelected.size()];
            ArrayList<String> list = new ArrayList<>();
            int i = 0;

            for (Image img :listImageSelected){
                File imgFile = new File(img.getPath());

                int start = imgFile.getName().indexOf("_"); // 첫 _ 위치 추출
                int end = imgFile.getName().indexOf("_", 4); //  추출

                String fileRestore_folder = imgFile.getName().substring(start+1, end);

                File move_path = new File("/storage/emulated/0/Pictures/" + fileRestore_folder);      // 복원 후 폴더
                File move_path2 = new File("/storage/emulated/0/" + fileRestore_folder);      // Pictures 였을 경우 복원 후 폴더

                switch (fileRestore_folder){
                    case "Pictures":
                        File MoveFile1 = new File(move_path2,fileRestore_folder + "_" + imgFile.getName());     // 이동할 경로와 이동 후 파일명
                        list.add(MoveFile1.getPath());
                        imgFile.renameTo(MoveFile1);
                        imgFile.deleteOnExit();
                        paths[i] = MoveFile1.getPath();
                        i++;
                        break;

                    case "Screenshot":
                        File MoveFile2 = new File(move_path,fileRestore_folder + "s" + "_" + imgFile.getName());     // 이동할 경로와 이동 후 파일명
                        list.add(MoveFile2.getPath());
                        imgFile.renameTo(MoveFile2);
                        imgFile.deleteOnExit();
                        paths[i] = MoveFile2.getPath();
                        i++;
                        break;

                    default:
                        File MoveFile = new File(move_path,fileRestore_folder + "_" + imgFile.getName());     // 이동할 경로와 이동 후 파일명
                        list.add(MoveFile.getPath());
                        imgFile.renameTo(MoveFile);
                        imgFile.deleteOnExit();
                        paths[i] = MoveFile.getPath();
                        i++;
                        break;
                }

            }
            // 밑에 코드가 있어야 휴지통으로 이동한 복사본이 보임
            MediaScannerConnection.scanFile(getApplicationContext(),paths, null, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setResult(RESULT_OK);
            finish();
        }
    }

}
