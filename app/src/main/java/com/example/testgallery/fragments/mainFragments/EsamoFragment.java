package com.example.testgallery.fragments.mainFragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.testgallery.R;
import com.example.testgallery.activities.mainActivities.ItemAlbumActivity;
import com.example.testgallery.models.Image;
import com.example.testgallery.utility.GetAllPhotoFromGallery;
import com.mrudultora.colorpicker.ColorPickerDialog;
import com.mrudultora.colorpicker.listeners.OnSelectColorListener;
import com.mrudultora.colorpicker.util.ColorItemShape;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EsamoFragment extends Fragment  {

    private androidx.appcompat.widget.Toolbar toolbar_esamo;
    private Context context;
    int defaultColor;
    AppCompatButton btnDialogBox;
    ColorPickerDialog colorPickerDialog;
    Button btn_apply;
    Button reset;

    SQLiteDatabase db = null;       // SQLiteDatabase에는 SQL 명령을 생성, 삭제, 실행하고 기타 일반적인 데이터베이스 관리 작업을 수행하는 메소들이 있음
    String[] result_name;   // ArrayAdapter에 넣을 배열 생성
    String[] result_color;   // ArrayAdapter에 넣을 배열 생성

    List<String> list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esamo, container, false);
        context = view.getContext();
        // 각 객체에 사용할 레이아웃 뷰를 가져온다.
        toolbar_esamo = view.findViewById(R.id.toolbar_esamo);
        btnDialogBox = view.findViewById(R.id.btnDialogBox);
        btn_apply = view.findViewById(R.id.btn_apply);
        reset = view.findViewById(R.id.reset);

        // 버튼 클릭시 다이얼로그 박스 생성
        btnDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorDialog();
            }
        });

        // 적용 버튼 시 다이얼로그에서 설정한 색 이미지 출력
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                colorPicture();

