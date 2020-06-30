package com.project.trajekline.wisata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.trajekline.R;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.transaksi.Adapter.Adapter_Transaksi;
import com.project.trajekline.transaksi.Model.Transaksi_Model;
import com.project.trajekline.transaksi.Transaksi;
import com.project.trajekline.wisata.Adapter.Adapter_Wisata;
import com.project.trajekline.wisata.Model.Wisata_Model;
import com.project.trajekline.wisata.Temp.Temp_Wisata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Wisata extends AppCompatActivity {
    private Adapter_Wisata adapter;
    private List<Wisata_Model> list;
    private RecyclerView listdata;
    RecyclerView.LayoutManager mManager;
    SwipeRefreshLayout swLayout;
    EditText cari;
    TextView kategori;
    private GridLayoutManager layoutManager;
    LinearLayout not_found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listdata = (RecyclerView) findViewById(R.id.listdata);
        not_found = findViewById(R.id.not_found);
        kategori = findViewById(R.id.kategori);
        if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("pantai")){
            kategori.setText("Pantai");
        }else if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("gunung")){
            kategori.setText("Gunung");
        }else if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("tamanbermain")){
            kategori.setText("Taman Bermain");
        }else if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("wisatapendidikan")){
            kategori.setText("Wisata Pendidikan");
        }else if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("semua")){
            kategori.setText("Semua");
        }
        listdata.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new Adapter_Wisata(this,(ArrayList<Wisata_Model>) list, this);
//        mManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        listdata.setLayoutManager(layoutManager);
        listdata.setAdapter(adapter);
        cari = findViewById(R.id.cari);
        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                reload(s.toString());
            }
        });
        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        swLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload("");
            }
        });
        loadJson("");
    }
    public void reload(String filter){
        not_found.setVisibility(View.GONE);
        list.clear();
        loadJson(filter); // your code
        listdata.getAdapter().notifyDataSetChanged();
        swLayout.setRefreshing(false);
    }
    private void loadJson(String filter)
    {
        Intent data = getIntent();
        String link = "";
        if(Temp_Wisata.getInstance(getBaseContext()).getKategori().equals("semua")){
             if(filter.equals("")){
                 link = ServerAccess.WISATA+"list/";
             }else{
                 link = ServerAccess.WISATA+"filter/"+filter;
             }
        }else{
            if(filter.equals("")){
                link = ServerAccess.WISATA+"list/"+ Temp_Wisata.getInstance(getBaseContext()).getKategori();
            }else{
                link = ServerAccess.WISATA+"filter/"+filter+"/"+ Temp_Wisata.getInstance(getBaseContext()).getKategori();
            }
        }
        StringRequest senddata = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray res = null;
                try {
                    res = new JSONArray(response);
//                    JSONArray arr = res.getJSONArray("data");
                    if(res.length() > 0) {
                        for (int i = 0; i < res.length(); i++) {
                            try {
                                JSONObject data = res.getJSONObject(i);
                                Wisata_Model md = new Wisata_Model();
                                md.setKode(data.getString("id_paket"));
                                md.setNama(data.getString("nama_paket"));
                                md.setHarga(ServerAccess.numberConvert(data.getString("harga")));
                                md.setCover(ServerAccess.BASE_URL+"asset/img/destinasi/"+ data.getString("foto"));
                                list.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();
                                Log.d("pesan", ea.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        not_found.setVisibility(View.VISIBLE);
//                        Toast.makeText(Wisata.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Wisata.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Wisata.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
