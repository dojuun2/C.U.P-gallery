package com.example.testgallery.fragments.mainFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.util.Pair;

import com.example.testgallery.R;
import com.example.testgallery.activities.mainActivities.ItemAlbumActivity;
import com.example.testgallery.activities.mainActivities.SlideShowActivity;
import com.example.testgallery.activities.mainActivities.data_favor.DataLocalManager;
import com.example.testgallery.adapters.CategoryAdapter;
import com.example.testgallery.adapters.ItemAlbumAdapter;

import com.example.testgallery.models.Category;
import com.example.testgallery.models.Image;
import com.example.testgallery.utility.GetAllPhotoFromGallery;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SecretFragment extends Fragment {


    private List<String> imageListPath;


    private List<Image> imageList;
    private androidx.appcompat.widget.Toolbar toolbar_secret;
    private Context context;

    private Set<String> imgListFavor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secret, container,false);
        context = view.getContext();
        toolbar_secret = view.findViewById(R.id.toolbar_secret);


        // Toolbar events
        toolbar_secret.inflateMenu(R.menu.menu_top);
        toolbar_secret.setTitle("이사모");
        toolbar_secret.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.menuSearch:
                        eventSearch(menuItem);
                        break;
                }

                return true;
            }
        });


        imageListPath = DataLocalManager.getListImg();
        imgListFavor=DataLocalManager.getListSet();
        //imageList = getListImgFavor(imageListPath);;



        return view;
    }





    private void eventSearch(@NonNull MenuItem item) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                String date1 = simpleDateFormat1.format(calendar.getTime());
                showImageByDate(date1);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showImageByDate(String date) {
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
        List<Image> imageList = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
        List<Image> listImageSearch = new ArrayList<>();

        for (Image image : imageList) {
            if (image.getDateTaken().contains(date)) {
                listImageSearch.add(image);
            }
        }

        if (listImageSearch.size() == 0) {
            Toast.makeText(getContext(), "Searched image not found", Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> listStringImage = new ArrayList<>();
            for (Image image : listImageSearch) {
                listStringImage.add(image.getPath());
            }
            Intent intent = new Intent(context, ItemAlbumActivity.class);
            intent.putStringArrayListExtra("data", listStringImage);
//            intent.putExtra("name", title);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }




    }
}