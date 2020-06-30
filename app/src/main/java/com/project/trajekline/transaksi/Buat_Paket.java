package com.project.trajekline.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.trajekline.R;
import com.project.trajekline.akun.Edit_Profile;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.dashboard.Dashboard;
import com.shuhart.stepview.StepView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Buat_Paket extends AppCompatActivity {
    StepView step_view;
    Spinner jumlah_orang, lama, tiket, penginapan, jenis_fasilitas;
    EditText nama, email, kota_tujuan, tujuan_wisata, tanggal_berangkat;
    private ArrayList<String> jumlahlist, lamalist, tiketlist, penginapanlist, jenis_fasilitaslist;
    private SimpleDateFormat dateFormatter;
    LinearLayout step1, step2;
    private DatePickerDialog datePickerDialog;
    Button selanjutnya, buat_paket;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_paket);
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
        pd = new ProgressDialog(this);
        jumlahlist = new ArrayList<String>();
        lamalist = new ArrayList<String>();
        tiketlist = new ArrayList<String>();
        penginapanlist = new ArrayList<String>();
        jenis_fasilitaslist = new ArrayList<String>();
        buat_paket = findViewById(R.id.buat_paket);
        tiket = findViewById(R.id.tiket);
        tiketlist.add("Ya");
        tiketlist.add("TIDAK");
        tiket.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, tiketlist));
        penginapan = findViewById(R.id.penginapan);
        penginapanlist.add("Home Stay");
        penginapanlist.add("Bintang 2");
        penginapanlist.add("Bintang 3");
        penginapanlist.add("Bintang 4");
        penginapanlist.add("Bintang 5");
        penginapan.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, penginapanlist));

        tiket.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, tiketlist));
        jenis_fasilitas = findViewById(R.id.jenis_fasilitas);
        jenis_fasilitaslist.add("Ekonomi");
        jenis_fasilitaslist.add("Bisnis");
        jenis_fasilitaslist.add("Eksekutif");
        jenis_fasilitas.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, jenis_fasilitaslist));
        selanjutnya = findViewById(R.id.selanjutnya);
        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step_view = findViewById(R.id.step_view);
        jumlah_orang = findViewById(R.id.jumlah_orang);
        lama = findViewById(R.id.lama);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nama.getText().toString().isEmpty()){
                    nama.setFocusable(true);
                    Toast.makeText(Buat_Paket.this, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if (email.getText().toString().isEmpty()){
                    email.setFocusable(true);
                    Toast.makeText(Buat_Paket.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if (kota_tujuan.getText().toString().isEmpty()){
                    kota_tujuan.setFocusable(true);
                    Toast.makeText(Buat_Paket.this, "Kota Tujuan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if (tujuan_wisata.getText().toString().isEmpty()){
                    tujuan_wisata.setFocusable(true);
                    Toast.makeText(Buat_Paket.this, "Tujuan Wisata Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if (tanggal_berangkat.getText().toString().isEmpty()){
                    tanggal_berangkat.setFocusable(true);
                    Toast.makeText(Buat_Paket.this, "Tanggal Berangkat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    step(1);
                    step_view.go(1, true);
                }
            }
        });
        buat_paket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        for (int i = 2; i <=20;i++){
            jumlahlist.add(i+ " Orang");
        }
        jumlah_orang.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, jumlahlist));
        for (int i = 1; i <=6;i++){
            if((i - 1) != 0){
                lamalist.add(i+ " Hari "+(i-1) +" Malam");
            }else{
                lamalist.add(i+ " Hari ");
            }
        }
        lama.setAdapter(new ArrayAdapter<String>(Buat_Paket.this, R.layout.support_simple_spinner_dropdown_item, lamalist));
        kota_tujuan = findViewById(R.id.kota_tujuan);
        tujuan_wisata = findViewById(R.id.tujuan_wisata);
        tanggal_berangkat = findViewById(R.id.tanggal_berangkat);
        tanggal_berangkat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tampilkanTanggal();
            }
        });
        tanggal_berangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanTanggal();
            }
        });
        step_view.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.colorWhite))
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.colorBlue))
                .nextTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("Data Diri");
                    add("Fasilitas & Tiket");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .stepsNumber(2)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                // other state methods are equal to the corresponding xml attributes
                .commit();
        step_view.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                // 0 is the first step
                step(step);
            }
        });
    }
    private void step(int step){
        if(step == 0){
            step2.setVisibility(View.GONE);
            step1.setVisibility(View.VISIBLE);
        }else{
            step2.setVisibility(View.VISIBLE);
            step1.setVisibility(View.GONE);
        }
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
                tanggal_berangkat.setText(dateFormatter.format(newDate.getTime()));
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
    private void simpan(){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        if (nama.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Nama  Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            nama.setFocusable(true);
            pd.dismiss();
        }else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            pd.dismiss();
        }else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            pd.dismiss();
        }else if (kota_tujuan.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Kota Tujuan Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            kota_tujuan.setFocusable(true);
            pd.dismiss();
        }else if (jumlah_orang.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Jumlah Orang Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            jumlah_orang.setFocusable(true);
            pd.dismiss();
        }else if (lama.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Lama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            lama.setFocusable(true);
            pd.dismiss();
        }else if (tanggal_berangkat.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Tanggal Berangkat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            tanggal_berangkat.setFocusable(true);
            pd.dismiss();
        }else if (tujuan_wisata.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Tujuan Wisata Berangkat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            tujuan_wisata.setFocusable(true);
            pd.dismiss();
        }else{
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.TRANSAKSI+"buat", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.cancel();
                    try {
                        JSONObject res = new JSONObject(response);
                        Log.d("pesan", res.toString());
                        if(res.getBoolean("status")){
                            Toast.makeText(Buat_Paket.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getBaseContext(), Dashboard.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getBaseContext(), res.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                    Log.e("errornyaa ", "" + error);
                    Toast.makeText(getBaseContext(), "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", AuthData.getInstance(getBaseContext()).getId_user());
                    params.put("nama_lengkap", nama.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("kota_tujuan", kota_tujuan.getText().toString());
                    params.put("jumlah_orang", jumlah_orang.getSelectedItem().toString());
                    params.put("lama_hari", lama.getSelectedItem().toString());
                    params.put("tgl_berangkat", tanggal_berangkat.getText().toString());
                    params.put("tujuan_wisata", tujuan_wisata.getText().toString());
                    params.put("tiket", tiket.getSelectedItem().toString());
                    params.put("penginapan", penginapan.getSelectedItem().toString());
                    params.put("fasilitas", jenis_fasilitas.getSelectedItem().toString());
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
