package com.project.trajekline.config;

import java.text.DecimalFormat;

public class ServerAccess {
//    deklarasi untuk link link api yang dibutuhkan di aplikasi
    public static final String BASE_URL = "http://trajekline.mif-project.com/";
//    public static final String BASE_URL = "http://192.168.1.8/sinisa/";
    public static final String ROOT_API = BASE_URL+"api/";
    public static final String TRANSAKSI = ROOT_API+"transaksi/";
    public static final String WISATA = ROOT_API+"paket/";
    public static final String auth = ROOT_API+"auth/";
    public static final String LOGIN = auth+"login";
    public static final String REGISTER = auth+"register";
    public static final String KONFIRMASI =  ROOT_API+"transaksi/konfirmasi/";
    public static String numberConvert(String val){
        double v = Double.parseDouble(val);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String format = formatter.format(v);
        return "Rp "+format;
    }


}
