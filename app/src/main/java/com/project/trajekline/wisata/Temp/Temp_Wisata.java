package com.project.trajekline.wisata.Temp;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.trajekline.config.AuthData;

public class Temp_Wisata {
    private static Temp_Wisata mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "wisata";
    private static final String kategori = "kategori";
    private Temp_Wisata(Context context){
        mCtx = context;
    }
    public static synchronized Temp_Wisata getInstance(Context context){
        if (mInstance == null){
            mInstance = new Temp_Wisata(context);
        }
        return mInstance;
    }
    public boolean SetKategori(String val){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(kategori, val);
        editor.apply();

        return true;
    }
    public String getKategori() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(kategori, null);
    }
    public boolean clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
