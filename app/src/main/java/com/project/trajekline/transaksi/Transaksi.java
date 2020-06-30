package com.project.trajekline.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.trajekline.R;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.dashboard.Dashboard;
import com.project.trajekline.transaksi.Adapter.Adapter_Transaksi;
import com.project.trajekline.transaksi.Model.Transaksi_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class Transaksi extends AppCompatActivity {
    private Adapter_Transaksi adapter;
    private List<Transaksi_Model> list;
    private RecyclerView listdata;
    RecyclerView.LayoutManager mManager;
    SwipeRefreshLayout swLayout;
    LinearLayout not_found;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Button tanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getBaseContext(), Dashboard.class));
            }
        });
        listdata = (RecyclerView) findViewById(R.id.listdata);
        tanggal = findViewById(R.id.tanggal);
        not_found = findViewById(R.id.not_found);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanTanggal();
            }
        });
        listdata.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new Adapter_Transaksi(this,(ArrayList<Transaksi_Model>) list, this);
        mManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        listdata.setLayoutManager(mManager);
        listdata.setAdapter(adapter);
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
    private void tampilkanTanggal() {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggal.setText("Tanggal :"+dateFormatter.format(newDate.getTime()));
                reload(dateFormatter.format(newDate.getTime()));
                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    public void reload(String tanggal){
        not_found.setVisibility(View.GONE);
        list.clear();
        loadJson(tanggal); // your code
        listdata.getAdapter().notifyDataSetChanged();
        swLayout.setRefreshing(false);
    }
    private void loadJson(String tanggal)
    {
        Intent data = getIntent();
        String link = ServerAccess.TRANSAKSI+"list/"+AuthData.getInstance(getBaseContext()).getNama();
        if(tanggal.isEmpty()){
            link = ServerAccess.TRANSAKSI+"list/"+ AuthData.getInstance(getBaseContext()).getNama();
        }else{
            link = ServerAccess.TRANSAKSI+"filter/"+tanggal+"/"+AuthData.getInstance(getBaseContext()).getNama();
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
                                Transaksi_Model md = new Transaksi_Model();
                                md.setKode(data.getString("id_pesan"));
                                md.setNama_customer(data.getString("nama_depan"));
                                md.setTanggal(data.getString("tgl_pesan"));
                                Log.d("tanggal activity", data.getString("tgl_tour"));
                                md.setTanggal_tour(data.getString("tgl_tour"));
                                md.setNama_paket(data.getString("nama_paket"));
                                md.setNama_wisata(data.getString("nama_wisata"));
//                                md.setId_bukti(data.getString("id_bukti"));
                                md.setStatus(data.getString("status"));
                                md.setHarga_paket(ServerAccess.numberConvert(data.getString("harga")));
                                md.setTotal(ServerAccess.numberConvert(data.getString("harga")));
                                list.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();
                                Log.d("pesan", ea.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        not_found.setVisibility(View.VISIBLE);
                        Toast.makeText(Transaksi.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Transaksi.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Transaksi.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
