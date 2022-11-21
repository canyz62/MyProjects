package com.example.mapwithmarker;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class RecyclerViewItem implements Serializable {

    private int mID;

    public int getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    private int favoriteID;
    private int mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String mText5;
    private double mText6;
    private Boolean Selected = false;
    private Boolean Defekt = false;

    private Boolean istInDerNaehe = false;

    private String DefektText;

    public RecyclerViewItem(int ID, int imageResource, String text1, String text2, String text3, String text4, String text5, double text6/*, boolean choice*/) {
        mID = ID;
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
        mText5 = text5;
        mText6 = text6;

    }

    public Boolean getIstInDerNaehe() {
        return istInDerNaehe;
    }

    public int getID() {
        return mID;
    }

    public int getImageResource(){
        return mImageResource;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

    public String getText3(){
        return mText3;
    }

    public String getText4(){
        return mText4;
    }

    public String getText5(){
        return mText5;
    }

    public double getText6(){
        return mText6;
    }

    public String getDefektText() { return DefektText; }


    public Boolean isSelected() {
        Boolean b = false;
        if(Selected == true) {
            b = true;
        }
        return b;
    }

    public Boolean isDefekt() {
        Boolean b = false;
        if(Defekt == true) {
            b = true;
        }
        return b;
    }

    void setSelected(boolean choice) {
        Selected = choice;
    }

    void setDefekt(boolean choice) {
        Defekt = choice;
    }

    public void setDefektText(String defektText) {
        DefektText = defektText;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }
    public void setIstInDerNaehe(Boolean istInDerNaehe) {
        this.istInDerNaehe = istInDerNaehe;
    }



}
