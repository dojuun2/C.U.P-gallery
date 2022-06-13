package com.example.testgallery.utility;

import android.util.Log;

/**
 * Created by avukelic on 25-Jul-18.
 */
public class ColorName {
    private int r, g, b;
    private String name;

    // r,g,b 이름 선언
    public ColorName(String name, int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
    }

    // 픽셀에 해당하는 r, g, b 를 받아 평균을 계산한다.
    // 평균은 이미지의 색을 결정하는데 활용한다.
    public int computeMSE(int pixR, int pixG, int pixB) {
        return  (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                * (pixB - b)) / 3);
    }

    // 문자열로 이름 설정
    public String getName() {
        return name;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
