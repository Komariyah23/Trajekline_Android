package com.project.trajekline.wisata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.project.trajekline.R;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.ServerAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Detail_Wisata extends AppCompatActivity {
    TextView nama_paket, nama_wisata, harga, fasilitas, deskripsideskripsi, kategori;
    ImageView cover;
    ProgressDialog pd;
    Button pesan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nama_paket = findViewById(R.id.nama_paket);
        cover = findViewById(R.id.cover);
        nama_wisata = findViewById(R.id.nama_wisata);
        pd = new ProgressDialog(Detail_Wisata.this);
        harga = findViewById(R.id.harga);
        fasilitas = findViewById(R.id.fasilitas);
        kategori = findViewById(R.id.kategori);
        kategori = findViewById(R.id.kategori);
        pesan = findViewById(R.id.pesan);
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pesan bt = new Pesan();
                Bundle bundle = new Bundle();
                Intent data = getIntent();
                bundle.putString("kode", data.getStringExtra("kode"));
                bt.setArguments(bundle);
                bt.show(getSupportFragmentManager(), "Pesan");
            }
        });
        loadJson();
    }
    private void loadJson()
    {
        pd.setMessage("Menampilkan Data");
        pd.setCancelable(false);
        pd.show();
        final Intent data = getIntent();
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.WISATA+"detail/"+data.getStringExtra("kode"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    pd.cancel();
                    res = new JSONObject(response);
                    JSONArray arr = res.getJSONArray("paket");
                    JSONObject data = arr.getJSONObject(0);
                    nama_paket.setText(data.getString("nama_paket"));
                    nama_wisata.setText(data.getString("nama_wisata"));
                    harga.setText(ServerAccess.numberConvert(data.getString("harga")));
                    fasilitas.setText(data.getString("fasilitas"));
                    kategori.setText(data.getString("kategori"));
                    kategori.setText(data.getString("kategori"));

                    Glide.with(getBaseContext())
                            .load(ServerAccess.BASE_URL+"asset/img/destinasi/"+data.getString("foto"))
                            .into(cover);
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.cancel();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(senddata);
    }
}
