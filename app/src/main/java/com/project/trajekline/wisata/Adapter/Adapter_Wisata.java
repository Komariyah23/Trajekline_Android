package com.project.trajekline.wisata.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.project.trajekline.wisata.Detail_Wisata;
import com.project.trajekline.wisata.Model.Wisata_Model;

import java.util.ArrayList;

public class Adapter_Wisata extends RecyclerView.Adapter<Adapter_Wisata.ViewHolder> {
    private ArrayList<Wisata_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Wisata(Activity activity, ArrayList<Wisata_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Wisata.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_wisata, parent, false);
        Adapter_Wisata.ViewHolder vh = new Adapter_Wisata.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_Wisata.ViewHolder holder, int position) {
        final Adapter_Wisata.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.nama_paket.setText(listdata.get(position).getNama());
        holder.harga.setText(listdata.get(position).getHarga());
        Glide.with(activity)
                .load(listdata.get(position).getCover())
                .into(holder.cover);
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView kode, nama_paket, harga;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            nama_paket = (TextView) v.findViewById(R.id.nama_paket);
            harga = (TextView) v.findViewById(R.id.harga);
            cover = (ImageView) v.findViewById(R.id.cover);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent;
                        intent = new Intent(v.getContext(), Detail_Wisata.class);
                        intent.putExtra("kode", kode.getText().toString());
                        v.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Log.d("pesan", "error");
                    }
                }
            });
        }
    }
}

