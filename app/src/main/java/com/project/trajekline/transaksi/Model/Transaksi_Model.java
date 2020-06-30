package com.project.trajekline.transaksi.Model;

public class Transaksi_Model {
    String kode, nama_customer, nama_paket, status, nama_wisata, harga_paket, tanggal, total, tanggal_tour, id_bukti;

    public String getId_bukti() {
        return id_bukti;
    }

    public void setId_bukti(String id_bukti) {
        this.id_bukti = id_bukti;
    }

    public String getTanggal_tour() {
        return tanggal_tour;
    }

    public void setTanggal_tour(String tanggal_tour) {
        this.tanggal_tour = tanggal_tour;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama_wisata() {
        return nama_wisata;
    }

    public void setNama_wisata(String nama_wisata) {
        this.nama_wisata = nama_wisata;
    }

    public String getHarga_paket() {
        return harga_paket;
    }

    public void setHarga_paket(String harga_paket) {
        this.harga_paket = harga_paket;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
