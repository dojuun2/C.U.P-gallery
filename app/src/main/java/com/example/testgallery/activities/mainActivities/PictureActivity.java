package com.example.testgallery.activities.mainActivities;


import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.ParcelFileDescriptor;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.testgallery.R;
import com.example.testgallery.activities.mainActivities.data_favor.DataLocalManager;
import com.example.testgallery.activities.subActivities.ItemAlbumMultiSelectActivity;
import com.example.testgallery.adapters.AlbumSheetAdapter;
import com.example.testgallery.adapters.SearchRVAdapter;
import com.example.testgallery.adapters.SlideImageAdapter;
import com.example.testgallery.fragments.mainFragments.BottomSheetFragment;
import com.example.testgallery.fragments.mainFragments.PhotoFragment;
import com.example.testgallery.models.Album;
import com.example.testgallery.models.Image;
import com.example.testgallery.models.SearchRV;
import com.example.testgallery.utility.FileUtility;
import com.example.testgallery.utility.GetAllPhotoFromGallery;
import com.example.testgallery.utility.IClickListener;
import com.example.testgallery.utility.ImageUtil;
import com.example.testgallery.utility.PictureInterface;
import com.example.testgallery.utility.SubInterface;
import com.example.testgallery.utility.ToastUtil;
import com.example.testgallery.viewmodel.PhotoViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PictureActivity extends AppCompatActivity implements PictureInterface, SubInterface {
    private ViewPager viewPager_picture;
    private Toolbar toolbar_picture;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;
    private BottomNavigationView bottomNavigationView3;
    private FrameLayout frame_viewPager;
    private ArrayList<String> imageListThumb;
    private ArrayList<String> imageListPath;
    private Intent intent;
    private int pos;
    private SlideImageAdapter slideImageAdapter;
    private PictureInterface activityPicture;
    private String imgPath;
    private String imageName;
    private String thumb;
    private Bitmap imageBitmap;
    private String title, link, displayedLink, snippet;
    private RecyclerView resultsRV;
    private SearchRVAdapter searchRVAdapter;
    //private ArrayList<SearchRV> searchRVArrayList;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView ryc_album;
    public static Set<String> imageListFavor = DataLocalManager.getListSet();
public static final int GALLERY_REQUEST = 1;

    private Uri imageListUri;

    TextView colorName;
    TextView colorHexa;
    TextView colorRGB;
    ImageView imageView;


    PhotoViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        imageListFavor = DataLocalManager.getListSet();
    }

    private void updateDisplay() {
        viewModel.getImage().observe(this, image -> {
            colorName.setText(image.getColor());
            Log.d("TAGG", "viewModel 00000000000000000000000000000000000000000000000000 - =" + image.getColor());
            colorHexa.setText(image.getHexadecimal());
            Log.d("TAGG", "viewModel 00000000000000000000000000000000000000000000000000 - =" + image.getHexadecimal());

            colorRGB.setText(image.getRGB());
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        //Fix Uri file SDK link: https://stackoverflow.com/questions/48117511/exposed-beyond-app-through-clipdata-item-geturi?answertab=oldest#tab-top
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        imageView = (ImageView) findViewById(R.id.imgPhoto);
        colorName = (TextView) findViewById(R.id.color_name_gallery);
        colorHexa = (TextView) findViewById(R.id.color_hexa_gallery);
        colorRGB = (TextView) findViewById(R.id.color_rgb_gallery);
        intent = getIntent();

        mappingControls();

        events();
    }

    private void events() {
        setDataIntent();
        setUpToolBar();
        setUpSilder();
        initializeUi();
        bottomNavigationViewEvents();
    }

    void initializeUi() {
        viewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);

        try {
            Uri imageUri = Uri.parse("file://" + imageListPath.get(pos));

            Log.d("TAg","000000000000000000000000000ddddddddddddddddddddddd = " + imageUri);
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            float aspectRatio = selectedImage.getWidth() /
                (float) selectedImage.getHeight();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = Math.round(width / aspectRatio);
            Bitmap.createScaledBitmap(selectedImage, width, height, false);

            // imageView.setImageBitmap(resizeBitmap(selectedImage));
            Log.d("TAg","000000000000000000000000000ddddddddddddddddddddddd = " + selectedImage);
            Log.d("TAg","000000000000000000000000000ddddddddddddddddddddddd = " + viewModel);
            if (!viewModel.isImageTaken()) {
                viewModel.setImage(ImageUtil.mapBitmapToImage(selectedImage));
            } else {
                updateDisplay();
            }
            updateDisplay();
        } catch (FileNotFoundException e) {
            Log.d("TAg","33333333333333333333333333333333333333 = " + imageListPath);
            e.printStackTrace();
            ToastUtil.callShortToast(PictureActivity.this, R.string.pick_image_error_msg);
        }
    }


    private void bottomNavigationViewEvents() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Uri targetUri = Uri.parse("file://" + thumb);


                switch (item.getItemId()) {

                    case R.id.sharePic:

                        if(thumb.contains("gif")){
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, targetUri);
                            startActivity( Intent.createChooser(share, "사진을 지인들에게 공유해보세요") );
                        }
                        else {
                            Drawable mDrawable = Drawable.createFromPath(imgPath);
                            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "사진 묘사", null);
                            thumb = thumb.replaceAll(" ", "");

                            Uri uri = Uri.parse(path);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(shareIntent, "사진 공유"));
                        }

                        break;

                    case R.id.editPic:
                        Intent editIntent = new Intent(PictureActivity.this, DsPhotoEditorActivity.class);

                        if(imgPath.contains("gif")){
                            Toast.makeText(PictureActivity.this,"GIF 이미지를 편집할 수 없습니다",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // Set data
                            editIntent.setData(Uri.fromFile(new File(imgPath)));
                            // Set output directory
                            editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Simple Gallery");
                            // Set toolbar color
                            editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF000000"));
                            // Set background color
                            editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FF000000"));
                            // Start activity
                            startActivity(editIntent);
                        }

                        break;

                    case R.id.starPic:

                        if(!imageListFavor.add(imgPath)){
                            imageListFavor.remove(imgPath);
                        }

                        DataLocalManager.setListImg(imageListFavor);
                        Toast.makeText(PictureActivity.this, imageListFavor.size()+"", Toast.LENGTH_SHORT).show();
                        if(!check(imgPath)){
                            bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star);
                        }
                        else{
                            bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star_red);

                        }
                        break;

                    case R.id.deletePic:

                        AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);

                        builder.setTitle("확인");
                        builder.setMessage("사진을 삭제하시겠습니까?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(targetUri.getPath());

                                if (file.exists()) {
                                    File img = new File(imgPath);

                                    String folderName = "휴지통";      // 생성할 폴더 이름
                                    String afterFilePath = "/storage/emulated/0/Pictures";      // 휴지통 상위 폴더
                                    String path = afterFilePath+"/"+folderName;     // 휴지통 경로
                                    File dir = new File(path);

                                    if(!dir.exists()) {
                                        dir.mkdir();
                                    }

                                    File imgFile = new File(img.getPath());
                                    // 파일 최종수정일자를 현재 시간으로 변경
                                    imgFile.setLastModified(System.currentTimeMillis());

                                    String imgFile_path = imgFile.getPath().replace("/" + imgFile.getName(), "");       // 파일의 이름까지 포함된 경로
                                    String parentFolder = imgFile_path.substring(imgFile_path.lastIndexOf("/") + 1);        // 삭제할 파일이 있는 폴더 이름
                                    
                                    File desImgFile = new File(path,"휴지통" + "_" + parentFolder + "_" + imgFile.getName());

                                    imgFile.renameTo(desImgFile);
                                    imgFile.deleteOnExit();     // 임시파일 삭제
                                    desImgFile.getPath();

                                    // 밑에 코드가 있어야 휴지통으로 이동한 복사본이 보임
                                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path+File.separator+desImgFile.getName()}, null, null);
                                }
                                finish();
                                dialog.dismiss();
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
                }
                return true;
            }

        });
    }

    private void showNavigation(boolean flag) {
        if (!flag) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
            toolbar_picture.setVisibility(View.INVISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar_picture.setVisibility(View.VISIBLE);
        }
    }


    private void setUpToolBar() {
        // Toolbar events
        toolbar_picture.inflateMenu(R.menu.menu_top_picture);
        setTitleToolbar("abc");

        // Show back button
        toolbar_picture.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar_picture.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Show info
        toolbar_picture.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.menuInfo:
                        Uri targetUri = Uri.parse("file://" + thumb);
                        if (targetUri != null) {
                            showExif(targetUri);
                        }
                        break;
                    case R.id.menuAddAlbum:
                        openBottomDialog();
                        break;
                    case R.id.menuAddSecret:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);

                        builder.setTitle("확인");
                        builder.setMessage("이미지를 숨기거나 표시하시겠습니까?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String scrPath = Environment.getExternalStorageDirectory()+File.separator+".secret";
                                File scrDir = new File(scrPath);
                                if(!scrDir.exists()){
                                    Toast.makeText(PictureActivity.this, "비밀 앨범을 만들지 않았습니다", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    FileUtility fu = new FileUtility();
                                    File img = new File(imgPath);
                                    if(!(scrPath+File.separator+img.getName()).equals(imgPath)){
                                        fu.moveFile(imgPath,img.getName(),scrPath);
                                        Toast.makeText(PictureActivity.this, "이미지가 숨겨진 상태입니다", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String outputPath = Environment.getExternalStorageDirectory()+File.separator+"DCIM" + File.separator + "Restore";
                                        File folder = new File(outputPath);
                                        File imgFile = new File(img.getPath());
                                        File desImgFile = new File(outputPath,imgFile.getName());
                                        if(!folder.exists()) {
                                            folder.mkdir();
                                        }
                                        imgFile.renameTo(desImgFile);
                                        imgFile.deleteOnExit();
                                        desImgFile.getPath();
                                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{outputPath+File.separator+desImgFile.getName()}, null, null);
                                    }
                                }
                                Intent intentResult = new Intent();
                                intentResult.putExtra("path_img", imgPath);
                                setResult(RESULT_OK, intentResult);
                                finish();
                                dialog.dismiss();
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
                    case R.id.setWallpaper:
                        Uri uri_wallpaper = Uri.parse("file://" + thumb);
                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setDataAndType(uri_wallpaper, "image/*");
                        intent.putExtra("mimeType", "image/*");
                        startActivity(Intent.createChooser(intent, "Set as:"));
                    case R.id.searchImage:
                        searchImage();
                }

                return true;
            }
        });
    }

    private void searchImage(){
        SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute();
    }

    public class SearchAsyncTask extends AsyncTask<Void, Integer, Void> {

        private ProgressDialog mProgressDialog ;
        ArrayList<SearchRV> searchRVArrayList = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            //searchRVAdapter = new SearchRVAdapter(searchRVArrayList,PictureActivity.this);
            getResults(searchRVArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(searchRVArrayList, new IClickListener() {
                @Override
                public void clickItem(SearchRV searchRV) {
                    Toast.makeText(PictureActivity.this,"테스트",Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            mProgressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(PictureActivity.this);
            mProgressDialog.setMessage("잠시만 기다려주세요");
            mProgressDialog.show();
            searchRVArrayList.add(new SearchRV("Test 1","https://ashpex.eu.org","https://ashpex.eu.org","test test test"));
            searchRVArrayList.add(new SearchRV("Test 2","https://ashpex.eu.org","https://ashpex.eu.org","test test test"));
        }

    }


    private void getResults(ArrayList<SearchRV> searchRVArrayList){
        Uri imageUri = Uri.parse("file://" + thumb);
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(imageUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            imageBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                String searchQuery = firebaseVisionImageLabels.get(0).getText();
                getSearchResults(searchQuery, searchRVArrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PictureActivity.this, "사진을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchResults(String searchQuery, ArrayList<SearchRV> searchRVArrayList){
        String apiKey = "51f619982c077bb7ef5cb7e50667ec174162dcae9f75f6c3a5ef88b00a7d305e";
        String url = "https://serpapi.com/search.json?q=" + searchQuery.trim() + "&hl=en&gl=us&google_domain=google.com&api_key=" + apiKey;
        //String url = "https://serpapi.com/search.json?engine=google&q="+searchQuery+"&api_key=51f619982c077bb7ef5cb7e50667ec174162dcae9f75f6c3a5ef88b00a7d305e";
        RequestQueue queue = Volley.newRequestQueue(PictureActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray organicArray = response.getJSONArray("organic_results");
                    for(int i = 0; i < organicArray.length(); i++){
                        JSONObject organicObj = organicArray.getJSONObject(i);

                        if(organicObj.has("title")){
                            title = organicObj.getString("title");
                        }
                        if(organicObj.has("link")){
                            link = organicObj.getString("link");
                        }
                        if(organicObj.has("displayed_link")){
                            displayedLink = organicObj.getString("displayed_link");
                        }
                        if(organicObj.has("snippet")){
                            snippet = organicObj.getString("snippet");
                        }
                        searchRVArrayList.add(new SearchRV(title,link,displayedLink,snippet));
                    }
                    //searchRVAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PictureActivity.this,"결과를 찾을 수 없습니다",Toast.LENGTH_SHORT).show();;
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void showExif(Uri photoUri) {
        if (photoUri != null) {

            ParcelFileDescriptor parcelFileDescriptor = null;

            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(photoUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                ExifInterface exifInterface = new ExifInterface(fileDescriptor);

                BottomSheetDialog infoDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
                View infoDialogView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_info,
                                (LinearLayout) findViewById(R.id.infoContainer),
                                false
                        );
                TextView txtInfoProducer = (TextView) infoDialogView.findViewById(R.id.txtInfoProducer);
                TextView txtInfoSize = (TextView) infoDialogView.findViewById(R.id.txtInfoSize);
                TextView txtInfoModel = (TextView) infoDialogView.findViewById(R.id.txtInfoModel);
                TextView txtInfoFlash = (TextView) infoDialogView.findViewById(R.id.txtInfoFlash);
                TextView txtInfoFocalLength = (TextView) infoDialogView.findViewById(R.id.txtInfoFocalLength);
                TextView txtInfoAuthor = (TextView) infoDialogView.findViewById(R.id.txtInfoAuthor);
                TextView txtInfoTime = (TextView) infoDialogView.findViewById(R.id.txtInfoTime);
                TextView txtInfoName = (TextView) infoDialogView.findViewById(R.id.txtInfoName);
                TextView txtInfoGps1 = (TextView) infoDialogView.findViewById(R.id.txtInfoGps1);
                TextView txtInfoGps11 = (TextView) infoDialogView.findViewById(R.id.txtInfoGps11);
                TextView txtInfoGps22 = (TextView) infoDialogView.findViewById(R.id.txtInfoGps22);
                TextView txtInfoGps2 = (TextView) infoDialogView.findViewById(R.id.txtInfoGps2);

                txtInfoName.setText(imageName);
                txtInfoProducer.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_MAKE));
                txtInfoSize.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_LENGTH) + "x" + exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_WIDTH));
                txtInfoModel.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_MODEL));
                txtInfoFlash.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_FLASH));
                txtInfoFocalLength.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_FOCAL_LENGTH));
                txtInfoAuthor.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_ARTIST));
                txtInfoTime.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_DATETIME));
                txtInfoGps1.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE));
                txtInfoGps11.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE_REF));
                txtInfoGps22.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE));
                txtInfoGps2.setText(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE_REF));


                infoDialog.setContentView(infoDialogView);
                infoDialog.show();


                parcelFileDescriptor.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "오류 발생:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "오류 발생:\n" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getApplicationContext(),
                    "photoUri가 존재하지 않습니다",
                    Toast.LENGTH_LONG).show();
        }
    }

    ;



    private void setUpSilder() {

        slideImageAdapter = new SlideImageAdapter();
        slideImageAdapter.setData(imageListThumb, imageListPath);
        slideImageAdapter.setContext(getApplicationContext());
        slideImageAdapter.setPictureInterface(activityPicture);
        viewPager_picture.setAdapter(slideImageAdapter);
        viewPager_picture.setCurrentItem(pos);

        viewPager_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                thumb = imageListThumb.get(position);
                imgPath = imageListPath.get(position);
                setTitleToolbar(thumb.substring(thumb.lastIndexOf('/') + 1));
                if(!check(imgPath)){
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star);
                }
                else{
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star_red);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setDataIntent() {
        intent = getIntent();
        imageListPath = intent.getStringArrayListExtra("data_list_path");
        imageListThumb = intent.getStringArrayListExtra("data_list_thumb");
        pos = intent.getIntExtra("pos", 0);
        activityPicture = this;

    }

    private void mappingControls() {
        viewPager_picture = findViewById(R.id.viewPager_picture);
        bottomNavigationView = findViewById(R.id.bottom_picture);



        toolbar_picture = findViewById(R.id.toolbar_picture);
        frame_viewPager = findViewById(R.id.frame_viewPager);
        
        bottomNavigationView2 = findViewById(R.id.bottom_trash_picture);
        bottomNavigationView2.setVisibility(View.INVISIBLE);
        bottomNavigationView3 = findViewById(R.id.bottom_wc_picture);
        bottomNavigationView3.setVisibility(View.INVISIBLE);

    }

    public Boolean check(String  Path){
        for (String img: imageListFavor) {
            if(img.equals(Path)){
                return true;
            }
        }
        return false;
    }

    public void setTitleToolbar(String imageName) {
        this.imageName = imageName;
        toolbar_picture.setTitle(imageName);

    }

    public void showDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
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
    //bottomDialog Add to album
        private void openBottomDialog() {
            View viewDialog = LayoutInflater.from(PictureActivity.this).inflate(R.layout.layout_bottom_sheet_add_to_album, null);
            ryc_album = viewDialog.findViewById(R.id.ryc_album);
            ryc_album.setLayoutManager(new GridLayoutManager(this, 2));

            bottomSheetDialog = new BottomSheetDialog(PictureActivity.this);
            bottomSheetDialog.setContentView(viewDialog);
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();

        }

    @Override
    public void actionShow(boolean flag) {
        showNavigation(flag);
    }

    @Override
    public void add(Album album) {
        AddAlbumAsync addAlbumAsync = new AddAlbumAsync();
        addAlbumAsync.setAlbum(album);
        addAlbumAsync.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        private AlbumSheetAdapter albumSheetAdapter;
        private List<Album> listAlbum;
        @Override
        protected Void doInBackground(Void... voids) {
            List<Image> listImage = GetAllPhotoFromGallery.getAllImageFromGallery(PictureActivity.this);
            listAlbum = getListAlbum(listImage);
            String path_folder = imgPath.substring(0, imgPath.lastIndexOf("/"));
            for(int i =0;i<listAlbum.size();i++) {
                if(path_folder.equals(listAlbum.get(i).getPathFolder())) {
                    listAlbum.remove(i);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            albumSheetAdapter = new AlbumSheetAdapter(listAlbum, PictureActivity.this);
            albumSheetAdapter.setSubInterface(PictureActivity.this);
            ryc_album.setAdapter(albumSheetAdapter);
            bottomSheetDialog.show();
        }
        @NonNull
        private List<Album> getListAlbum(List<Image> listImage) {
            List<String> ref = new ArrayList<>();
            List<Album> listAlbum = new ArrayList<>();

            for (int i = 0; i < listImage.size(); i++) {
                String[] _array = listImage.get(i).getThumb().split("/");
                String _pathFolder = listImage.get(i).getThumb().substring(0, listImage.get(i).getThumb().lastIndexOf("/"));
                String _name = _array[_array.length - 2];
                if (!ref.contains(_pathFolder)) {
                    ref.add(_pathFolder);
                    Album token = new Album(listImage.get(i), _name);
                    token.setPathFolder(_pathFolder);
                    token.addItem(listImage.get(i));
                    listAlbum.add(token);
                } else {
                    listAlbum.get(ref.indexOf(_pathFolder)).addItem(listImage.get(i));
                }
            }

            return listAlbum;
        }
    }
    public class AddAlbumAsync extends AsyncTask<Void, Integer, Void> {
        Album album;
        public void setAlbum(Album album) {
            this.album = album;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File directtory = new File(album.getPathFolder());
            if(!directtory.exists()){
                directtory.mkdirs();
                Log.e("File-no-exist",directtory.getPath());
            }
            String[] paths = new String[1];
            File imgFile = new File(imgPath);
            File desImgFile = new File(album.getPathFolder(),album.getName()+"_"+imgFile.getName());
            imgFile.renameTo(desImgFile);
            imgFile.deleteOnExit();
            paths[0] = desImgFile.getPath();
            for (String imgFavor: imageListFavor){
                if(imgFavor.equals(imgFile.getPath())){
                    imageListFavor.remove(imgFile.getPath());
                    imageListFavor.add(desImgFile.getPath());
                    break;
                }
            }
            DataLocalManager.setListImg(imageListFavor);
            MediaScannerConnection.scanFile(getApplicationContext(),paths, null, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            bottomSheetDialog.cancel();
        }
    }
}
