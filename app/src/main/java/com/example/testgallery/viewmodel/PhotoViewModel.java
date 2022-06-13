package com.example.testgallery.viewmodel;



import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import com.example.testgallery.models.Image;

/**
 * Created by avukelic on 25-Jul-18.
 */
public class PhotoViewModel extends ViewModel {
    // 이미지를 MutableLiveData로 설정하여 객체의 상태가 바뀌면 그 객체에 의존하는 다른 객체를 변동하기 유용하게 설정
    private MutableLiveData<Image> image;

    public LiveData<Image> setImage(Image image) {
        if (this.image == null) {
            this.image = new MutableLiveData<>();
            this.image.setValue(image);
            this.image.getValue().getDominantColor();
        }
        return this.image;
    }

    public void updateImage(Image image) {
        this.image.setValue(image);
        this.image.getValue().getDominantColor();
    }


    public boolean isImageTaken(){
        return image != null;
    }
    public MutableLiveData<Image> getImage() {
        return image;
    }
}
