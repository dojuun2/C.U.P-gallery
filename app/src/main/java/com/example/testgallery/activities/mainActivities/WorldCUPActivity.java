package com.example.testgallery.activities.mainActivities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testgallery.R;
import com.example.testgallery.activities.subActivities.ItemAlbumMultiSelectActivity;
import com.example.testgallery.adapters.SlideShowAdapter;
import com.example.testgallery.models.Image;
import com.example.testgallery.utility.GetAllPhotoFromGallery;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class WorldCUPActivity<image> extends AppCompatActivity {
    private SliderView sliderView;
    private ImageView img_back_wolrd_cup;
    private Toolbar toolbar_slide;
    private ArrayList<image> imageList;
    private Intent intent;
    private ArrayList<String> list;
    private Long mLastClickTime = 0L;
    private static final String logTag = "ggoog";
    ArrayList<Integer> delete_list = new ArrayList<>();
    final OnSwipeTouchListener swipeDetector = new OnSwipeTouchListener();

    ImageView WC_image1;
    ImageView WC_image2;
    int i;
    int j;
    int k;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);
        intent = getIntent();
        list = intent.getStringArrayListExtra("data_worldlist");


        mappingControls();
        event();
        gallery();


    }

    private void result() {

        Intent intent = new Intent(WorldCUPActivity.this, WorldCUPActivity_result.class);
        intent.putStringArrayListExtra("WC_list",list);
        intent.putExtra("WC_deletenum",delete_list);
        startActivity(intent);
        finish();
    }




    private void event() {

        img_back_wolrd_cup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void mappingControls() {

        img_back_wolrd_cup = findViewById(R.id.img_back_world_cup);
        WC_image1 = (ImageView)findViewById(R.id.WC_image_1);
        WC_image2 = (ImageView)findViewById(R.id.WC_image_2);


    }

    private void gallery() {
        j=0; // WC_image1 의 값
        k=1;// WC_image2 의 값
        i=2;



        WC_image1.setOnTouchListener(swipeDetector);
        WC_image2.setOnTouchListener(swipeDetector);
        WC_image1.setImageDrawable(Drawable.createFromPath(list.get(0)));
        WC_image2.setImageDrawable(Drawable.createFromPath(list.get(1)));
        WC_image1.setOnClickListener(new worldcc());
        WC_image2.setOnClickListener(new worldcc());
        WC_image1.setOnLongClickListener(new worldcc());
        WC_image2.setOnLongClickListener(new worldcc());

        MyAdapter adapter = new MyAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.item_worldcup_gallerryview,
                list);

        Gallery g = (Gallery)findViewById(R.id.WC_gallery_view);
        g.setAdapter(adapter);



    }

    private void WC_LRswipe1() {
        if (i >= list.size()) {
            result();
        } else {
            j = i;
            WC_image1.setImageDrawable(Drawable.createFromPath(list.get(j)));
            i++;

        }
    }

    private void WC_LRswipe2() {
        if( i >= list.size()) {
            result();
        }
        else{
            k= i;
            WC_image2.setImageDrawable(Drawable.createFromPath(list.get(k)));
            i++;
        }
    }

    class worldcc implements View.OnClickListener,View.OnLongClickListener {


        @Override
        public void onClick(View view) {


            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                switch (view.getId()) {
                    case R.id.WC_image_1:


                        if (swipeDetector.swipeDetected()) {
                            //스와이프 처리
                            if (swipeDetector.getAction() == OnSwipeTouchListener.Action.LR) {
                                WC_LRswipe1();

                            }
                        } else {
                            if (i >= list.size()) {
                                delete_list.add(k);
                                result();
                            } else {
                                delete_list.add(k);
                                k = i;
                                WC_image2.setImageDrawable(Drawable.createFromPath(list.get(k)));
                                i++;
                            }
                        }


                        break;


                    case R.id.WC_image_2:
                        if (swipeDetector.swipeDetected()) {
                            //스와이프 처리
                            if (swipeDetector.getAction() == OnSwipeTouchListener.Action.LR) {
                                WC_LRswipe2();
                            }
                        } else {
                            if (i >= list.size()) {
                                delete_list.add(j);
                                result();
                            } else {
                                delete_list.add(j);
                                j = i;
                                WC_image1.setImageDrawable(Drawable.createFromPath(list.get(j)));
                                i++;

                            }
                        }
                        break;
                }
                Log.i(logTag, "1초 지남");
            }
            mLastClickTime = SystemClock.elapsedRealtime();


        }

        @Override
        public boolean onLongClick(View view) {
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                switch (view.getId()) {
                    case R.id.WC_image_1:
                        if (swipeDetector.swipeDetected()) {
                            //스와이프 처리
                            if (swipeDetector.getAction() == OnSwipeTouchListener.Action.LR) {
                                WC_LRswipe1();
                            }
                        } else {
                            Intent intent = new Intent(WorldCUPActivity.this, WC_longClick.class);
                            intent.putExtra("longclicked", list.get(j));
                            startActivityForResult(intent, 1);
                        }
                        break;
                    case R.id.WC_image_2:

                        if (swipeDetector.swipeDetected()){
                            //스와이프 처리
                            if(swipeDetector.getAction()== OnSwipeTouchListener.Action.LR){
                                WC_LRswipe2();
                            }
                        } else {
                            Intent intent = new Intent(WorldCUPActivity.this, WC_longClick.class);
                            intent.putExtra("longclicked", list.get(k));
                            startActivityForResult(intent, 1);
                        }
                        break;

                }
                Log.i(logTag, "롱 1초 지남");
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
    }
}

class MyAdapter extends BaseAdapter {
    Context context;
    int layout;

    ArrayList<String> list;
    LayoutInflater inf;

    public MyAdapter(Context context, int layout, ArrayList<String> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { // 보여줄 데이터의 총 개수 - 꼭 작성해야 함
        return list.size();
    }

    @Override
    public Object getItem(int position) { // 해당행의 데이터- 안해도 됨
        return null;
    }

    @Override
    public long getItemId(int position) { // 해당행의 유니크한 id - 안해도 됨
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        ImageView iv = (ImageView)convertView.findViewById(R.id.WC_gallery_sampleimg);
        iv.setImageDrawable(Drawable.createFromPath(list.get(position)));
        return convertView;
    }
}


