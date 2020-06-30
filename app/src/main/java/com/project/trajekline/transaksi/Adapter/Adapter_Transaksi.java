package com.project.trajekline.transaksi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.trajekline.R;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.transaksi.Konfirmasi_Pembayaran;
import com.project.trajekline.transaksi.Model.Transaksi_Model;
import com.project.trajekline.wisata.Adapter.Adapter_Wisata;
import com.project.trajekline.wisata.Detail_Wisata;
import com.project.trajekline.wisata.Model.Wisata_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_Transaksi extends RecyclerView.Adapter<Adapter_Transaksi.ViewHolder> {
    private ArrayList<Transaksi_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Transaksi(Activity activity, ArrayList<Transaksi_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Transaksi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_transaksi, parent, false);
        Adapter_Transaksi.ViewHolder vh = new Adapter_Transaksi.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final Adapter_Transaksi.ViewHolder holder, int position) {
        final Adapter_Transaksi.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.kode_transaksi.setText(listdata.get(position).getKode());
        holder.nama_customer.setText(listdata.get(position).getNama_customer());
        holder.nama_paket.setText(listdata.get(position).getNama_paket());
        holder.status.setText(listdata.get(position).getStatus());
        holder.nama_wisata.setText(listdata.get(position).getNama_wisata());
        holder.harga_paket.setText(listdata.get(position).getHarga_paket());
        holder.tanggal.setText(listdata.get(position).getTanggal());
        holder.tanggal_tour.setText(listdata.get(position).getTanggal_tour());
        holder.tgl_tour = listdata.get(position).getTanggal_tour();
        holder.st = listdata.get(position).getStatus();
        String stta = listdata.get(position).getStatus();
        holder.total.setText(listdata.get(position).getTotal());
//        holder.id_bukti = listdata.get(position).getId_bukti();

        final int posisi = position;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
            try {
                strDate = sdf.parse(listdata.get(position).getTanggal_tour());
                if(stta.equals("S2") && System.currentTimeMillis() >= strDate.getTime() || stta.equals("S3") && System.currentTimeMillis() >= strDate.getTime()){
                    holder.konfirmasi.setText("Cetak");
                    holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServerAccess.BASE_URL +"bookingList/bookingFinish/"+listdata.get(posisi).getKode()));
                            v.getContext().startActivity(browserIntent);
                        }
                    });
                }else if(stta.equals("S4")){
                    holder.konfirmasi.setText("Telah Tour ");
                }else if(strDate.getTime() < System.currentTimeMillis()){
                    holder.konfirmasi.setText("Kadaluarsa");
                }else{
//                    if(id_bukti.equals("null")){
                    holder.konfirmasi.setText("Upload Bukti");
                    holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent;
                                    intent = new Intent(v.getContext(), Konfirmasi_Pembayaran.class);
                                    intent.putExtra("kode",listdata.get(posisi).getKode());
                                    v.getContext().startActivity(intent);
                                } catch (Exception e) {
                                    Log.d("pesan", "error");
                                }
                            }
                        });
//                    }else{
//                        konfirmasi.setText("Sudah Upload Bukti");
//                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        String id_bukti = "", tgl_tour = "", st = "";
        private TextView kode, nama_customer, kode_transaksi, nama_paket, status, nama_wisata, harga_paket, tanggal, tanggal_tour, total, konfirmasi;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            kode_transaksi = (TextView) v.findViewById(R.id.kode_transaksi);
            nama_customer = (TextView) v.findViewById(R.id.nama_customer);
            nama_paket = (TextView) v.findViewById(R.id.nama_paket);
            status = (TextView) v.findViewById(R.id.status);
            nama_wisata = (TextView) v.findViewById(R.id.nama_wisata);
            harga_paket = (TextView) v.findViewById(R.id.harga_paket);
            tanggal = (TextView) v.findViewById(R.id.tanggal);
            tanggal_tour = (TextView) v.findViewById(R.id.tanggal_tour);
            total = (TextView) v.findViewById(R.id.total);
            konfirmasi = (TextView) v.findViewById(R.id.konfirmasi);



        }
    }
}