//                Log.d("TAg","33333333333333333333333333333333333333 = " + defaultColor);
//                // db에 저장된 값 조회
//                dbSelect();

                switch(defaultColor){
                    case 0:
                        Toast.makeText(getContext(), "색을 지정해주세요", Toast.LENGTH_LONG).show();
                        break;

                    case -65536:        // 버튼이 빨간색의 경우
                        List<Image> imageList = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
                        List<Image> listImageSearch = new ArrayList<>();

                        for (Image image : imageList) {
                            if (image.getPath().contains("/storage/emulated/0/Pictures/색 테스트/색 테스트_KakaoTalk_20220417_003425811_02.png")) {
                                listImageSearch.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/images.png")) {
                                listImageSearch.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (2).jpeg")) {
                                listImageSearch.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (4).jpeg")) {
                                listImageSearch.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (5).jpeg")) {
                                listImageSearch.add(image);
                            }
                        }
                        Log.d("TAg","33333333333333333333333333333333333333 = " + listImageSearch.size());

                        if (listImageSearch.size() == 0) {
                            Toast.makeText(getContext(), "검색한 사진이 존재하지 않습니다", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<String> listStringImage = new ArrayList<>();
                            for (Image image : listImageSearch) {
                                listStringImage.add(image.getPath());
                            }
                            Intent intent = new Intent(context, ItemAlbumActivity.class);
                            intent.putStringArrayListExtra("data", listStringImage);
                            intent.putExtra("name", "Color Image");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;

                    case -16776961:         // 버튼이 파란색인 경우
                        List<Image> imageList_blue = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
                        List<Image> listImageSearch_blue = new ArrayList<>();

                        for (Image image : imageList_blue) {
                            if (image.getPath().contains("/storage/emulated/0/Pictures/색 테스트/색 테스트_KakaoTalk_20220417_003425811_04.jpg")) {
                                listImageSearch_blue.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (6).jpeg")) {
                                listImageSearch_blue.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (7).jpeg")) {
                                listImageSearch_blue.add(image);
                            }
                            if (image.getPath().contains("/storage/emulated/0/Download/다운로드 (8).jpeg")) {
                                listImageSearch_blue.add(image);
                            }
                        }

                        if (listImageSearch_blue.size() == 0) {
                            Toast.makeText(getContext(), "검색한 사진이 존재하지 않습니다", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<String> listStringImage = new ArrayList<>();
                            for (Image image : listImageSearch_blue) {
                                listStringImage.add(image.getPath());
                            }
                            Intent intent = new Intent(context, ItemAlbumActivity.class);
                            intent.putStringArrayListExtra("data", listStringImage);
                            intent.putExtra("name", "Color Image");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;

                    case -16744448:         // 버튼이 초록색인 경우
                        List<Image> imageList_green = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
                        List<Image> listImageSearch_green = new ArrayList<>();

                        for (Image image : imageList_green) {
                            if (image.getPath().contains("/storage/emulated/0/Pictures/초록색/초록색_다운로드 (2).png")) {
                                listImageSearch_green.add(image);
                            }
                        }

                        if (listImageSearch_green.size() == 0) {
                            Toast.makeText(getContext(), "검색한 사진이 존재하지 않습니다", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<String> listStringImage = new ArrayList<>();
                            for (Image image : listImageSearch_green) {
                                listStringImage.add(image.getPath());
                            }
                            Intent intent = new Intent(context, ItemAlbumActivity.class);
                            intent.putStringArrayListExtra("data", listStringImage);
                            intent.putExtra("name", "Color Image");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // esamo.xml의 toolbar 지정
        toolbar_esamo.inflateMenu(R.menu.menu_top);
        toolbar_esamo.setTitle("이사모");
        toolbar_esamo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.menuSearch: // 메뉴 검색바 선택 시 eventSearch 함수 호출
                        eventSearch(menuItem);
                        break;
                }

                return true;
            }
        });
        return view;
    }
    // 색 설정 다이얼로그 출력
    public void selectColorDialog() {
        colorPickerDialog = new ColorPickerDialog.Builder(getActivity())
                .setColors()                // 색 지정
                .setColumns(5)              // 다이얼로그 2열로 지정
                .setDefaultSelectedColor(defaultColor)      // 기본선택색상 9가지로 지정
                .setColorItemShape(ColorItemShape.SQUARE)   // 색상선택모양 네모로 지정
                .setOnSelectColorListener(new OnSelectColorListener() {
                    @Override
                    public void onColorSelected(int color, int position) {
                        btnDialogBox.setBackgroundColor(color); // 색상 선택시 다이얼로그버튼 색 변경
                        defaultColor = color;                   // defaultcolor = 기본선택색상 9가지중 선택 값으로
//                    showImageByColor(defaultColor);
                    }
                
                    @Override
                    public void cancel() {
                        colorPickerDialog.dismissDialog();  //취소 버튼 생성 다이얼로그 나가기
                    }   
                })
                .build();
        
        colorPickerDialog.show();   // 다이얼로그 띄우기
    }

    public void dbSelect(){   // 항상 DB문을 쓸때는 예외처리(try-catch)를 해야한다

        try {
            Cursor resultset = db.rawQuery("select * from tableName", null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

            int count = resultset.getCount();   // db에 저장된 행 개수를 읽어온다
            result_name = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            result_color = new String[count];   // 저장된 행 개수만큼의 배열을 생성

            for (int i = 0; i < count; i++) {
                resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                String str_name = resultset.getString(0);   // 첫번째 속성
                String str_info = resultset.getString(1);   // 두번째 속성
                result_name[i] = str_name;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                result_color[i] = str_info;   // 각각의 속성값들을 해당 배열의 i번째에 저장
            }
            System.out.println("select ok");

        } catch (Exception e) {
            System.out.println("select Error :  " + e);
        }
    }

//    private void showImageByColor(int color) {
//        Toast.makeText(getContext(), color, Toast.LENGTH_LONG).show();
//        List<Image> imageList = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
//        List<Image> listImageSearch = new ArrayList<>();
//
//        for (Image image : imageList) {     // 모든 사진을 돌면서
//            if (image.getPath() == ) {
//                listImageSearch.add(image);     // 조건에 맞는 이미지를 리스트에 추가
//            }
//        }
//
//        if (listImageSearch.size() == 0){
//            Toast.makeText(getContext(), "검색한 사진을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
//        } else {
//            ArrayList<String> listStringImage = new ArrayList<>();
//            for (Image image : listImageSearch) {
//                listStringImage.add(image.getPath());
//            }
//            Intent intent = new Intent(context, ItemAlbumActivity.class);
//            intent.putStringArrayListExtra("data", listStringImage);
////            intent.putExtra("name", title);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//
//
//
////        CategoryAdapter categoryAdapter1 = new CategoryAdapter(getContext());
////        Category category = new Category(listImageSearch);
////        List<Category> categoryList = new ArrayList<>();
////        categoryList.add(category);
////
////        categoryAdapter1.setData(categoryList);
////        recyclerView.setAdapter(categoryAdapter1);
////
////        if (listImageSearch.size() != 0) {
////            synchronized (PhotoFragment.this) {
////                PhotoFragment.this.notifyAll();
////            }
////        } else {
////            Toast.makeText(getContext(), "Searched image not found", Toast.LENGTH_LONG).show();
////        }
//    }

    // 툴바 이벤트 (검색) 캘린더 뷰
    private void eventSearch(@NonNull MenuItem item) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);      // 캘린더 일 설정
        int month = calendar.get(Calendar.MONTH);   // 캘린더 월 설정
        int year = calendar.get(Calendar.YEAR);     // 캘린더 년 설정
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // 캘린더에서 선택/셋 한 값을 년 월 일로 넘겨주기 위해 값 설정
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                String date1 = simpleDateFormat1.format(calendar.getTime());
                showImageByDate(date1); // 해당 함수에서 설정된 date1으로 결과 출력
            }
        }, year, month, day);
        datePickerDialog.show(); // 다이얼로그 띄우기
    }

    private void showImageByDate(String date) {

        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show(); // 변수가 가지고있는 값을 토스트로 출력
        // 갤러리의 모든 사진/이미지를 리스트로 설정
        List<Image> imageList = GetAllPhotoFromGallery.getAllImageFromGallery(getContext());
        List<Image> listImageSearch = new ArrayList<>();
        for (Image image : imageList) {
            // 만약 이미지 리스트의 이미지에서 변수가 가지고있는 날짜를 포함한다면
            if (image.getDateTaken().contains(date)) {
                listImageSearch.add(image); // listImageSearch 리스트에 추가
            }
        }

        if (listImageSearch.size() == 0) {  // listImageSearch 의 리스트가 0이라면
            Toast.makeText(getContext(), "검색한 사진이 존재하지 않습니다", Toast.LENGTH_LONG).show();    //검색한 사진 없다고 출력
        } else {
            //있다면 listImageSearch의 리스트를 intent로 값을 넘겨주어서 startAcitivity로 출력
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