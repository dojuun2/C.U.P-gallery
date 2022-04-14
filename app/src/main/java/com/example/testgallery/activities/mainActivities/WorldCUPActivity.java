package com.example.testgallery.activities.mainActivities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testgallery.R;
import com.example.testgallery.adapters.WC_recyclerAdapter;
import com.example.testgallery.adapters.WC_recyclerlistAdapter;
import com.example.testgallery.models.Image;
import com.smarteist.autoimageslider.SliderView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class WorldCUPActivity<image> extends AppCompatActivity implements WC_AdapterEndingListener,WC_LongClickListener {
    private SliderView sliderView;
    private ImageView img_back_wolrd_cup, img_help;
    private Toolbar toolbar_slide;
    private ArrayList<image> imageList;
    private Intent intent;
    private ArrayList<String> list ;
    private ArrayList<String> WCGRIDlist;
    private Long mLastClickTime = 0L;
    private static final String logTag = "ggoog";
    private WC_recyclerAdapter adapter,adapter1;
    private WC_recyclerlistAdapter listadapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerlistView;
    ArrayList<String> Savelist = new ArrayList<>();
    ArrayList<String> Deletelist = new ArrayList<>();


    private int endnum = 0;


    ImageView WC_image1;
    ImageView WC_image2;
    int i = 2;
    int j;
    int k;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldcup);
        intent = getIntent();
        list = new ArrayList<>();
        WCGRIDlist = new ArrayList<>();
        list = intent.getStringArrayListExtra("data_worldlist");
        WCGRIDlist.addAll(list);
        Log.d("TAG", "00000000000000000000000000000000000000-- ==="+WCGRIDlist);


        mappingControls();
        event();
        init();
        getData();



    }

    public void result() {
        Intent intent = new Intent(WorldCUPActivity.this, WorldCUPActivity_result.class);
        intent.putStringArrayListExtra("WC_savelist", Savelist);
        intent.putStringArrayListExtra("WC_deletelist", Deletelist);
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

        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorldCUPActivity.this, WC_HelpExample.class);
                startActivity(intent);
            }
        });


    }

    private void mappingControls() {

        img_back_wolrd_cup = findViewById(R.id.img_back_world_cup);
        img_help = findViewById(R.id.img_help);
    }


    private void init() {


        recyclerView = findViewById(R.id.WC_recycler_play);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1 = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };

        recyclerView.setLayoutManager(linearLayoutManager1);
        adapter = new WC_recyclerAdapter();
        recyclerView.setAdapter(adapter);



        recyclerlistView = findViewById(R.id.WC_recycler_playlist);
        LinearLayoutManager listLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerlistView.setLayoutManager(listLayoutManager1);
        listadapter = new WC_recyclerlistAdapter();
        recyclerlistView.setAdapter(listadapter);

        WC_listItemDecorater itemDecorater = new WC_listItemDecorater(5);
        recyclerlistView.addItemDecoration(itemDecorater);


      //  swipeHelper1 = new ItemTouchHelper(new WC_MySwipeHelper(adapter1));
     //   swipeHelper1.attachToRecyclerView(recyclerView);




        WC_MySwipeHelper swipeHelper= new WC_MySwipeHelper(WorldCUPActivity.this,recyclerView,300,adapter) {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<WC_MySwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(WorldCUPActivity.this,
                        "SAVE",
                        0,
                        R.drawable.wc_downloadimg,
                        Color.parseColor("#B2C7D9"),

                        new WC_MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Savelist.add(list.get(viewHolder.getAdapterPosition()));
                                list.remove(viewHolder.getAdapterPosition());
                                count(1);
                                if (list.size()<2){
                                    adapter.getdeletelist();
                                }

                                // 해당 항목 삭제
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.

                            }




                        }));
            }

        };// swipeHelper



    }

    private void getData() {
        adapter.addItem(list,this,this );
        adapter.notifyDataSetChanged();
        listadapter.addItem(WCGRIDlist);
        listadapter.notifyDataSetChanged();


    }


    @Override
    public void niceEnding(int endnum,ArrayList<String> Deletelist) {
        if (endnum== 2)
        {
            Savelist.add(list.get(0));
            this.Deletelist = Deletelist;
            result();
        }

    }

    @Override
    public void count(int count) {
        i += count;
        Log.d("TAG","0000000000000000000000000 ========"+i);
        recyclerlistView.scrollToPosition(i);

    }


    @Override
    public void onClick(int num) {
        
        ArrayList<String> image = new ArrayList<>();
        image.add(list.get(num));

        Intent intent = new Intent(this,WC_longClick.class);
        intent.putStringArrayListExtra("image",image);
        startActivityForResult(intent,1);
    }
}




