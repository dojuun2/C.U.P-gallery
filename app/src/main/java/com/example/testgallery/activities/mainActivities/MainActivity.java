package com.example.testgallery.activities.mainActivities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testgallery.R;
import com.example.testgallery.adapters.ViewPagerAdapter;
import com.example.testgallery.fragments.mainFragments.FavoriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.karan.churi.PermissionManager.PermissionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;
    PermissionManager permission;
    Button down;
    private Object FavoriteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide status bar
        /*getSupportActionBar();*/
        setContentView(R.layout.activity_main);

        // permission 부분(접근 권한)
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

//        View down = findViewById(R.id.down);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);

        permission = new PermissionManager() {
            @Override
            public void ifCancelledAndCannotRequest(Activity activity) {
            }
        };
        permission.checkAndRequestPermissions(this);
        setUpViewPager();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.photo:

                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.album:

                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.esamo:

                        viewPager.setCurrentItem(2);
                        break;

                    case R.id.favorite:

                        viewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });


    }

    private void verifyStoragePermission(MainActivity mainActivity) {
    }


    // Toolbar handle


    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.setContext(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.photo).setChecked(true);
                        break;
                    case 1:

                        bottomNavigationView.getMenu().findItem(R.id.album).setChecked(true);
                        break;
                    case 2:

                        bottomNavigationView.getMenu().findItem(R.id.scret).setChecked(true);
                        break;
                    case 3:

                        bottomNavigationView.getMenu().findItem(R.id.favorite).setChecked(true);
                        break;
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission.checkResult(requestCode, permissions, grantResults);
    }


    private void loadSettings() {
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //    // 캡쳐버튼클릭
//    public void mOnCaptureClick(View v) throws Exception {
//        // 전체화면
//        View rootView = getWindow().getDecorView();
//
//        File screenShot = ScreenShot(rootView);
//        if(screenShot!=null){
//            // 갤러리에 추가
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
//
//
//
//        }
//    }
//
//        // 화면 캡쳐하기
//    public File ScreenShot(View view) throws Exception{
//        view.setDrawingCacheEnabled(true);  // 화면에 뿌릴때 캐시를 사용하게 한다
//
//        Bitmap screenshot = view.getDrawingCache();   // 캐시를 비트맵으로 변환
//
//        String filename = "screenshot.png";
//
//        File file = new File(Environment.getExternalStorageDirectory()+"Pictures", filename);  // Pictures폴더 screenshot.png 파일
//        FileOutputStream outputStream = null;
//        try{
//            File f = new File(Environment.getExternalStorageDirectory(), filename);
//
//            f.createNewFile();
//
//            OutputStream outStream = new FileOutputStream(f);
//
//            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//
//            outStream.close();
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        view.setDrawingCacheEnabled(false);
//
//        return file;
//    }

    public void ScreenshotButton(View view) {

        View rootView = getWindow().getDecorView(); // 전체화면 부분

        File screenShot = ScreenShot(rootView);
        if (screenShot != null) {
            // 갤러리에 추가
            sendBroadcast((new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot))));
        }


    }

    // 화면 캡쳐
    public File ScreenShot(View view) {


        view.setDrawingCacheEnabled(true);


        Bitmap screenBitmap = view.getDrawingCache();   // 비트맵으로 변환

        String filename = "screenshot" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);    // Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   // 비트맵 > PNG파일
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}


