package com.example.testgallery.models;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.palette.graphics.Palette;

import com.example.testgallery.App;
import com.example.testgallery.R;
import com.example.testgallery.utility.ColorList;
import com.example.testgallery.utility.ColorName;

public class Image {
    private String path;
    private String thumb;
    private String dateTaken;
    public String getPath() {
        return path;
    }
    private int resId;
    private Bitmap bitmap;
    private String color;
    private String hexadecimal;
    private String RGB;

    private int r, g, b;

    ColorList colors;

    public Image() {
        colors = new ColorList();
    }

    // 이미지 경로를 위한 객체 지정
    public void setPath(String path) {
        this.path = path;
    }
    public String getThumb() {
        return thumb;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public Image(String path, String thumb) {
        this.path = path;
        this.thumb = thumb;
    }
    public int getResId() {
        return resId;
    }

    public String getDateTaken() {
        return dateTaken;
    }
    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }


    // 비트맵 객체 지정
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // 색 값 문자열 객체 지정
    public String getColor() {return color;}
    public void setColor(String color) {
        this.color = color;
    }

    //헥사값 문자열 객체 지정
    public String getHexadecimal() {
        return hexadecimal;
    }
    public void setHexadecimal(String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    //RGB 값 문자열 객체 지정
    public String getRGB() {
        return RGB;
    }
    public void setRGB(String RGB) {
        this.RGB = RGB;
    }

    //주요 색상에 대한 RGB, 헥사 및 이름 값 가져오기
    public void getDominantColor() {
        Palette p = Palette.from(getBitmap()).generate();
        setHexadecimal(getHexadecimal(p.getDominantSwatch().getRgb()));
        setRGB(getRgbCode(p.getDominantSwatch().getRgb()));

        if (isGray(r, g, b))
            setColor("gray");
        else {
            setColor(getColorName(r, g, b));
        }
    }


    //헥사 값을 rgb 값으로 변환
    private String getRgbCode(int intColor) {
        String color = String.format("#%06X", (0xFFFFFF & intColor));
        r = Integer.valueOf(color.substring(1, 3), 16);
        g = Integer.valueOf(color.substring(3, 5), 16);
        b = Integer.valueOf(color.substring(5, 7), 16);
        StringBuilder sb = new StringBuilder();
        sb.append("Red: ");
        sb.append(r);
        sb.append(" Green: ");
        sb.append(g);
        sb.append(" Blue: ");
        sb.append(b);
        return sb.toString();
    }

    //헥사값 가져오기
    private String getHexadecimal(int intColor) {
        return String.format("#%06X", (0xFFFFFF & intColor));
    }


    //색상이 회색인지 확인
    private boolean isGray(int r, int g, int b) {
        if (r == g && r == b) {
            return true;
        } else {
            return false;
        }
    }

    //색상이 회색이 아닌 경우 나머지 8가지 색을 확인할 수 있다.
    private String getColorName(int r, int g, int b) {
        ColorName closestMatch = null;  // 변수 null 값으로 초기화
        int minMSE = Integer.MAX_VALUE;
        int mse;
        // ColorName rgb평균계산을 활용한다.
        // 이쪽.
        for (ColorName c : colors.getColors()) {
            System.out.println(c.getName());
            mse = c.computeMSE(r, g, b);
            System.out.println(mse);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }
        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "No matched color name.";
        }
    }
}
