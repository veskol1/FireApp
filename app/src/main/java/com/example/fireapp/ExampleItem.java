package com.example.fireapp;

public class ExampleItem {

    private int mImage;
    private String mtext1,mtext2;

    public ExampleItem(int image, String text1,String text2){
        mImage=image;
        mtext1=text1;
        mtext2=text2;
    }

    public int getmImage() {return mImage;}
    public String getMtext1(){return mtext1;}
    public String getMtext2(){return mtext2;}
}
